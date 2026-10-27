package edu.kit.monitoring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnore
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vital_sign_id")
    @JsonIgnore
    private VitalSign vitalSign;

    @Column(name = "alert_type", nullable = false, length = 50)
    private String alertType; // THRESHOLD_VIOLATION, ABNORMAL_READING, SYSTEM

    @Column(nullable = false, length = 20)
    private String severity = "WARNING"; // WARNING, CRITICAL

    @Column(name = "parameter_name", nullable = false, length = 50)
    private String parameterName; // Heart Rate, SpO2, Temperature, Blood Pressure, Respiratory Rate

    @Column(name = "measured_value", nullable = false)
    private Double measuredValue;

    @Column(name = "threshold_value", nullable = false, length = 100)
    private String thresholdValue;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false)
    private boolean acknowledged = false;

    @Column(name = "acknowledged_by", length = 50)
    private String acknowledgedBy;

    @Column(name = "acknowledged_at")
    private LocalDateTime acknowledgedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Alert() {}

    public Alert(Patient patient, VitalSign vitalSign, String alertType, String severity,
                 String parameterName, Double measuredValue, String thresholdValue, String message) {
        this.patient = patient;
        this.vitalSign = vitalSign;
        this.alertType = alertType;
        this.severity = severity;
        this.parameterName = parameterName;
        this.measuredValue = measuredValue;
        this.thresholdValue = thresholdValue;
        this.message = message;
        this.acknowledged = false;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Long getPatientId() {
        return patient != null ? patient.getId() : null;
    }

    public String getPatientName() {
        return patient != null ? patient.getFullName() : "Unknown";
    }

    public String getPatientCode() {
        return patient != null ? patient.getPatientCode() : "N/A";
    }

    public VitalSign getVitalSign() { return vitalSign; }
    public void setVitalSign(VitalSign vitalSign) { this.vitalSign = vitalSign; }

    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getParameterName() { return parameterName; }
    public void setParameterName(String parameterName) { this.parameterName = parameterName; }

    public Double getMeasuredValue() { return measuredValue; }
    public void setMeasuredValue(Double measuredValue) { this.measuredValue = measuredValue; }

    public String getThresholdValue() { return thresholdValue; }
    public void setThresholdValue(String thresholdValue) { this.thresholdValue = thresholdValue; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isAcknowledged() { return acknowledged; }
    public void setAcknowledged(boolean acknowledged) { this.acknowledged = acknowledged; }

    public String getAcknowledgedBy() { return acknowledgedBy; }
    public void setAcknowledgedBy(String acknowledgedBy) { this.acknowledgedBy = acknowledgedBy; }

    public LocalDateTime getAcknowledgedAt() { return acknowledgedAt; }
    public void setAcknowledgedAt(LocalDateTime acknowledgedAt) { this.acknowledgedAt = acknowledgedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
