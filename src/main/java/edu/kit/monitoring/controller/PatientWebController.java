package edu.kit.monitoring.controller;

import edu.kit.monitoring.dto.PatientDto;
import edu.kit.monitoring.dto.ReadingRequest;
import edu.kit.monitoring.entity.Patient;
import edu.kit.monitoring.service.PatientService;
import edu.kit.monitoring.service.VitalSignService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/patients")
public class PatientWebController {

    private final PatientService patientService;
    private final VitalSignService vitalSignService;

    public PatientWebController(PatientService patientService, VitalSignService vitalSignService) {
        this.patientService = patientService;
        this.vitalSignService = vitalSignService;
    }

    @PostMapping("/add")
    public String addPatient(
            @Valid @ModelAttribute("newPatient") PatientDto patientDto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("patients", patientService.getAllPatients());
            model.addAttribute("selectedStatus", "ALL");
            return "patients/list";
        }

        try {
            Patient saved = patientService.createPatient(patientDto);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Patient " + saved.getFullName() + " (" + saved.getPatientCode() + ") registered successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/patients";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Patient patient = patientService.getPatientById(id);
        model.addAttribute("patientDto", patientService.toDto(patient));
        return "patients/form";
    }

    @PostMapping("/edit/{id}")
    public String updatePatient(
            @PathVariable Long id,
            @Valid @ModelAttribute("patientDto") PatientDto patientDto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "patients/form";
        }

        try {
            Patient updated = patientService.updatePatient(id, patientDto);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Patient " + updated.getFullName() + " updated successfully.");
            return "redirect:/patients/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/patients/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Patient patient = patientService.getPatientById(id);
            String name = patient.getFullName();
            patientService.deletePatient(id);
            redirectAttributes.addFlashAttribute("successMessage", "Patient " + name + " deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not delete patient: " + e.getMessage());
        }
        return "redirect:/patients";
    }

    @PostMapping("/{id}/vitals")
    public String addPatientVitals(
            @PathVariable Long id,
            @Valid @ModelAttribute("newReading") ReadingRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid vital reading inputs. Please verify all values.");
            return "redirect:/patients/" + id;
        }

        try {
            ReadingRequest enriched = new ReadingRequest(
                    id, null,
                    request.heartRate(), request.spo2(), request.temperature(),
                    request.systolicBP(), request.diastolicBP(), request.respiratoryRate(),
                    false
            );
            vitalSignService.recordReading(enriched);
            redirectAttributes.addFlashAttribute("successMessage", "New vital measurement recorded successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error recording vitals: " + e.getMessage());
        }

        return "redirect:/patients/" + id;
    }
}
