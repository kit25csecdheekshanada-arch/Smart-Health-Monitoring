package edu.kit.monitoring.dto;

import jakarta.validation.constraints.*;

public class PatientDto {

    private Long id;

    @NotBlank(message = "Patient ID/Code is required")
    @Size(min = 2, max = 30, message = "Patient Code must be between 2 and 30 characters")
    private String patientCode;

    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 50, message = "First name must not exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age cannot be negative")
    @Max(value = 130, message = "Please enter a valid age")
    private Integer age;

    @NotBlank(message = "Gender is required")
    private String gender;

    private String bloodGroup;

    @Pattern(regexp = "^[0-9+\\-\\s()]{7,25}$", message = "Please enter a valid contact number or leave blank")
    private String contactNumber;

    private String emergencyContact;
    private String roomBedNumber;
    private String assignedDoctor;
    private String monitoringStatus = "ACTIVE";
    private String medicalHistory;
    private String notes;

    public PatientDto() {}

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
}
