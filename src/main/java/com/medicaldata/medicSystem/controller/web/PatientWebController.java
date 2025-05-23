package com.medicaldata.medicSystem.controller.web;

import com.medicaldata.medicSystem.dto.PatientDto;
import com.medicaldata.medicSystem.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.NoSuchElementException;

@Controller
@RequestMapping(value = "/web/patients")
@AllArgsConstructor
public class PatientWebController {
    private PatientService patientService;

    @GetMapping(value = "/home")
    public String goHome(Model model, Authentication authentication) {
        System.out.println("Rendering patient homepage...");
        String patientName = "";
        try{
            patientName = patientService.getPatientFirstnameByUsername(authentication.getName());
        } catch(NoSuchElementException exception){
            String message = exception.getMessage();

            model.addAttribute("message", message);
            return "error";
        }
        model.addAttribute("patientName", patientName);
        return "home";
    }

    @GetMapping(value = "/profile")
    public String getProfile(Model model, Authentication authentication) {
        PatientDto patientDto = patientService.getPatientByUsername(authentication.getName());
        model.addAttribute("patient", patientDto);
        return "profile/profile";
    }

    @GetMapping(value = "/confirm-logout")
    public String confirmLogout() {
        return "confirm-logout";
    }
}