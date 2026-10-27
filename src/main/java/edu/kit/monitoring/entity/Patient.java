package edu.kit.monitoring.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_code", nullable = false, unique = true, length = 30)
    private String patientCode;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false, length = 15)
    private String gender; // Male, Female, Other

    @Column(name = "blood_group", length = 10)
    private String bloodGroup;

    @Column(name = "contact_number", length = 25)
    private String contactNumber;

    @Column(name = "emergency_contact", length = 25)
    private String emergencyContact;

    @Column(name = "room_bed_number", length = 30)
    private String roomBedNumber;

    @Column(name = "assigned_doctor", length = 100)
    private String assignedDoctor;

    @Column(name = "monitoring_status", nullable = false, length = 20)
    private String monitoringStatus = "ACTIVE"; // ACTIVE, INACTIVE, DISCHARGED

    @Column(name = "medical_history", columnDefinition = "TEXT")
    private String medicalHistory;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("recordedAt DESC")
    private List<VitalSign> vitalSigns = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("createdAt DESC")
    private List<Alert> alerts = new ArrayList<>();

    public Patient() {}

    public Patient(String patientCode, String firstName, String lastName, Integer age, String gender,
                   String bloodGroup, String contactNumber, String roomBedNumber, String assignedDoctor) {
        this.patientCode = patientCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.contactNumber = contactNumber;
        this.roomBedNumber = roomBedNumber;
        this.assignedDoctor = assignedDoctor;
        this.monitoringStatus = "ACTIVE";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    public void onPrePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (updatedAt == null) updatedAt = LocalDateTime.now();
        if (monitoringStatus == null) monitoringStatus = "ACTIVE";
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPatientCode() { return patientCode; }
    public void setPatientCode(String patientCode) { this.patientCode = patientCode; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    public String getRoomBedNumber() { return roomBedNumber; }
    public void setRoomBedNumber(String roomBedNumber) { this.roomBedNumber = roomBedNumber; }

    public String getAssignedDoctor() { return assignedDoctor; }
    public void setAssignedDoctor(String assignedDoctor) { this.assignedDoctor = assignedDoctor; }

    public String getMonitoringStatus() { return monitoringStatus; }
    public void setMonitoringStatus(String monitoringStatus) { this.monitoringStatus = monitoringStatus; }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<VitalSign> getVitalSigns() { return vitalSigns; }
    public void setVitalSigns(List<VitalSign> vitalSigns) { this.vitalSigns = vitalSigns; }

    public List<Alert> getAlerts() { return alerts; }
    public void setAlerts(List<Alert> alerts) { this.alerts = alerts; }
}
