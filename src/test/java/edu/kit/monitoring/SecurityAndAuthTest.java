package edu.kit.monitoring;

import edu.kit.monitoring.dto.UserRegistrationDto;
import edu.kit.monitoring.entity.User;
import edu.kit.monitoring.repository.UserRepository;
import edu.kit.monitoring.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SecurityAndAuthTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void unauthenticatedUserRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void publicEndpointsAreAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "doctor", roles = {"DOCTOR"})
    void authenticatedUserCanAccessDashboard() throws Exception {
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"));
    }

    @Test
    @WithMockUser(username = "nurse", roles = {"NURSE"})
    void nurseCannotAccessAdminSettings() throws Exception {
        mockMvc.perform(get("/settings"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminCanAccessSettings() throws Exception {
        mockMvc.perform(get("/settings"))
                .andExpect(status().isOk())
                .andExpect(view().name("settings"));
    }

    @Test
    void testUserRegistrationAndBCryptHashing() {
        UserRegistrationDto dto = new UserRegistrationDto(
                "dr.watson",
                "WatsonPass@123",
                "Dr. John Watson",
                "dr.watson@hospital.org",
                "ROLE_DOCTOR",
                "General Medicine"
        );

        User saved = userService.registerUser(dto);
        assertNotNull(saved.getId());
        assertEquals("dr.watson", saved.getUsername());
        assertNotEquals("WatsonPass@123", saved.getPassword(), "Password must be BCrypt hashed, not plain text");
        assertTrue(passwordEncoder.matches("WatsonPass@123", saved.getPassword()), "Encoded password must match plain password");

        Optional<User> queried = userRepository.findByUsername("dr.watson");
        assertTrue(queried.isPresent());
        assertEquals("dr.watson@hospital.org", queried.get().getEmail());
    }
}
