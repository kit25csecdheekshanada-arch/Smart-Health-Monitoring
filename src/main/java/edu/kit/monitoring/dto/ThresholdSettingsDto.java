package edu.kit.monitoring.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class ThresholdSettingsDto {

    private Long id;

    @NotNull(message = "Min Heart Rate is required")
    @DecimalMin(value = "30.0", message = "Min Heart Rate must be >= 30 bpm")
    @DecimalMax(value = "150.0", message = "Min Heart Rate must be <= 150 bpm")
    private Double hrMin = 60.0;

    @NotNull(message = "Max Heart Rate is required")
    @DecimalMin(value = "60.0", message = "Max Heart Rate must be >= 60 bpm")
    @DecimalMax(value = "220.0", message = "Max Heart Rate must be <= 220 bpm")
    private Double hrMax = 100.0;

    @NotNull(message = "Min SpO2 is required")
    @DecimalMin(value = "70.0", message = "Min SpO2 must be >= 70%")
    @DecimalMax(value = "100.0", message = "Min SpO2 must be <= 100%")
    private Double spo2Min = 95.0;

    @NotNull(message = "Min Temperature is required")
    @DecimalMin(value = "30.0", message = "Min Temperature must be >= 30°C")
    @DecimalMax(value = "38.0", message = "Min Temperature must be <= 38°C")
    private Double tempMin = 36.0;

    @NotNull(message = "Max Temperature is required")
    @DecimalMin(value = "36.0", message = "Max Temperature must be >= 36°C")
    @DecimalMax(value = "43.0", message = "Max Temperature must be <= 43°C")
    private Double tempMax = 37.5;

    @NotNull(message = "Min Systolic BP is required")
    @DecimalMin(value = "60.0", message = "Min Systolic BP must be >= 60 mmHg")
    @DecimalMax(value = "140.0", message = "Min Systolic BP must be <= 140 mmHg")
    private Double sysBpMin = 90.0;

    @NotNull(message = "Max Systolic BP is required")
    @DecimalMin(value = "100.0", message = "Max Systolic BP must be >= 100 mmHg")
    @DecimalMax(value = "220.0", message = "Max Systolic BP must be <= 220 mmHg")
    private Double sysBpMax = 120.0;

    @NotNull(message = "Min Diastolic BP is required")
    @DecimalMin(value = "40.0", message = "Min Diastolic BP must be >= 40 mmHg")
    @DecimalMax(value = "90.0", message = "Min Diastolic BP must be <= 90 mmHg")
    private Double diaBpMin = 60.0;

    @NotNull(message = "Max Diastolic BP is required")
    @DecimalMin(value = "60.0", message = "Max Diastolic BP must be >= 60 mmHg")
    @DecimalMax(value = "140.0", message = "Max Diastolic BP must be <= 140 mmHg")
    private Double diaBpMax = 80.0;

    @NotNull(message = "Min Respiratory Rate is required")
    @DecimalMin(value = "8.0", message = "Min Resp Rate must be >= 8 bpm")
    @DecimalMax(value = "25.0", message = "Min Resp Rate must be <= 25 bpm")
    private Double respRateMin = 12.0;

    @NotNull(message = "Max Respiratory Rate is required")
    @DecimalMin(value = "14.0", message = "Max Resp Rate must be >= 14 bpm")
    @DecimalMax(value = "45.0", message = "Max Resp Rate must be <= 45 bpm")
    private Double respRateMax = 20.0;

    private Integer autoRefreshInterval = 10;
    private boolean soundAlertEnabled = true;

    public ThresholdSettingsDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getHrMin() { return hrMin; }
    public void setHrMin(Double hrMin) { this.hrMin = hrMin; }

    public Double getHrMax() { return hrMax; }
    public void setHrMax(Double hrMax) { this.hrMax = hrMax; }

    public Double getSpo2Min() { return spo2Min; }
    public void setSpo2Min(Double spo2Min) { this.spo2Min = spo2Min; }

    public Double getTempMin() { return tempMin; }
    public void setTempMin(Double tempMin) { this.tempMin = tempMin; }

    public Double getTempMax() { return tempMax; }
    public void setTempMax(Double tempMax) { this.tempMax = tempMax; }

    public Double getSysBpMin() { return sysBpMin; }
    public void setSysBpMin(Double sysBpMin) { this.sysBpMin = sysBpMin; }

    public Double getSysBpMax() { return sysBpMax; }
    public void setSysBpMax(Double sysBpMax) { this.sysBpMax = sysBpMax; }

    public Double getDiaBpMin() { return diaBpMin; }
    public void setDiaBpMin(Double diaBpMin) { this.diaBpMin = diaBpMin; }

    public Double getDiaBpMax() { return diaBpMax; }
    public void setDiaBpMax(Double diaBpMax) { this.diaBpMax = diaBpMax; }

    public Double getRespRateMin() { return respRateMin; }
    public void setRespRateMin(Double respRateMin) { this.respRateMin = respRateMin; }

    public Double getRespRateMax() { return respRateMax; }
    public void setRespRateMax(Double respRateMax) { this.respRateMax = respRateMax; }

    public Integer getAutoRefreshInterval() { return autoRefreshInterval; }
    public void setAutoRefreshInterval(Integer autoRefreshInterval) { this.autoRefreshInterval = autoRefreshInterval; }

    public boolean isSoundAlertEnabled() { return soundAlertEnabled; }
    public void setSoundAlertEnabled(boolean soundAlertEnabled) { this.soundAlertEnabled = soundAlertEnabled; }
}
