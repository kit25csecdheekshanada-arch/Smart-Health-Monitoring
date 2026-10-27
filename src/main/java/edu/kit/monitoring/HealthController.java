package edu.kit.monitoring;

import edu.kit.monitoring.dto.HealthReading;
import edu.kit.monitoring.dto.ReadingRequest;
import edu.kit.monitoring.entity.VitalSign;
import edu.kit.monitoring.service.AlertService;
import edu.kit.monitoring.service.PatientService;
import edu.kit.monitoring.service.VitalSignService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    private final VitalSignService vitalSignService;
    private final PatientService patientService;
    private final AlertService alertService;

    public HealthController(VitalSignService vitalSignService,
                            PatientService patientService,
                            AlertService alertService) {
        this.vitalSignService = vitalSignService;
        this.patientService = patientService;
        this.alertService = alertService;
    }

    @PostMapping("/readings")
    public HealthReading add(@Valid @RequestBody ReadingRequest request) {
        try {
            VitalSign saved = vitalSignService.recordReading(request);
            return vitalSignService.toHealthReading(saved);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not save reading: " + e.getMessage(), e);
        }
    }

    @GetMapping("/readings")
    public List<HealthReading> readings() {
        return vitalSignService.toHealthReadingList(vitalSignService.getLatestReadings());
    }

    @GetMapping("/summary")
    public Map<String, Object> summary() {
        List<VitalSign> rows = vitalSignService.getLatestReadings();
        long alerts = alertService.getActiveAlertCount();
        Map<String, Object> map = new HashMap<>();
        map.put("totalReadings", rows.size());
        map.put("alertReadings", alerts);
        map.put("totalPatients", patientService.countTotalPatients());
        map.put("monitoredPatients", patientService.countActivePatients());
        map.put("latest", rows.isEmpty() ? Map.of() : vitalSignService.toHealthReading(rows.get(0)));
        return map;
    }

    @PostMapping("/demo-reading")
    public HealthReading demo() {
        VitalSign v = vitalSignService.generateSimulatedReading(null, false);
        return vitalSignService.toHealthReading(v);
    }
}
