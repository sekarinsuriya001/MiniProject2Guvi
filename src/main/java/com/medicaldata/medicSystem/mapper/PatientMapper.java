package com.medicaldata.medicSystem.mapper;

import com.medicaldata.medicSystem.dto.save.SavePatientDto;
import com.medicaldata.medicSystem.dto.PatientDto;
import com.medicaldata.medicSystem.model.Patient;

public class PatientMapper {
    public static PatientDto mapToPatientDto(Patient patient){
        return new PatientDto(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getEmail(),
                patient.getMobile(),
                patient.getAddress(),
                patient.getAge(),
                patient.getGender(),
                patient.getBloodGroup(),
                patient.getEmergencyContactName(),
                patient.getEmergencyContactMobile(),
                patient.getEmergencyContactRelation(),
                patient.getPreviousDiagnoses(),
                patient.getSurgeries(),
                patient.getAllergies(),
                patient.getVaccinationHistory(),
                patient.isSmoker(),
                patient.isConsumesAlcohol()
        );
    }

    public static Patient mapToPatientFromSavePatientDto(Long id, SavePatientDto savePatientDto){
        return new Patient(
                id,
                savePatientDto.getFirstName().trim(),
                savePatientDto.getLastName().trim(),
                savePatientDto.getEmail().trim(),
                savePatientDto.getMobile().trim(),
                savePatientDto.getPassword(),
                savePatientDto.getAddress().trim(),
                savePatientDto.getAge(),
                savePatientDto.getGender(),
                savePatientDto.getBloodGroup().trim(),
                savePatientDto.getEmergencyContactName().trim(),
                savePatientDto.getEmergencyContactMobile().trim(),
                savePatientDto.getEmergencyContactRelation().trim(),
                savePatientDto.getPreviousDiagnoses().trim(),
                savePatientDto.getSurgeries().trim(),
                savePatientDto.getAllergies().trim(),
                savePatientDto.getVaccinationHistory().trim(),
                savePatientDto.getIsSmoker(),
                savePatientDto.getConsumesAlcohol()
        );
    }
}