package com.medicaldata.medicSystem.controller.web;

import com.medicaldata.medicSystem.dto.save.SavePatientDto;
import com.medicaldata.medicSystem.service.PatientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/web/patients")
@AllArgsConstructor
public class PatientAuthController {
    private PatientService patientService;

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/register")
    public String register(Model model) {
        model.addAttribute("newPatient", new SavePatientDto());
        return "register";
    }

    @PostMapping(value = "/savePatient")
    public String savePatient(@Valid @ModelAttribute SavePatientDto savePatientDto, Model model) {
//        System.out.println(savePatientDto.toString());
        try{
            patientService.addPatient(savePatientDto);
        } catch (RuntimeException e){
            System.err.println(e.getMessage());
            return "redirect:/web/patients/register?mobileAlreadyExists";
        }
        return "redirect:/web/patients/register?success";
    }

}