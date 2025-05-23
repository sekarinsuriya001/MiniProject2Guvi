package com.medicaldata.medicSystem.service.impl;

import com.medicaldata.medicSystem.dto.DoctorDto;
import com.medicaldata.medicSystem.dto.save.SaveDoctorDto;
import com.medicaldata.medicSystem.mapper.DoctorMapper;
import com.medicaldata.medicSystem.model.Doctor;
import com.medicaldata.medicSystem.repository.DoctorRepository;
import com.medicaldata.medicSystem.service.AppointmentService;
import com.medicaldata.medicSystem.service.DoctorService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private DoctorRepository doctorRepository;
    private AppointmentService appointmentService;

    @Override
    public List<DoctorDto> getAllDoctors() {
        List<DoctorDto> doctorDtoList = new ArrayList<>();
        doctorRepository.findAll().forEach(
                (doctor) -> doctorDtoList.add(DoctorMapper.mapToDoctorDto(doctor))
        );
        if(doctorDtoList.isEmpty()){
            throw new NoSuchElementException("No doctors present in the database.");
        }
        return doctorDtoList;
    }

    @Override
    public DoctorDto getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(
                ()->new NoSuchElementException("No doctor present with the given id: " + id + ".")
        );
        return DoctorMapper.mapToDoctorDto(doctor);
    }

    @Override
    public List<DoctorDto> addAllDoctors(List<SaveDoctorDto> saveDoctorDtoList) {
        List<Doctor> doctorList = new ArrayList<>();
        saveDoctorDtoList.forEach(
                saveDoctorDto -> {
                    doctorList.add(DoctorMapper.mapToDoctorFromSaveDoctorDto(null, saveDoctorDto));
                }
        );

        List<Doctor> savedDoctorList = doctorRepository.saveAll(doctorList);

        List<DoctorDto> doctorDtoList = new ArrayList<>();
        savedDoctorList.forEach(
                fetchedDoctor -> {
                    doctorDtoList.add(DoctorMapper.mapToDoctorDto(fetchedDoctor));
                }
        );
        return doctorDtoList;
    }

    @Override
    public DoctorDto addDoctor(SaveDoctorDto saveDoctorDto) {
        if(doctorRepository.findByMobile(saveDoctorDto.getMobile()).isPresent()){
            throw new RuntimeException("Doctor already exists with given mobile.");
        }
        Doctor savedDoctor = doctorRepository.save(DoctorMapper.mapToDoctorFromSaveDoctorDto(null, saveDoctorDto));
        return DoctorMapper.mapToDoctorDto(savedDoctor);
    }

    @Override
    public DoctorDto updateDoctor(Long id,SaveDoctorDto saveDoctorDto) {
        if(doctorRepository.existsById(id)){
            Doctor updatedDoctor = doctorRepository.save(DoctorMapper.mapToDoctorFromSaveDoctorDto(id, saveDoctorDto));
            return DoctorMapper.mapToDoctorDto(updatedDoctor);
        }
        throw new NoSuchElementException("Update not possible as no one exists under the given doctor's id.");
    }

    @Override
    @Transactional
    public void deleteDoctorById(Long id) {
        if(!doctorRepository.existsById(id)){
            throw new NoSuchElementException("Deletion not possible as no doctor exists under the given doctor's id.");
        }
        if(appointmentService.existsByDoctorId(id)){
            appointmentService.deleteAllAppointmentByDoctorId(id);
        }
        doctorRepository.deleteById(id);
        if(doctorRepository.existsById(id)){
            throw new RuntimeException("Exception while deletion of the doctor with ID: " + id + ". A doctor still exists in the Id.");
        }
    }
}