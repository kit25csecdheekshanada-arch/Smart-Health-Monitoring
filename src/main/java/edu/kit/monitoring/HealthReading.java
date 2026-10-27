package edu.kit.monitoring;

import java.time.LocalDateTime;

public record HealthReading(
        long id, double heartRate, double spo2, double temperature,
        LocalDateTime timestamp, String status, String alertMessage) {

    public HealthReading(edu.kit.monitoring.dto.HealthReading dto) {
        this(dto.id(), dto.heartRate(), dto.spo2(), dto.temperature(),
             dto.timestamp(), dto.status(), dto.alertMessage());
    }
}
