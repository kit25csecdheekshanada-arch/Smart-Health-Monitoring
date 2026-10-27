package edu.kit.monitoring;

import edu.kit.monitoring.dto.PatientDto;
import edu.kit.monitoring.entity.Patient;
import edu.kit.monitoring.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PatientServiceTest {

    @Autowired
    private PatientService patientService;

    @Test
    void testCreateAndFindPatient() {
        PatientDto dto = new PatientDto();
        dto.setPatientCode("P-UNIT-99");
        dto.setFirstName("Alice");
        dto.setLastName("Wonder");
        dto.setAge(29);
        dto.setGender("Female");
        dto.setBloodGroup("B+");
        dto.setContactNumber("+1-555-8888");
        dto.setRoomBedNumber("Ward-505");
        dto.setAssignedDoctor("Dr. Smith");
        dto.setMonitoringStatus("ACTIVE");

        Patient created = patientService.createPatient(dto);
        assertNotNull(created.getId());
        assertEquals("Alice Wonder", created.getFullName());

        Patient found = patientService.getPatientById(created.getId());
        assertEquals("P-UNIT-99", found.getPatientCode());
    }

    @Test
    void testDuplicatePatientCodeThrowsException() {
        PatientDto dto1 = new PatientDto();
        dto1.setPatientCode("P-DUP-01");
        dto1.setFirstName("Bob");
        dto1.setLastName("Builder");
        dto1.setAge(35);
        dto1.setGender("Male");
        patientService.createPatient(dto1);

        PatientDto dto2 = new PatientDto();
        dto2.setPatientCode("P-DUP-01"); // Duplicate code
        dto2.setFirstName("Robert");
        dto2.setLastName("Builder");
        dto2.setAge(40);
        dto2.setGender("Male");

        assertThrows(IllegalArgumentException.class, () -> patientService.createPatient(dto2));
    }

    @Test
    void testSearchAndFilterPatients() {
        List<Patient> results = patientService.searchAndFilter("John", "ALL");
        assertNotNull(results);
    }
}
