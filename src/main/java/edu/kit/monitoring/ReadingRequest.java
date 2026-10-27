package edu.kit.monitoring;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record ReadingRequest(
        @NotNull @DecimalMin("20.0") @DecimalMax("250.0") Double heartRate,
        @NotNull @DecimalMin("50.0") @DecimalMax("100.0") Double spo2,
        @NotNull @DecimalMin("25.0") @DecimalMax("45.0") Double temperature) {

    public edu.kit.monitoring.dto.ReadingRequest toDto() {
        return new edu.kit.monitoring.dto.ReadingRequest(null, null, heartRate, spo2, temperature, 120.0, 80.0, 16.0, false);
    }
}
