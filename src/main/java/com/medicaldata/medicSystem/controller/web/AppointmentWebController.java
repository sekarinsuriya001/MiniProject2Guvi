package com.medicaldata.medicSystem.controller.web;

import com.medicaldata.medicSystem.dto.AppointmentDto;
import com.medicaldata.medicSystem.dto.DoctorDto;
import com.medicaldata.medicSystem.dto.SlotDto;
import com.medicaldata.medicSystem.dto.save.SaveAppointmentDto;
import com.medicaldata.medicSystem.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/web/appointments")
@AllArgsConstructor
public class AppointmentWebController {
    private final AppointmentService appointmentService;

    @GetMapping(value = "/list")
    public String getAppointments(Authentication authentication, Model model) {
        List<List<AppointmentDto>> appointmentDtosList = appointmentService.getAllAppointmentByUsername(authentication.getName());
        List<AppointmentDto> upcomingAppointments = appointmentDtosList.get(0);
        List<AppointmentDto> completedAppointments = appointmentDtosList.get(1);

        model.addAttribute("upcomingAppointments", upcomingAppointments);
        model.addAttribute("noUpcomingAppointments", upcomingAppointments.isEmpty());
        model.addAttribute("completedAppointments", completedAppointments);
        model.addAttribute("noCompletedAppointments", completedAppointments.isEmpty());

        return "appointments/appointments-list";
    }

    @PostMapping("/doctors")
    @ResponseBody
    public List<DoctorDto> getDoctorsBySpeciality(@RequestParam String speciality) {
        return appointmentService.getDoctorsBySpeciality(speciality);
    }

    @GetMapping(value = "/add-appointment")
    public String bookAppointment(Model model, Authentication authentication) {
        SaveAppointmentDto saveAppointmentDto = new SaveAppointmentDto();
        saveAppointmentDto.setPatientId(appointmentService.getPatientIdByUsername(authentication.getName()));
        model.addAttribute("appointment", saveAppointmentDto);
        model.addAttribute("specialties", appointmentService.getAllDoctorSpecialties());
        return "appointments/add-appointment";
    }

    @PostMapping("/checkSlots")
    @ResponseBody
    public List<SlotDto> checkAvailableSlots(@RequestParam Long doctorId, @RequestParam String date) {
        return appointmentService.getAvailableSlots(doctorId, date);
    }

    @PostMapping("/book")
    public String bookAppointment(@Valid @ModelAttribute SaveAppointmentDto saveAppointmentDto, Model model) {
        AppointmentDto savedAppointment = appointmentService.createAppointment(saveAppointmentDto);
        if (savedAppointment == null) {
            model.addAttribute("message", "Error while adding new appointment.");
            return "error";
        }
        model.addAttribute("message", "Appointment added with ID: " + savedAppointment.getId() + ".\nCheck out the appointments main page.");
        return "success";
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteAppointment(@PathVariable("id") Long id, Model model) {
        String message;
        try {
            message = appointmentService.deleteAppointment(id);
        } catch (Exception e) {
            message = e.getMessage();
        }
        model.addAttribute("message", message);
        return "Successfully cancelled the appointment.".equals(message) ? "success" : "error";
    }
}