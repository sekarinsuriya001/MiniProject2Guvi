package com.medicaldata.medicSystem.controller.web;

import com.medicaldata.medicSystem.dto.MedicationDto;
import com.medicaldata.medicSystem.dto.save.SaveMedicationDto;
import com.medicaldata.medicSystem.service.MedicationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping(value = "/web/medications")
@AllArgsConstructor
public class MedicationWebController {
    private MedicationService medicationService;

    @GetMapping(value = "/list")
    public String getMedicationsForUser(Authentication authentication, Model model){
        List<MedicationDto> medicationList = medicationService.getAllMedicationByUsername(authentication.getName());
        if (!medicationList.isEmpty()) {
            model.addAttribute("medicineList", medicationList);
            model.addAttribute("isEmpty", false);
        } else {
            model.addAttribute("isEmpty", true);
        }
        return "medications/medication-list";
    }

    @GetMapping(value = "/view/{id}")
    public String showMedicationById(@PathVariable(value = "id") Long id, Model model) {
        MedicationDto medicationDto = medicationService.getMedicationById(id);
        model.addAttribute("medicineDto", medicationDto);
        return "medications/view-medication";
    }

    @GetMapping(value = "/add-medication")
    public String addMedication(Authentication authentication, Model model) {
        model.addAttribute("medicineDto", new SaveMedicationDto());
        return "medications/add-medication";
    }

    @PostMapping(value = "/add")
    public String saveMedication(Authentication authentication, Model model, @Valid @ModelAttribute SaveMedicationDto newMedication) {
        newMedication.setPatientId(medicationService.getPatientIdByUsername(authentication.getName()));
//        System.out.println("Patient Id: " + newMedication.getPatientId());
        MedicationDto savedMedication;
        try {
            savedMedication = medicationService.saveMedication(newMedication);
//            System.out.println(newMedication.toString());
        } catch (Exception exception) {
            model.addAttribute("message", "Error while adding new medication." + exception.getMessage());
            System.out.println("Stack trace:");
            exception.printStackTrace();
            System.out.println("Resuming application...");
            return "error";
        }
        model.addAttribute("message", "Medication added with medication ID: " + savedMedication.getId() + ".");
        return "success";
    }

    @GetMapping(value = "/update-medication/{id}")
    public String updateMedication(@PathVariable(value = "id") Long id, Model model) {
        MedicationDto medicationDto = medicationService.getMedicationById(id);
        model.addAttribute("medicineDto", medicationDto);
        return "medications/update-medication";
    }

    @PostMapping(value = "/update/{id}")
    public String saveUpdatedMedication(
            @PathVariable(value = "id") Long id,
            @RequestParam(value = "prescriptionDate") LocalDate prescriptionDate,
            Authentication authentication,
            Model model,
            @Valid @ModelAttribute SaveMedicationDto updatedMedication
    ) {
        updatedMedication.setPatientId(medicationService.getPatientIdByUsername(authentication.getName()));

        MedicationDto savedMedication;
        try {
            savedMedication = medicationService.updateMedication(id, prescriptionDate, updatedMedication);
//            System.out.println(updatedMedication.toString());
//            System.out.println(prescriptionDate);
        } catch (Exception exception) {

            model.addAttribute("message", "Error while updating the medication-" + exception.getMessage());
            System.out.println("Stack trace:");
            exception.printStackTrace();
            System.out.println("Resuming application...");
            return "error";
        }
        model.addAttribute("message", "Medication updated with medication ID: " + id + ".");
        return "success";
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteMedication(@PathVariable(value = "id") Long id, Model model) {
        medicationService.deleteMedication(id);
        try {
            medicationService.getMedicationById(id);
        } catch (NoSuchElementException e) {
            String message = "Successfully deleted medication from the database.";
            model.addAttribute("message", message);
            return "success";
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        model.addAttribute("message", "Error deleting the Medication.");
        return "error";
    }
}