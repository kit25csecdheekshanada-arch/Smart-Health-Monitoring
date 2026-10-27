package edu.kit.monitoring;

import edu.kit.monitoring.entity.Patient;
import edu.kit.monitoring.entity.VitalSign;
import edu.kit.monitoring.service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @Test
    void testGenerateCsvReport() throws IOException {
        Patient patient = new Patient("P-CSV", "Jane", "Doe", 30, "Female", "A+", "555-4321", "Bed-10", "Dr. Report");
        patient.setId(101L);

        VitalSign v1 = new VitalSign(patient, 72.0, 36.6, 98.0, 118.0, 78.0, 16.0, "NORMAL", null, false);
        v1.setId(1L);
        v1.setRecordedAt(LocalDateTime.now().minusMinutes(10));

        VitalSign v2 = new VitalSign(patient, 110.0, 38.2, 92.0, 140.0, 90.0, 24.0, "WARNING", "Tachycardia; High Temp", false);
        v2.setId(2L);
        v2.setRecordedAt(LocalDateTime.now().minusMinutes(5));

        byte[] csvBytes = reportService.generateCsvReport(List.of(v1, v2));
        assertNotNull(csvBytes);
        assertTrue(csvBytes.length > 0);

        String csvString = new String(csvBytes, StandardCharsets.UTF_8);
        assertTrue(csvString.contains("Reading ID,Recorded At,Patient Code,Patient Name"));
        assertTrue(csvString.contains("P-CSV"));
        assertTrue(csvString.contains("Jane Doe"));
        assertTrue(csvString.contains("110.0"));
    }

    @Test
    void testCalculateVitalStatistics() {
        Patient patient = new Patient("P-STAT", "Sam", "Smith", 45, "Male", "B+", "555-7777", "Bed-20", "Dr. Stat");
        patient.setId(102L);

        VitalSign v1 = new VitalSign(patient, 60.0, 36.0, 95.0, 100.0, 70.0, 12.0, "NORMAL", null, false);
        VitalSign v2 = new VitalSign(patient, 80.0, 37.0, 99.0, 120.0, 80.0, 16.0, "NORMAL", null, false);
        VitalSign v3 = new VitalSign(patient, 100.0, 38.0, 97.0, 140.0, 90.0, 20.0, "WARNING", "High HR", false);

        Map<String, Object> stats = reportService.calculateVitalStatistics(List.of(v1, v2, v3));

        assertEquals(3, stats.get("totalReadings"));
        assertEquals(2L, stats.get("normalCount"));
        assertEquals(1L, stats.get("warningCount"));
        assertEquals(60.0, (Double) stats.get("hrMin"));
        assertEquals(100.0, (Double) stats.get("hrMax"));
        assertEquals(80.0, (Double) stats.get("hrAvg"));
    }
}
