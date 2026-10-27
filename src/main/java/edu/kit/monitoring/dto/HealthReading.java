package edu.kit.monitoring.dto;

import java.time.LocalDateTime;

public record HealthReading(
        long id,
        Long patientId,
        String patientName,
        double heartRate,
        double spo2,
        double temperature,
        Double systolicBP,
        Double diastolicBP,
        Double respiratoryRate,
        LocalDateTime timestamp,
        String status,
        String alertMessage,
        boolean isSimulated
) {
    // Convenience constructor matching original prototype record:
    public HealthReading(long id, double heartRate, double spo2, double temperature,
                         LocalDateTime timestamp, String status, String alertMessage) {
        this(id, null, "General", heartRate, spo2, temperature, 120.0, 80.0, 16.0, timestamp, status, alertMessage, false);
    }

    public String bpString() {
        if (systolicBP != null && diastolicBP != null) {
            return String.format("%.0f/%.0f", systolicBP, diastolicBP);
        }
        return "N/A";
    }

    public String getBpString() {
        return bpString();
    }
}
