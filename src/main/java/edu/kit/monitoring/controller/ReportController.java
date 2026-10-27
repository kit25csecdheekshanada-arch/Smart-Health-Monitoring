package edu.kit.monitoring.controller;

import edu.kit.monitoring.entity.Patient;
import edu.kit.monitoring.entity.VitalSign;
import edu.kit.monitoring.service.PatientService;
import edu.kit.monitoring.service.ReportService;
import edu.kit.monitoring.service.VitalSignService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final PatientService patientService;
    private final VitalSignService vitalSignService;
    private final ReportService reportService;

    public ReportController(PatientService patientService,
                            VitalSignService vitalSignService,
                            ReportService reportService) {
        this.patientService = patientService;
        this.vitalSignService = vitalSignService;
        this.reportService = reportService;
    }

    @GetMapping("/view")
    public String viewReport(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Model model) {

        List<Patient> patients = patientService.getAllPatients();
        model.addAttribute("patients", patients);

        Patient patient = null;
        if (patientId != null) {
            patient = patientService.getPatientById(patientId);
        } else if (!patients.isEmpty()) {
            patient = patients.get(0);
            patientId = patient.getId();
        }

        LocalDateTime start = (startDate != null && !startDate.isBlank()) ?
                LocalDate.parse(startDate).atStartOfDay() :
                LocalDateTime.now().minusDays(7);

        LocalDateTime end = (endDate != null && !endDate.isBlank()) ?
                LocalDate.parse(endDate).atTime(LocalTime.MAX) :
                LocalDateTime.now();

        List<VitalSign> readings = (patient != null) ?
                vitalSignService.getPatientReadingsBetween(patient, start, end) :
                List.of();

        Map<String, Object> stats = reportService.calculateVitalStatistics(readings);

        model.addAttribute("selectedPatient", patient);
        model.addAttribute("selectedPatientId", patientId);
        model.addAttribute("startDate", start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        model.addAttribute("endDate", end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        model.addAttribute("readings", readings);
        model.addAttribute("stats", stats);

        return "reports";
    }

    @GetMapping("/export-csv")
    public ResponseEntity<byte[]> exportCsv(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) throws IOException {

        LocalDateTime start = (startDate != null && !startDate.isBlank()) ?
                LocalDate.parse(startDate).atStartOfDay() :
                LocalDateTime.now().minusDays(30);

        LocalDateTime end = (endDate != null && !endDate.isBlank()) ?
                LocalDate.parse(endDate).atTime(LocalTime.MAX) :
                LocalDateTime.now();

        List<VitalSign> readings;
        String filename;

        if (patientId != null) {
            Patient patient = patientService.getPatientById(patientId);
            readings = vitalSignService.getPatientReadingsBetween(patient, start, end);
            filename = "health-report-" + patient.getPatientCode() + "-" + LocalDate.now() + ".csv";
        } else {
            readings = vitalSignService.getLatestReadings();
            filename = "health-report-all-patients-" + LocalDate.now() + ".csv";
        }

        byte[] csvData = reportService.generateCsvReport(readings);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvData);
    }
}
