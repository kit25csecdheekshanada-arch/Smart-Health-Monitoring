package edu.kit.monitoring.service;

import edu.kit.monitoring.dto.UserRegistrationDto;
import edu.kit.monitoring.entity.User;
import edu.kit.monitoring.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public User registerUser(UserRegistrationDto dto) {
        if (usernameExists(dto.getUsername())) {
            throw new IllegalArgumentException("Username '" + dto.getUsername() + "' is already taken.");
        }
        if (emailExists(dto.getEmail())) {
            throw new IllegalArgumentException("Email '" + dto.getEmail() + "' is already registered.");
        }

        String role = dto.getRole();
        if (role == null || !role.startsWith("ROLE_")) {
            role = "ROLE_STAFF";
        }

        User user = new User(
                dto.getUsername(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getFullName(),
                dto.getEmail(),
                role,
                dto.getDepartment()
        );
        return userRepository.save(user);
    }

    public long countUsers() {
        return userRepository.count();
    }
}
