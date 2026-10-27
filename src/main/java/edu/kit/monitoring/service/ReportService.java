package edu.kit.monitoring.service;

import edu.kit.monitoring.entity.Patient;
import edu.kit.monitoring.entity.VitalSign;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReportService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public byte[] generateCsvReport(List<VitalSign> readings) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out, StandardCharsets.UTF_8),
                CSVFormat.DEFAULT.builder().setHeader(
                        "Reading ID",
                        "Recorded At",
                        "Patient Code",
                        "Patient Name",
                        "Heart Rate (bpm)",
                        "SpO2 (%)",
                        "Temperature (°C)",
                        "Systolic BP (mmHg)",
                        "Diastolic BP (mmHg)",
                        "Respiratory Rate (bpm)",
                        "Status",
                        "Alert Message",
                        "Is Simulated"
                ).build())) {

            for (VitalSign v : readings) {
                printer.printRecord(
                        v.getId(),
                        v.getRecordedAt() != null ? v.getRecordedAt().format(DATE_TIME_FORMATTER) : "",
                        v.getPatientCode(),
                        v.getPatientName(),
                        v.getHeartRate(),
                        v.getSpo2(),
                        v.getTemperature(),
                        v.getSystolicBP() != null ? v.getSystolicBP() : "",
                        v.getDiastolicBP() != null ? v.getDiastolicBP() : "",
                        v.getRespiratoryRate() != null ? v.getRespiratoryRate() : "",
                        v.getStatus(),
                        v.getAlertMessage() != null ? v.getAlertMessage() : "",
                        v.isSimulated() ? "YES" : "NO"
                );
            }
            printer.flush();
        }
        return out.toByteArray();
    }

    public Map<String, Object> calculateVitalStatistics(List<VitalSign> readings) {
        Map<String, Object> stats = new HashMap<>();
        if (readings == null || readings.isEmpty()) {
            stats.put("totalReadings", 0);
            stats.put("normalCount", 0);
            stats.put("warningCount", 0);
            stats.put("criticalCount", 0);
            return stats;
        }

        stats.put("totalReadings", readings.size());
        stats.put("normalCount", readings.stream().filter(v -> "NORMAL".equalsIgnoreCase(v.getStatus())).count());
        stats.put("warningCount", readings.stream().filter(v -> "WARNING".equalsIgnoreCase(v.getStatus())).count());
        stats.put("criticalCount", readings.stream().filter(v -> "CRITICAL".equalsIgnoreCase(v.getStatus())).count());

        DoubleSummaryStatistics hrStats = readings.stream()
                .filter(v -> v.getHeartRate() != null)
                .mapToDouble(VitalSign::getHeartRate)
                .summaryStatistics();
        stats.put("hrMin", hrStats.getCount() > 0 ? hrStats.getMin() : 0);
        stats.put("hrMax", hrStats.getCount() > 0 ? hrStats.getMax() : 0);
        stats.put("hrAvg", hrStats.getCount() > 0 ? Math.round(hrStats.getAverage() * 10.0) / 10.0 : 0);

        DoubleSummaryStatistics spo2Stats = readings.stream()
                .filter(v -> v.getSpo2() != null)
                .mapToDouble(VitalSign::getSpo2)
                .summaryStatistics();
        stats.put("spo2Min", spo2Stats.getCount() > 0 ? spo2Stats.getMin() : 0);
        stats.put("spo2Max", spo2Stats.getCount() > 0 ? spo2Stats.getMax() : 0);
        stats.put("spo2Avg", spo2Stats.getCount() > 0 ? Math.round(spo2Stats.getAverage() * 10.0) / 10.0 : 0);

        DoubleSummaryStatistics tempStats = readings.stream()
                .filter(v -> v.getTemperature() != null)
                .mapToDouble(VitalSign::getTemperature)
                .summaryStatistics();
        stats.put("tempMin", tempStats.getCount() > 0 ? tempStats.getMin() : 0);
        stats.put("tempMax", tempStats.getCount() > 0 ? tempStats.getMax() : 0);
        stats.put("tempAvg", tempStats.getCount() > 0 ? Math.round(tempStats.getAverage() * 10.0) / 10.0 : 0);

        DoubleSummaryStatistics sysStats = readings.stream()
                .filter(v -> v.getSystolicBP() != null)
                .mapToDouble(VitalSign::getSystolicBP)
                .summaryStatistics();
        stats.put("sysMin", sysStats.getCount() > 0 ? sysStats.getMin() : 0);
        stats.put("sysMax", sysStats.getCount() > 0 ? sysStats.getMax() : 0);
        stats.put("sysAvg", sysStats.getCount() > 0 ? Math.round(sysStats.getAverage() * 10.0) / 10.0 : 0);

        DoubleSummaryStatistics diaStats = readings.stream()
                .filter(v -> v.getDiastolicBP() != null)
                .mapToDouble(VitalSign::getDiastolicBP)
                .summaryStatistics();
        stats.put("diaMin", diaStats.getCount() > 0 ? diaStats.getMin() : 0);
        stats.put("diaMax", diaStats.getCount() > 0 ? diaStats.getMax() : 0);
        stats.put("diaAvg", diaStats.getCount() > 0 ? Math.round(diaStats.getAverage() * 10.0) / 10.0 : 0);

        DoubleSummaryStatistics rrStats = readings.stream()
                .filter(v -> v.getRespiratoryRate() != null)
                .mapToDouble(VitalSign::getRespiratoryRate)
                .summaryStatistics();
        stats.put("rrMin", rrStats.getCount() > 0 ? rrStats.getMin() : 0);
        stats.put("rrMax", rrStats.getCount() > 0 ? rrStats.getMax() : 0);
        stats.put("rrAvg", rrStats.getCount() > 0 ? Math.round(rrStats.getAverage() * 10.0) / 10.0 : 0);

        return stats;
    }
}
