package edu.kit.monitoring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vital_signs")
public class VitalSign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnore
    private Patient patient;

    @Column(name = "heart_rate", nullable = false)
    private Double heartRate;

    @Column(nullable = false)
    private Double temperature;

    @Column(nullable = false)
    private Double spo2;

    @Column(name = "systolic_bp")
    private Double systolicBP;

    @Column(name = "diastolic_bp")
    private Double diastolicBP;

    @Column(name = "respiratory_rate")
    private Double respiratoryRate;

    @Column(nullable = false, length = 20)
    private String status = "NORMAL"; // NORMAL, WARNING, CRITICAL

    @Column(name = "alert_message", length = 500)
    private String alertMessage;

    @Column(name = "is_simulated", nullable = false)
    private boolean isSimulated = false;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt = LocalDateTime.now();

    public VitalSign() {}

    public VitalSign(Patient patient, Double heartRate, Double temperature, Double spo2,
                     Double systolicBP, Double diastolicBP, Double respiratoryRate,
                     String status, String alertMessage, boolean isSimulated) {
        this.patient = patient;
        this.heartRate = heartRate;
        this.temperature = temperature;
        this.spo2 = spo2;
        this.systolicBP = systolicBP;
        this.diastolicBP = diastolicBP;
        this.respiratoryRate = respiratoryRate;
        this.status = status;
        this.alertMessage = alertMessage;
        this.isSimulated = isSimulated;
        this.recordedAt = LocalDateTime.now();
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

    public Double getHeartRate() { return heartRate; }
    public void setHeartRate(Double heartRate) { this.heartRate = heartRate; }

    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }

    public Double getSpo2() { return spo2; }
    public void setSpo2(Double spo2) { this.spo2 = spo2; }

    public Double getSystolicBP() { return systolicBP; }
    public void setSystolicBP(Double systolicBP) { this.systolicBP = systolicBP; }

    public Double getDiastolicBP() { return diastolicBP; }
    public void setDiastolicBP(Double diastolicBP) { this.diastolicBP = diastolicBP; }

    public Double getRespiratoryRate() { return respiratoryRate; }
    public void setRespiratoryRate(Double respiratoryRate) { this.respiratoryRate = respiratoryRate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAlertMessage() { return alertMessage; }
    public void setAlertMessage(String alertMessage) { this.alertMessage = alertMessage; }

    public boolean isSimulated() { return isSimulated; }
    public void setSimulated(boolean simulated) { isSimulated = simulated; }

    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }

    public String getBpString() {
        if (systolicBP != null && diastolicBP != null) {
            return String.format("%.0f/%.0f", systolicBP, diastolicBP);
        }
        return "N/A";
    }
}
