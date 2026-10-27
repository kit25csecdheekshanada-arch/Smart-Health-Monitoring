package edu.kit.monitoring.service;

import edu.kit.monitoring.dto.PatientDto;
import edu.kit.monitoring.entity.Patient;
import edu.kit.monitoring.exception.ResourceNotFoundException;
import edu.kit.monitoring.repository.PatientRepository;
import edu.kit.monitoring.repository.VitalSignRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final VitalSignRepository vitalSignRepository;

    public PatientService(PatientRepository patientRepository, VitalSignRepository vitalSignRepository) {
        this.patientRepository = patientRepository;
        this.vitalSignRepository = vitalSignRepository;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + id));
    }

    public Patient getPatientByCode(String code) {
        return patientRepository.findByPatientCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with code: " + code));
    }

    public List<Patient> searchAndFilter(String query, String status) {
        boolean hasQuery = query != null && !query.trim().isEmpty();
        boolean hasStatus = status != null && !status.trim().isEmpty() && !"ALL".equalsIgnoreCase(status);

        if (hasQuery && hasStatus) {
            return patientRepository.searchPatientsWithStatus(query.trim(), status.trim());
        } else if (hasQuery) {
            return patientRepository.searchPatients(query.trim());
        } else if (hasStatus) {
            return patientRepository.findByMonitoringStatus(status.trim());
        } else {
            return patientRepository.findAll();
        }
    }

    @Transactional
    public Patient createPatient(PatientDto dto) {
        if (patientRepository.existsByPatientCode(dto.getPatientCode())) {
            throw new IllegalArgumentException("Patient with code " + dto.getPatientCode() + " already exists.");
        }

        Patient patient = new Patient();
        copyDtoToEntity(dto, patient);
        return patientRepository.save(patient);
    }

    @Transactional
    public Patient updatePatient(Long id, PatientDto dto) {
        Patient patient = getPatientById(id);

        if (!patient.getPatientCode().equalsIgnoreCase(dto.getPatientCode()) &&
            patientRepository.existsByPatientCode(dto.getPatientCode())) {
            throw new IllegalArgumentException("Patient with code " + dto.getPatientCode() + " already exists.");
        }

        copyDtoToEntity(dto, patient);
        return patientRepository.save(patient);
    }

    @Transactional
    public void deletePatient(Long id) {
        Patient patient = getPatientById(id);
        patientRepository.delete(patient);
    }

    public long countTotalPatients() {
        return patientRepository.count();
    }

    public long countActivePatients() {
        return patientRepository.countByMonitoringStatus("ACTIVE");
    }

    public long countAbnormalPatients() {
        return vitalSignRepository.countPatientsWithAbnormalReadings();
    }

    public PatientDto toDto(Patient p) {
        PatientDto dto = new PatientDto();
        dto.setId(p.getId());
        dto.setPatientCode(p.getPatientCode());
        dto.setFirstName(p.getFirstName());
        dto.setLastName(p.getLastName());
        dto.setAge(p.getAge());
        dto.setGender(p.getGender());
        dto.setBloodGroup(p.getBloodGroup());
        dto.setContactNumber(p.getContactNumber());
        dto.setEmergencyContact(p.getEmergencyContact());
        dto.setRoomBedNumber(p.getRoomBedNumber());
        dto.setAssignedDoctor(p.getAssignedDoctor());
        dto.setMonitoringStatus(p.getMonitoringStatus());
        dto.setMedicalHistory(p.getMedicalHistory());
        dto.setNotes(p.getNotes());
        return dto;
    }

    private void copyDtoToEntity(PatientDto dto, Patient patient) {
        patient.setPatientCode(dto.getPatientCode());
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setAge(dto.getAge());
        patient.setGender(dto.getGender());
        patient.setBloodGroup(dto.getBloodGroup());
        patient.setContactNumber(dto.getContactNumber());
        patient.setEmergencyContact(dto.getEmergencyContact());
        patient.setRoomBedNumber(dto.getRoomBedNumber());
        patient.setAssignedDoctor(dto.getAssignedDoctor());
        patient.setMonitoringStatus(dto.getMonitoringStatus());
        patient.setMedicalHistory(dto.getMedicalHistory());
        patient.setNotes(dto.getNotes());
    }
}
