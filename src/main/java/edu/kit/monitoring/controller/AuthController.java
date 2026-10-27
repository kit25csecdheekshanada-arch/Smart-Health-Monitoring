package edu.kit.monitoring.controller;

import edu.kit.monitoring.dto.ThresholdSettingsDto;
import edu.kit.monitoring.dto.UserRegistrationDto;
import edu.kit.monitoring.service.SettingsService;
import edu.kit.monitoring.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;
    private final SettingsService settingsService;

    public AuthController(UserService userService, SettingsService settingsService) {
        this.userService = userService;
        this.settingsService = settingsService;
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("userDto") UserRegistrationDto userDto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            return "register";
        }

        try {
            userService.registerUser(userDto);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Registration successful! Please sign in with your credentials.");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }

    @PostMapping("/settings/update")
    public String updateSettings(
            @Valid @ModelAttribute("settingsDto") ThresholdSettingsDto settingsDto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid threshold configurations. Please check constraints.");
            return "redirect:/settings";
        }

        try {
            settingsService.updateSettings(settingsDto);
            redirectAttributes.addFlashAttribute("successMessage", "Monitoring thresholds and preferences updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update settings: " + e.getMessage());
        }

        return "redirect:/settings";
    }
}
