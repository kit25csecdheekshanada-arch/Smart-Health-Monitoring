package edu.kit.monitoring.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record ReadingRequest(
        Long patientId,
        String patientCode,

        @NotNull(message = "Heart rate is required")
        @DecimalMin(value = "20.0", message = "Heart rate must be at least 20 bpm")
        @DecimalMax(value = "250.0", message = "Heart rate must not exceed 250 bpm")
        Double heartRate,

        @NotNull(message = "SpO2 is required")
        @DecimalMin(value = "50.0", message = "SpO2 must be at least 50%")
        @DecimalMax(value = "100.0", message = "SpO2 must not exceed 100%")
        Double spo2,

        @NotNull(message = "Temperature is required")
        @DecimalMin(value = "25.0", message = "Temperature must be at least 25°C")
        @DecimalMax(value = "45.0", message = "Temperature must not exceed 45°C")
        Double temperature,

        @DecimalMin(value = "40.0", message = "Systolic BP must be at least 40 mmHg")
        @DecimalMax(value = "260.0", message = "Systolic BP must not exceed 260 mmHg")
        Double systolicBP,

        @DecimalMin(value = "30.0", message = "Diastolic BP must be at least 30 mmHg")
        @DecimalMax(value = "160.0", message = "Diastolic BP must not exceed 160 mmHg")
        Double diastolicBP,

        @DecimalMin(value = "4.0", message = "Respiratory rate must be at least 4 breaths/min")
        @DecimalMax(value = "60.0", message = "Respiratory rate must not exceed 60 breaths/min")
        Double respiratoryRate,

        Boolean isSimulated
) {
    // Convenience constructor for the original 3-field prototype calls:
    public ReadingRequest(Double heartRate, Double spo2, Double temperature) {
        this(null, null, heartRate, spo2, temperature, 120.0, 80.0, 16.0, false);
    }

    public ReadingRequest(Long patientId, Double heartRate, Double spo2, Double temperature,
                          Double systolicBP, Double diastolicBP, Double respiratoryRate) {
        this(patientId, null, heartRate, spo2, temperature, systolicBP, diastolicBP, respiratoryRate, false);
    }
}
