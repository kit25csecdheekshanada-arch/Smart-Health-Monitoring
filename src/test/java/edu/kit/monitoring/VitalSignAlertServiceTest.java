package edu.kit.monitoring;

import edu.kit.monitoring.dto.ReadingRequest;
import edu.kit.monitoring.entity.Alert;
import edu.kit.monitoring.entity.Patient;
import edu.kit.monitoring.entity.VitalSign;
import edu.kit.monitoring.repository.AlertRepository;
import edu.kit.monitoring.repository.PatientRepository;
import edu.kit.monitoring.service.AlertService;
import edu.kit.monitoring.service.VitalSignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class VitalSignAlertServiceTest {

    @Autowired
    private VitalSignService vitalSignService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AlertService alertService;

    @Autowired
    private AlertRepository alertRepository;

    private Patient testPatient;

    @BeforeEach
    void setUp() {
        testPatient = patientRepository.findAll().stream().findFirst().orElseGet(() -> {
            Patient p = new Patient("P-TEST", "Test", "Patient", 40, "Male", "A+", "555-0000", "Bed-01", "Dr. Test");
            return patientRepository.save(p);
        });
    }

    @Test
    void testRecordNormalReading() {
        ReadingRequest request = new ReadingRequest(
                testPatient.getId(),
                testPatient.getPatientCode(),
                72.0, // HR normal (60-100)
                98.5, // SpO2 normal (>= 95)
                36.7, // Temp normal (36-37.5)
                120.0, // Systolic normal (90-120)
                80.0,  // Diastolic normal (60-80)
                16.0,  // RR normal (12-20)
                false
        );

        VitalSign recorded = vitalSignService.recordReading(request);
        assertNotNull(recorded.getId());
        assertEquals("NORMAL", recorded.getStatus());
        assertEquals(72.0, recorded.getHeartRate());
        assertEquals(98.5, recorded.getSpo2());
    }

    @Test
    void testRecordAbnormalReadingTriggersAlert() {
        long initialAlertCount = alertRepository.count();

        // Tachycardia and severe hypoxemia reading
        ReadingRequest request = new ReadingRequest(
                testPatient.getId(),
                testPatient.getPatientCode(),
                135.0, // Tachycardia
                88.0,  // Critical hypoxemia
                39.0,  // High fever
                160.0, // Hypertension
                100.0,
                28.0,  // Tachypnea
                false
        );

        VitalSign recorded = vitalSignService.recordReading(request);
        assertNotNull(recorded.getId());
        assertTrue("WARNING".equals(recorded.getStatus()) || "CRITICAL".equals(recorded.getStatus()));

        long newAlertCount = alertRepository.count();
        assertTrue(newAlertCount > initialAlertCount, "Threshold violations should create Alert records");

        List<Alert> alerts = alertService.getActiveAlerts();
        assertFalse(alerts.isEmpty());
    }

    @Test
    void testAcknowledgeAlert() {
        // Trigger alert
        ReadingRequest request = new ReadingRequest(
                testPatient.getId(),
                testPatient.getPatientCode(),
                145.0, 91.0, 38.8, 155.0, 95.0, 26.0, false
        );
        vitalSignService.recordReading(request);

        List<Alert> unacknowledged = alertService.getActiveAlerts();
        assertFalse(unacknowledged.isEmpty());

        Alert alert = unacknowledged.get(0);
        assertFalse(alert.isAcknowledged());

        Alert acknowledged = alertService.acknowledgeAlert(alert.getId(), "Dr. Johnson");
        assertTrue(acknowledged.isAcknowledged());
        assertEquals("Dr. Johnson", acknowledged.getAcknowledgedBy());
        assertNotNull(acknowledged.getAcknowledgedAt());
    }
}
