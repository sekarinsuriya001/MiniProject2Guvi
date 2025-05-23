package com.medicaldata.medicSystem.service;

import com.medicaldata.medicSystem.dto.AppointmentDto;
import com.medicaldata.medicSystem.dto.DoctorDto;
import com.medicaldata.medicSystem.dto.SlotDto;
import com.medicaldata.medicSystem.dto.save.SaveAppointmentDto;

import java.util.List;

public interface AppointmentService {
    List<AppointmentDto> getAllAppointments();

    AppointmentDto getAppointmentById(Long id);

    AppointmentDto createAppointment(SaveAppointmentDto saveAppointmentDto);

    String deleteAppointment(Long id);

    boolean existsByDoctorId(Long doctorId);

    void deleteAllAppointmentByDoctorId(Long doctorId);

    boolean existsByPatientId(Long id);

    void deleteAllAppointmentByPatientId(Long id);

    List<List<AppointmentDto>> getAllAppointmentByUsername(String name);

    List<DoctorDto> getDoctorsBySpeciality(String specialty);

    List<String> getAllDoctorSpecialties();
    List<SlotDto> getAvailableSlots(Long doctorId, String date);

    Long getPatientIdByUsername(String name);
}