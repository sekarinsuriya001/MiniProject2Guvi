package com.medicaldata.medicSystem.service.impl;

import com.medicaldata.medicSystem.dto.MedicationDto;
import com.medicaldata.medicSystem.dto.save.SaveMedicationDto;
import com.medicaldata.medicSystem.mapper.MedicationMapper;
import com.medicaldata.medicSystem.model.Medication;
import com.medicaldata.medicSystem.repository.MedicationRepository;
import com.medicaldata.medicSystem.repository.PatientRepository;
import com.medicaldata.medicSystem.service.MedicationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@AllArgsConstructor
public class MedicationServiceImpl implements MedicationService {
    private MedicationRepository medicationRepository;
    private PatientRepository patientRepository;

    @Override
    public List<MedicationDto> getAllMedication() {
        List<MedicationDto> medicationDtos = new ArrayList<>();
        medicationRepository.findAll().forEach(
                medication -> medicationDtos.add(MedicationMapper.mapToMedicationDto(medication))
        );
        if (medicationDtos.isEmpty())
            throw new NoSuchElementException("No medication exists in the database for any patient.");
        return medicationDtos;
    }

    @Override
    public MedicationDto getMedicationById(Long id) {
        Medication medication = medicationRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("No Medication exists under given id: " + id + ".")
        );
        return MedicationMapper.mapToMedicationDto(medication);
    }

    @Override
    public MedicationDto saveMedication(SaveMedicationDto saveMedicationDto) {
        Medication medication = MedicationMapper.mapToMedicationFromSaveMedicationDto(
                null,
                patientRepository.findById(Long.parseLong(saveMedicationDto.getPatientId())).orElseThrow(
                        () -> new NoSuchElementException("No patient exists as per the patient id given.")
                ),
                LocalDate.now(),
                saveMedicationDto
        );
        Medication savedMedication = medicationRepository.save(medication);
        return MedicationMapper.mapToMedicationDto(savedMedication);
    }

    @Override
    public MedicationDto updateMedication(Long id, LocalDate prescriptionDate, SaveMedicationDto saveMedicationDto) {
        Long updatedPatientId = Long.parseLong(saveMedicationDto.getPatientId().trim());
        if (!medicationRepository.existsById(id)) {
            throw new NoSuchElementException("No medication exists under the given medication's id.");
        }
        if (!Objects.equals(medicationRepository.findById(id).get().getPatient().getId(), updatedPatientId)) {
            System.out.println(medicationRepository.findById(id).get().getPatient().getId());
            System.out.println(updatedPatientId);
            throw new NoSuchElementException("No such medication exists for the mentioned patient's id.");
        }
        Medication medication = MedicationMapper.mapToMedicationFromSaveMedicationDto(
                id,
                patientRepository.findById(updatedPatientId).orElseThrow(
                        () -> new NoSuchElementException("No patient exists under given medication's patient id.")
                ),
                prescriptionDate,
                saveMedicationDto
        );
        Medication savedMedication = medicationRepository.save(medication);
        return MedicationMapper.mapToMedicationDto(savedMedication);
    }

    @Override
    public String deleteMedication(Long id) {
        if (!medicationRepository.existsById(id)) {
            throw new NoSuchElementException("Deletion not possible as no medication exists under the given medication id: " + id + ".");
        }
        medicationRepository.deleteById(id);
        if (medicationRepository.existsById(id)) {
            throw new NoSuchElementException("Error occurred. Medication exists even after deletion.");
        }
        return "Successfully deleted medication with id: " + id + ".";
    }

    @Override
    public boolean existsByPatientId(Long patientId) {
        return medicationRepository.existsByPatientId(patientId);
    }

    @Override
    public void deleteAllMedicationByPatientId(Long patientId) {
        int rows = medicationRepository.deleteAllByPatientId(patientId);
        System.out.printf("%d rows deleted from the medication table.%n", rows);
        if (this.existsByPatientId(patientId)) {
            throw new RuntimeException("Error while deleting appointments of patient with id: " + patientId + ".");
        }
    }

    @Override
    public List<MedicationDto> getAllMedicationByUsername(String username) {
        Long patientId = patientRepository.findByMobile(username).get().getId();
        List<MedicationDto> medicationDtoList = new ArrayList<>();
        medicationRepository.findAllByPatientId(patientId).forEach(
                medication -> medicationDtoList.add(MedicationMapper.mapToMedicationDto(medication))
        );
        return medicationDtoList;
    }

    @Override
    public String getPatientIdByUsername(String username) {
        return patientRepository.findByMobile(username).get().getId().toString();
    }
}