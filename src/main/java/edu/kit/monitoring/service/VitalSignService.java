package edu.kit.monitoring.service;

import edu.kit.monitoring.dto.HealthReading;
import edu.kit.monitoring.dto.ReadingRequest;
import edu.kit.monitoring.entity.MonitoringSetting;
import edu.kit.monitoring.entity.Patient;
import edu.kit.monitoring.entity.VitalSign;
import edu.kit.monitoring.repository.PatientRepository;
import edu.kit.monitoring.repository.VitalSignRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class VitalSignService {

    private final VitalSignRepository vitalSignRepository;
    private final PatientRepository patientRepository;
    private final AlertService alertService;
    private final SettingsService settingsService;
    private final Random random = new Random();

    public VitalSignService(VitalSignRepository vitalSignRepository,
                            PatientRepository patientRepository,
                            AlertService alertService,
                            SettingsService settingsService) {
        this.vitalSignRepository = vitalSignRepository;
        this.patientRepository = patientRepository;
        this.alertService = alertService;
        this.settingsService = settingsService;
    }

    @Transactional
    public VitalSign recordReading(ReadingRequest req) {
        Patient patient = resolvePatient(req);
        MonitoringSetting settings = settingsService.getSettings();

        double hr = req.heartRate();
        double spo2 = req.spo2();
        double temp = req.temperature();
        Double sys = req.systolicBP() != null ? req.systolicBP() : 120.0;
        Double dia = req.diastolicBP() != null ? req.diastolicBP() : 80.0;
        Double rr = req.respiratoryRate() != null ? req.respiratoryRate() : 16.0;
        boolean simulated = Boolean.TRUE.equals(req.isSimulated());

        List<String> alertMessages = new ArrayList<>();
        String overallStatus = "NORMAL";

        // 1. Evaluate Heart Rate
        if (hr < settings.getHrMin() || hr > settings.getHrMax()) {
            String severity = (hr < 45 || hr > 140) ? "CRITICAL" : "WARNING";
            if ("CRITICAL".equals(severity)) overallStatus = "CRITICAL";
            else if (!"CRITICAL".equals(overallStatus)) overallStatus = "WARNING";

            String msg = String.format("Heart rate (%.0f bpm) outside normal threshold (%.0f-%.0f bpm)",
                    hr, settings.getHrMin(), settings.getHrMax());
            alertMessages.add(msg);
            alertService.createAlert(patient, null, severity, "Heart Rate", hr,
                    String.format("%.0f-%.0f bpm", settings.getHrMin(), settings.getHrMax()), msg);
        }

        // 2. Evaluate SpO2
        if (spo2 < settings.getSpo2Min()) {
            String severity = (spo2 < 90.0) ? "CRITICAL" : "WARNING";
            if ("CRITICAL".equals(severity)) overallStatus = "CRITICAL";
            else if (!"CRITICAL".equals(overallStatus)) overallStatus = "WARNING";

            String msg = String.format("SpO2 (%.1f%%) dropped below threshold (%.1f%%)",
                    spo2, settings.getSpo2Min());
            alertMessages.add(msg);
            alertService.createAlert(patient, null, severity, "SpO2", spo2,
                    String.format(">= %.1f%%", settings.getSpo2Min()), msg);
        }

        // 3. Evaluate Temperature
        if (temp < settings.getTempMin() || temp > settings.getTempMax()) {
            String severity = (temp > 38.5 || temp < 35.0) ? "CRITICAL" : "WARNING";
            if ("CRITICAL".equals(severity)) overallStatus = "CRITICAL";
            else if (!"CRITICAL".equals(overallStatus)) overallStatus = "WARNING";

            String msg = String.format("Temperature (%.1f°C) outside normal threshold (%.1f-%.1f°C)",
                    temp, settings.getTempMin(), settings.getTempMax());
            alertMessages.add(msg);
            alertService.createAlert(patient, null, severity, "Temperature", temp,
                    String.format("%.1f-%.1f°C", settings.getTempMin(), settings.getTempMax()), msg);
        }

        // 4. Evaluate Blood Pressure
        if (sys < settings.getSysBpMin() || sys > settings.getSysBpMax() ||
            dia < settings.getDiaBpMin() || dia > settings.getDiaBpMax()) {
            String severity = (sys >= 160 || sys <= 80 || dia >= 100 || dia <= 50) ? "CRITICAL" : "WARNING";
            if ("CRITICAL".equals(severity)) overallStatus = "CRITICAL";
            else if (!"CRITICAL".equals(overallStatus)) overallStatus = "WARNING";

            String msg = String.format("Blood Pressure (%.0f/%.0f mmHg) abnormal (Normal: %.0f-%.0f / %.0f-%.0f mmHg)",
                    sys, dia, settings.getSysBpMin(), settings.getSysBpMax(), settings.getDiaBpMin(), settings.getDiaBpMax());
            alertMessages.add(msg);
            alertService.createAlert(patient, null, severity, "Blood Pressure", sys,
                    String.format("%.0f-%.0f/%.0f-%.0f", settings.getSysBpMin(), settings.getSysBpMax(), settings.getDiaBpMin(), settings.getDiaBpMax()), msg);
        }

        // 5. Evaluate Respiratory Rate
        if (rr < settings.getRespRateMin() || rr > settings.getRespRateMax()) {
            String severity = (rr < 10 || rr > 30) ? "CRITICAL" : "WARNING";
            if ("CRITICAL".equals(severity)) overallStatus = "CRITICAL";
            else if (!"CRITICAL".equals(overallStatus)) overallStatus = "WARNING";

            String msg = String.format("Respiratory rate (%.0f bpm) outside normal threshold (%.0f-%.0f bpm)",
                    rr, settings.getRespRateMin(), settings.getRespRateMax());
            alertMessages.add(msg);
            alertService.createAlert(patient, null, severity, "Respiratory Rate", rr,
                    String.format("%.0f-%.0f bpm", settings.getRespRateMin(), settings.getRespRateMax()), msg);
        }

        String fullAlertMessage = String.join("; ", alertMessages);

        VitalSign vitalSign = new VitalSign(
                patient, hr, temp, spo2, sys, dia, rr,
                overallStatus, fullAlertMessage, simulated
        );

        return vitalSignRepository.save(vitalSign);
    }

    public Patient resolvePatient(ReadingRequest req) {
        if (req.patientId() != null) {
            return patientRepository.findById(req.patientId())
                    .orElseGet(this::getDefaultPatient);
        }
        if (req.patientCode() != null && !req.patientCode().isBlank()) {
            return patientRepository.findByPatientCode(req.patientCode())
                    .orElseGet(this::getDefaultPatient);
        }
        return getDefaultPatient();
    }

    private Patient getDefaultPatient() {
        return patientRepository.findAll().stream().findFirst().orElseGet(() -> {
            Patient p = new Patient("P-1001", "John", "Doe", 45, "Male", "O+", "+1-555-0199", "ICU-Bed-01", "Dr. Sarah Johnson");
            return patientRepository.save(p);
        });
    }

    public List<VitalSign> getLatestReadings() {
        return vitalSignRepository.findTop100ByOrderByRecordedAtDesc();
    }

    public List<VitalSign> getTop10RecentReadings() {
        return vitalSignRepository.findTop10ByOrderByRecordedAtDesc();
    }

    public List<VitalSign> getRecentReadingsForPatient(Patient patient) {
        return vitalSignRepository.findTop20ByPatientOrderByRecordedAtDesc(patient);
    }

    public List<VitalSign> getPatientReadingsBetween(Patient patient, LocalDateTime start, LocalDateTime end) {
        return vitalSignRepository.findByPatientAndRecordedAtBetweenOrderByRecordedAtAsc(patient, start, end);
    }

    public VitalSign getLatestReadingForPatient(Patient patient) {
        return vitalSignRepository.findFirstByPatientOrderByRecordedAtDesc(patient).orElse(null);
    }

    @Transactional
    public VitalSign generateSimulatedReading(Long patientId, boolean forceAbnormal) {
        Patient patient = (patientId != null) ?
                patientRepository.findById(patientId).orElseGet(this::getDefaultPatient) :
                getDefaultPatient();

        double hr = 68 + random.nextDouble() * 22; // 68 - 90
        double spo2 = 96.5 + random.nextDouble() * 3.4; // 96.5 - 99.9
        double temp = 36.4 + random.nextDouble() * 0.8; // 36.4 - 37.2
        double sys = 115 + random.nextInt(15); // 115 - 130
        double dia = 75 + random.nextInt(10); // 75 - 85
        double rr = 14 + random.nextInt(5); // 14 - 19

        if (forceAbnormal || random.nextDouble() < 0.25) {
            int scenario = random.nextInt(5);
            switch (scenario) {
                case 0 -> hr = 125 + random.nextInt(30); // Tachycardia
                case 1 -> spo2 = 88.0 + random.nextDouble() * 4.0; // Hypoxemia
                case 2 -> temp = 38.6 + random.nextDouble() * 1.0; // High Fever
                case 3 -> { sys = 165 + random.nextInt(25); dia = 100 + random.nextInt(15); } // Hypertension
                case 4 -> rr = 28 + random.nextInt(8); // Tachypnea
            }
        }

        ReadingRequest request = new ReadingRequest(
                patient.getId(),
                patient.getPatientCode(),
                Math.round(hr * 10.0) / 10.0,
                Math.round(spo2 * 10.0) / 10.0,
                Math.round(temp * 10.0) / 10.0,
                (double) Math.round(sys),
                (double) Math.round(dia),
                (double) Math.round(rr),
                true
        );

        return recordReading(request);
    }

    public HealthReading toHealthReading(VitalSign v) {
        if (v == null) return null;
        return new HealthReading(
                v.getId(),
                v.getPatientId(),
                v.getPatientName(),
                v.getHeartRate(),
                v.getSpo2(),
                v.getTemperature(),
                v.getSystolicBP(),
                v.getDiastolicBP(),
                v.getRespiratoryRate(),
                v.getRecordedAt(),
                v.getStatus(),
                v.getAlertMessage(),
                v.isSimulated()
        );
    }

    public List<HealthReading> toHealthReadingList(List<VitalSign> list) {
        return list.stream().map(this::toHealthReading).toList();
    }
}
