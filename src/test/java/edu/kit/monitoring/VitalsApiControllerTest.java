package edu.kit.monitoring;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kit.monitoring.dto.ReadingRequest;
import edu.kit.monitoring.entity.Patient;
import edu.kit.monitoring.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VitalsApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PatientRepository patientRepository;

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = patientRepository.findAll().stream().findFirst().orElseGet(() -> {
            Patient p = new Patient("P-IOT-TEST", "IoT", "Tester", 50, "Male", "O+", "555-1234", "ICU-99", "Dr. Tech");
            return patientRepository.save(p);
        });
    }

    @Test
    void testPostVitalsValidRequest() throws Exception {
        ReadingRequest request = new ReadingRequest(
                patient.getId(),
                patient.getPatientCode(),
                78.0,
                98.0,
                36.8,
                120.0,
                80.0,
                16.0,
                false
        );

        mockMvc.perform(post("/api/v1/vitals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.reading.heartRate").value(78.0))
                .andExpect(jsonPath("$.reading.spo2").value(98.0));
    }

    @Test
    void testPostVitalsInvalidHeartRateFailsValidation() throws Exception {
        // Heart rate 10.0 is below min 20.0
        ReadingRequest invalid = new ReadingRequest(
                patient.getId(),
                patient.getPatientCode(),
                10.0, // Invalid < 20
                98.0,
                36.8,
                120.0,
                80.0,
                16.0,
                false
        );

        mockMvc.perform(post("/api/v1/vitals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLegacyApiReadingsEndpoint() throws Exception {
        mockMvc.perform(get("/api/readings"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void testLegacyApiSummaryEndpoint() throws Exception {
        mockMvc.perform(get("/api/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalReadings").exists());
    }
}
