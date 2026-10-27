package edu.kit.monitoring.controller;

import edu.kit.monitoring.dto.HealthReading;
import edu.kit.monitoring.dto.ReadingRequest;
import edu.kit.monitoring.entity.Alert;
import edu.kit.monitoring.entity.Patient;
import edu.kit.monitoring.entity.VitalSign;
import edu.kit.monitoring.service.AlertService;
import edu.kit.monitoring.service.PatientService;
import edu.kit.monitoring.service.VitalSignService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class VitalsApiController {

    private final VitalSignService vitalSignService;
    private final PatientService patientService;
    private final AlertService alertService;

    public VitalsApiController(VitalSignService vitalSignService,
                               PatientService patientService,
                               AlertService alertService) {
        this.vitalSignService = vitalSignService;
        this.patientService = patientService;
        this.alertService = alertService;
    }

    /**
     * IoT Ingestion Endpoint: Designed for ESP32, Arduino, Raspberry Pi, or external sensors.
     * Receives JSON payload with patientId/code and all 5 vital parameters.
     */
    @PostMapping("/vitals")
    public ResponseEntity<Map<String, Object>> ingestVitals(
            @Valid @RequestBody ReadingRequest request,
            @RequestHeader(value = "X-Device-Id", required = false) String deviceId) {

        VitalSign saved = vitalSignService.recordReading(request);
        HealthReading reading = vitalSignService.toHealthReading(saved);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("message", "Vital sign recorded successfully");
        response.put("reading", reading);
        response.put("evaluationStatus", saved.getStatus());
        if (saved.getAlertMessage() != null && !saved.getAlertMessage().isEmpty()) {
            response.put("alertGenerated", true);
            response.put("alertDetails", saved.getAlertMessage());
        } else {
            response.put("alertGenerated", false);
        }

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/vitals/latest/{patientId}")
    public ResponseEntity<HealthReading> getLatestByPatient(@PathVariable Long patientId) {
        Patient patient = patientService.getPatientById(patientId);
        VitalSign latest = vitalSignService.getLatestReadingForPatient(patient);
        if (latest == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(vitalSignService.toHealthReading(latest));
    }

    @GetMapping("/vitals/history/{patientId}")
    public ResponseEntity<List<HealthReading>> getHistoryByPatient(@PathVariable Long patientId) {
        Patient patient = patientService.getPatientById(patientId);
        List<VitalSign> list = vitalSignService.getRecentReadingsForPatient(patient);
        return ResponseEntity.ok(vitalSignService.toHealthReadingList(list));
    }

    @PostMapping("/vitals/simulate")
    public ResponseEntity<HealthReading> simulateReading(
            @RequestParam(required = false) Long patientId,
            @RequestParam(defaultValue = "false") boolean forceAbnormal) {

        VitalSign simulated = vitalSignService.generateSimulatedReading(patientId, forceAbnormal);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vitalSignService.toHealthReading(simulated));
    }

    @GetMapping("/alerts/unread-count")
    public ResponseEntity<Map<String, Object>> getUnreadAlertCount() {
        long count = alertService.getActiveAlertCount();
        Map<String, Object> map = new HashMap<>();
        map.put("activeAlerts", count);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/alerts/acknowledge/{id}")
    public ResponseEntity<Map<String, Object>> acknowledgeAlert(
            @PathVariable Long id,
            Authentication authentication) {

        String staffName = (authentication != null) ? authentication.getName() : "Staff";
        Alert alert = alertService.acknowledgeAlert(id, staffName);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("alertId", alert.getId());
        response.put("acknowledged", true);
        response.put("acknowledgedBy", alert.getAcknowledgedBy());
        return ResponseEntity.ok(response);
    }
}
