package com.medicaldata.medicSystem.controller.web;

import com.medicaldata.medicSystem.dto.save.SaveDoctorDto;
import com.medicaldata.medicSystem.service.DoctorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/web/doctors")
@AllArgsConstructor
public class DoctorWebController {
    private DoctorService doctorService;

    @GetMapping(value = "/register")
    public String register(Model model) {
        model.addAttribute("newDoctor", new SaveDoctorDto());
        return "doctor-register";
    }

    @PostMapping(value = "/saveDoctor")
    public String register(Model model, @Valid @ModelAttribute SaveDoctorDto saveDoctorDto) {
//        System.out.println(saveDoctorDto.toString());
        try{
            doctorService.addDoctor(saveDoctorDto);
        } catch (RuntimeException e){
            System.err.println(e.getMessage());
            return "redirect:/web/doctors/register?mobileAlreadyExists";
        }
        return "redirect:/web/doctors/register?success";
    }
}