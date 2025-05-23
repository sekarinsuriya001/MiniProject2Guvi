package com.medicaldata.medicSystem.service.impl;

import com.medicaldata.medicSystem.dto.PatientDto;
import com.medicaldata.medicSystem.dto.save.SavePatientDto;
import com.medicaldata.medicSystem.mapper.PatientMapper;
import com.medicaldata.medicSystem.model.Patient;
import com.medicaldata.medicSystem.repository.PatientRepository;
import com.medicaldata.medicSystem.service.AppointmentService;
import com.medicaldata.medicSystem.service.MedicationService;
import com.medicaldata.medicSystem.service.PatientService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {
    private PatientRepository patientRepository;
    private AppointmentService appointmentService;
    private MedicationService medicationService;
    private PasswordEncoder passwordEncoder;

    @Override
    public List<PatientDto> getAllPatients() {
        List<PatientDto> patientDtoList = new ArrayList<>();
        patientRepository.findAll().forEach(
                (patient) -> patientDtoList.add(PatientMapper.mapToPatientDto(patient))
        );
        if (patientDtoList.isEmpty()) {
            throw new NoSuchElementException("No Patients present in the database.");
        }
        return patientDtoList;
    }


    @Override
    public PatientDto getPatientById(Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("No patient present in our database under given id: " + id + ".")
        );
        return PatientMapper.mapToPatientDto(patient);
    }


    @Override
    public PatientDto addPatient(SavePatientDto savePatientDto) {
        if(patientRepository.findByMobile(savePatientDto.getMobile()).isPresent()){
            throw new RuntimeException("Patient already exists with given mobile.");
        }
        Patient newPatient = PatientMapper.mapToPatientFromSavePatientDto(null, savePatientDto);
        newPatient.setPassword(passwordEncoder.encode(newPatient.getPassword()));
        Patient savedPatient = patientRepository.save(newPatient);
        return PatientMapper.mapToPatientDto(savedPatient);
    }

    @Override
    public PatientDto updatePatient(Long id, SavePatientDto savePatientDto) {
        if (patientRepository.existsById(id)) {
            Patient updatedPatient = patientRepository.save(PatientMapper.mapToPatientFromSavePatientDto(id, savePatientDto));
            return PatientMapper.mapToPatientDto(updatedPatient);
        }
        throw new NoSuchElementException("Update not possible as no patient exists under the given patient id: " + id + ".");
    }

    @Override
    @Transactional
    public void deletePatientById(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new NoSuchElementException("Deletion not possible as no patient exists under the given patient id" + id + ".");
        }
        if (appointmentService.existsByPatientId(id)) {
            appointmentService.deleteAllAppointmentByPatientId(id);
        }
        if (medicationService.existsByPatientId(id)) {
            medicationService.deleteAllMedicationByPatientId(id);
        }
        patientRepository.deleteById(id);
        if (patientRepository.existsById(id)) {
            throw new RuntimeException("Exception while deletion of the patient with ID: " + id + ". A patient still exists in the Id.");
        }
    }


    @Override
    public PatientDto getPatientByUsername(String username) {
        Patient fetchedPatient = patientRepository.findByMobile(username).orElseThrow(
                () -> new NoSuchElementException("No patient present in our database under given mobile no.: " + username + "."));
        return PatientMapper.mapToPatientDto(fetchedPatient);
    }


    @Override
    public String getPatientFirstnameByUsername(String username) {
        Patient fetchedPatient = patientRepository.findByMobile(username).orElseThrow(
                () -> new NoSuchElementException("No patient present in our database under given mobile no.: " + username + "."));
        return fetchedPatient.getFirstName();
    }
}
