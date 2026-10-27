package edu.kit.monitoring.controller;

import edu.kit.monitoring.dto.PatientDto;
import edu.kit.monitoring.dto.ReadingRequest;
import edu.kit.monitoring.entity.Alert;
import edu.kit.monitoring.entity.MonitoringSetting;
import edu.kit.monitoring.entity.Patient;
import edu.kit.monitoring.entity.VitalSign;
import edu.kit.monitoring.service.AlertService;
import edu.kit.monitoring.service.PatientService;
import edu.kit.monitoring.service.SettingsService;
import edu.kit.monitoring.service.VitalSignService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class PageController {

    private final PatientService patientService;
    private final VitalSignService vitalSignService;
    private final AlertService alertService;
    private final SettingsService settingsService;

    public PageController(PatientService patientService,
                          VitalSignService vitalSignService,
                          AlertService alertService,
                          SettingsService settingsService) {
        this.patientService = patientService;
        this.vitalSignService = vitalSignService;
        this.alertService = alertService;
        this.settingsService = settingsService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long totalPatients = patientService.countTotalPatients();
        long activePatients = patientService.countActivePatients();
        long abnormalPatients = patientService.countAbnormalPatients();
        long activeAlerts = alertService.getActiveAlertCount();

        List<VitalSign> recentReadings = vitalSignService.getTop10RecentReadings();
        List<Alert> recentAlerts = alertService.getTop10RecentAlerts();
        List<Patient> patients = patientService.getAllPatients();
        MonitoringSetting settings = settingsService.getSettings();

        model.addAttribute("totalPatients", totalPatients);
        model.addAttribute("activePatients", activePatients);
        model.addAttribute("abnormalPatients", abnormalPatients);
        model.addAttribute("activeAlerts", activeAlerts);
        model.addAttribute("recentReadings", recentReadings);
        model.addAttribute("recentAlerts", recentAlerts);
        model.addAttribute("patients", patients);
        model.addAttribute("settings", settings);
        model.addAttribute("currentDateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy · HH:mm:ss")));

        // Latest reading overall
        if (!recentReadings.isEmpty()) {
            model.addAttribute("latestReading", recentReadings.get(0));
        }

        return "dashboard";
    }

    @GetMapping("/monitoring")
    public String monitoring(@RequestParam(required = false) Long patientId, Model model) {
        List<Patient> patients = patientService.getAllPatients();
        Patient selectedPatient;

        if (patientId != null) {
            selectedPatient = patientService.getPatientById(patientId);
        } else if (!patients.isEmpty()) {
            selectedPatient = patients.get(0);
        } else {
            selectedPatient = null;
        }

        VitalSign latestReading = selectedPatient != null ?
                vitalSignService.getLatestReadingForPatient(selectedPatient) : null;
        List<VitalSign> history = selectedPatient != null ?
                vitalSignService.getRecentReadingsForPatient(selectedPatient) : List.of();
        MonitoringSetting settings = settingsService.getSettings();

        model.addAttribute("patients", patients);
        model.addAttribute("selectedPatient", selectedPatient);
        model.addAttribute("latestReading", latestReading);
        model.addAttribute("history", history);
        model.addAttribute("settings", settings);
        model.addAttribute("readingRequest", new ReadingRequest(null, null, 72.0, 98.0, 36.6, 120.0, 80.0, 16.0, false));

        return "monitoring";
    }

    @GetMapping("/patients")
    public String patients(
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "ALL") String status,
            Model model) {

        List<Patient> patients = patientService.searchAndFilter(query, status);
        model.addAttribute("patients", patients);
        model.addAttribute("searchQuery", query);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("newPatient", new PatientDto());
        return "patients/list";
    }

    @GetMapping("/patients/{id}")
    public String patientDetails(@PathVariable Long id, Model model) {
        Patient patient = patientService.getPatientById(id);
        VitalSign latest = vitalSignService.getLatestReadingForPatient(patient);
        List<VitalSign> vitalsHistory = vitalSignService.getRecentReadingsForPatient(patient);
        List<Alert> alerts = alertService.getTop10AlertsForPatient(patient);
        MonitoringSetting settings = settingsService.getSettings();

        model.addAttribute("patient", patient);
        model.addAttribute("latestReading", latest);
        model.addAttribute("vitalsHistory", vitalsHistory);
        model.addAttribute("alerts", alerts);
        model.addAttribute("settings", settings);
        model.addAttribute("newReading", new ReadingRequest(patient.getId(), patient.getPatientCode(),
                75.0, 98.0, 36.6, 120.0, 80.0, 16.0, false));

        return "patients/details";
    }

    @GetMapping("/alerts")
    public String alerts(
            @RequestParam(required = false, defaultValue = "all") String filter,
            Model model) {

        List<Alert> alertList;
        if ("active".equalsIgnoreCase(filter)) {
            alertList = alertService.getActiveAlerts();
        } else {
            alertList = alertService.getRecentAlerts();
        }

        model.addAttribute("alerts", alertList);
        model.addAttribute("activeCount", alertService.getActiveAlertCount());
        model.addAttribute("currentFilter", filter);
        return "alerts";
    }

    @GetMapping("/reports")
    public String reports(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        StringBuilder url = new StringBuilder("redirect:/reports/view");
        boolean hasParam = false;
        if (patientId != null) {
            url.append(hasParam ? "&" : "?").append("patientId=").append(patientId);
            hasParam = true;
        }
        if (startDate != null && !startDate.isBlank()) {
            url.append(hasParam ? "&" : "?").append("startDate=").append(startDate);
            hasParam = true;
        }
        if (endDate != null && !endDate.isBlank()) {
            url.append(hasParam ? "&" : "?").append("endDate=").append(endDate);
        }
        return url.toString();
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("settingsDto", settingsService.getSettingsDto());
        return "settings";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userDto", new edu.kit.monitoring.dto.UserRegistrationDto());
        return "register";
    }
}
