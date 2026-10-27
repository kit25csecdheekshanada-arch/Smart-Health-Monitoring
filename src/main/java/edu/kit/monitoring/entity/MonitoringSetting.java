package edu.kit.monitoring.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "monitoring_settings")
public class MonitoringSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hr_min", nullable = false)
    private Double hrMin = 60.0;

    @Column(name = "hr_max", nullable = false)
    private Double hrMax = 100.0;

    @Column(name = "spo2_min", nullable = false)
    private Double spo2Min = 95.0;

    @Column(name = "temp_min", nullable = false)
    private Double tempMin = 36.0;

    @Column(name = "temp_max", nullable = false)
    private Double tempMax = 37.5;

    @Column(name = "sys_bp_min", nullable = false)
    private Double sysBpMin = 90.0;

    @Column(name = "sys_bp_max", nullable = false)
    private Double sysBpMax = 120.0;

    @Column(name = "dia_bp_min", nullable = false)
    private Double diaBpMin = 60.0;

    @Column(name = "dia_bp_max", nullable = false)
    private Double diaBpMax = 80.0;

    @Column(name = "resp_rate_min", nullable = false)
    private Double respRateMin = 12.0;

    @Column(name = "resp_rate_max", nullable = false)
    private Double respRateMax = 20.0;

    @Column(name = "auto_refresh_interval", nullable = false)
    private Integer autoRefreshInterval = 10; // seconds

    @Column(name = "sound_alert_enabled", nullable = false)
    private boolean soundAlertEnabled = true;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public MonitoringSetting() {}

    @PrePersist
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

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

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
