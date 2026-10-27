package edu.kit.monitoring.service;

import edu.kit.monitoring.entity.Alert;
import edu.kit.monitoring.entity.Patient;
import edu.kit.monitoring.entity.VitalSign;
import edu.kit.monitoring.exception.ResourceNotFoundException;
import edu.kit.monitoring.repository.AlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Transactional
    public Alert createAlert(Patient patient, VitalSign vitalSign, String severity,
                             String parameterName, Double measuredValue, String thresholdValue, String message) {
        Alert alert = new Alert(patient, vitalSign, "THRESHOLD_VIOLATION", severity,
                parameterName, measuredValue, thresholdValue, message);
        return alertRepository.save(alert);
    }

    public List<Alert> getActiveAlerts() {
        return alertRepository.findByAcknowledgedFalseOrderByCreatedAtDesc();
    }

    public List<Alert> getRecentAlerts() {
        return alertRepository.findTop50ByOrderByCreatedAtDesc();
    }

    public List<Alert> getTop10RecentAlerts() {
        return alertRepository.findTop10ByOrderByCreatedAtDesc();
    }

    public List<Alert> getAlertsForPatient(Patient patient) {
        return alertRepository.findByPatientOrderByCreatedAtDesc(patient);
    }

    public List<Alert> getTop10AlertsForPatient(Patient patient) {
        return alertRepository.findTop10ByPatientOrderByCreatedAtDesc(patient);
    }

    public long getActiveAlertCount() {
        return alertRepository.countByAcknowledgedFalse();
    }

    @Transactional
    public Alert acknowledgeAlert(Long alertId, String acknowledgedBy) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + alertId));
        alert.setAcknowledged(true);
        alert.setAcknowledgedBy(acknowledgedBy != null ? acknowledgedBy : "Staff");
        alert.setAcknowledgedAt(LocalDateTime.now());
        return alertRepository.save(alert);
    }
}
