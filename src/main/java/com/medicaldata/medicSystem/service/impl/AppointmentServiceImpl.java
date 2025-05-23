package com.medicaldata.medicSystem.service.impl;

import com.medicaldata.medicSystem.dto.AppointmentDto;
import com.medicaldata.medicSystem.dto.DoctorDto;
import com.medicaldata.medicSystem.dto.SlotDto;
import com.medicaldata.medicSystem.dto.save.SaveAppointmentDto;
import com.medicaldata.medicSystem.mapper.AppointmentMapper;
import com.medicaldata.medicSystem.mapper.DoctorMapper;
import com.medicaldata.medicSystem.model.Appointment;
import com.medicaldata.medicSystem.repository.AppointmentRepository;
import com.medicaldata.medicSystem.repository.DoctorRepository;
import com.medicaldata.medicSystem.repository.PatientRepository;
import com.medicaldata.medicSystem.service.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private AppointmentRepository appointmentRepository;
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;

    @Override
    public List<AppointmentDto> getAllAppointments() {
        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        appointmentRepository.findAll().forEach(
                appointment -> appointmentDtos.add(AppointmentMapper.mapToAppointmentDto(appointment))
        );
        return appointmentDtos;
    }

    @Override
    public AppointmentDto getAppointmentById(Long id) {
        return AppointmentMapper.mapToAppointmentDto(appointmentRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("No appointment exists under given id: " + id + ".")
        ));
    }

    @Override
    public AppointmentDto createAppointment(SaveAppointmentDto saveAppointmentDto) {
        Appointment appointment = AppointmentMapper.mapToAppointmentFromSaveAppointmentDto(saveAppointmentDto);
        appointment.setPatient(
                patientRepository.findById(saveAppointmentDto.getPatientId()).orElseThrow(
                        () -> new NoSuchElementException("No patient exists under given patient id: " + saveAppointmentDto.getPatientId() + ".")
                )
        );
        appointment.setDoctor(
                doctorRepository.findById(saveAppointmentDto.getDoctorId()).orElseThrow(
                        () -> new NoSuchElementException("No doctor exists under given doctor id: " + saveAppointmentDto.getDoctorId() + ".")
                )
        );
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return AppointmentMapper.mapToAppointmentDto(savedAppointment);
    }

    @Override
    public String deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new NoSuchElementException("No appointment exists under the given id: " + id + ".");
        }
        appointmentRepository.deleteById(id);
        if (appointmentRepository.existsById(id)) {
            throw new RuntimeException("Appointment still exists with the given id: " + id + ".");
        }
        return "Successfully cancelled the appointment.";
    }

    @Override
    public boolean existsByPatientId(Long patientId) {
        return appointmentRepository.existsByPatientId(patientId);
    }

    @Override
    public void deleteAllAppointmentByPatientId(Long patientId) {
        int rows = appointmentRepository.deleteAllByPatientId(patientId);
        System.out.printf("%d rows deleted from the appointment table.%n", rows);
        if (this.existsByPatientId(patientId)) {
            throw new RuntimeException("Error while deleting appointments of patient with id: " + patientId + ".");
        }
    }

    @Override
    public List<List<AppointmentDto>> getAllAppointmentByUsername(String name) {
        Long patientId = patientRepository.findByMobile(name).get().getId();
        List<List<AppointmentDto>> appointmentDtosList = new ArrayList<>();
        List<AppointmentDto> upcomingAppointmentDtos = new ArrayList<>();
        List<AppointmentDto> completedAppointmentDtos = new ArrayList<>();
        appointmentRepository.getAllByPatientId(patientId).forEach(
                (appointment) -> {
                    if(appointment.getAppointmentDateTime().isBefore(LocalDateTime.now())){
                        completedAppointmentDtos.add(AppointmentMapper.mapToAppointmentDto(appointment));
                    } else {
                        upcomingAppointmentDtos.add(AppointmentMapper.mapToAppointmentDto(appointment));
                    }
                }
        );
        appointmentDtosList.add(upcomingAppointmentDtos);
        appointmentDtosList.add(completedAppointmentDtos);
        return appointmentDtosList;
    }

    @Override
    public boolean existsByDoctorId(Long doctorId) {
        return appointmentRepository.existsByDoctorId(doctorId);
    }

    @Override
    public void deleteAllAppointmentByDoctorId(Long doctorId) {
        int rows = appointmentRepository.deleteAllByDoctorId(doctorId);
        System.out.printf("%d rows deleted from the appointment table.%n", rows);
        if (this.existsByDoctorId(doctorId)) {
            throw new RuntimeException("Error while deleting appointments of Doctor with id: " + doctorId + ".");
        }
    }

    @Override
    public List<DoctorDto> getDoctorsBySpeciality(String speciality){
        List<DoctorDto> doctorDtos = new ArrayList<>();
        doctorRepository.getAllBySpeciality(speciality).forEach(
                doctor -> doctorDtos.add(DoctorMapper.mapToDoctorDto(doctor))
        );
        return doctorDtos;
    }

    @Override
    public List<String> getAllDoctorSpecialties(){
        List<String> specialities = doctorRepository.getDistinctSpeciality();
        if (specialities.isEmpty()){
            throw new RuntimeException("No specialities found as no doctors are present in the database.");
        }
        return specialities;
    }

    @Override
    public List<SlotDto> getAvailableSlots(Long doctorId, String date) {
        System.out.println("Getting available slots...");
        List<SlotDto> slotDtos = new ArrayList<>();
        LocalDate selectedDate = LocalDate.parse(date);
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(18, 00);
        int slotDuration = 30;

        LocalTime time = startTime;

        // adding slots
        for(; time.isBefore(endTime); time = time.plusMinutes(slotDuration)){
            System.out.println("for loop for adding slots started");
            System.out.println(time.format(DateTimeFormatter.ofPattern("hh:mm a")));
            if(time.equals(LocalTime.of(12,30)) || time.equals(LocalTime.of(13,0))){
                System.out.println("lunch time arrived!");
                //do nothing -> lunch break 🤷‍♂️
            } else {
                System.out.println("Preparing to add slot...");
                boolean isBooked = appointmentRepository.existsByDoctorIdAndAppointmentDateTimeBetween(
                        doctorId,
                        selectedDate.atTime(time),
                        selectedDate.atTime(time.plusMinutes(slotDuration - 1))
                );
                String displayString = time.format(DateTimeFormatter.ofPattern("hh:mm a")) + " to " + time.plusMinutes(slotDuration).format(DateTimeFormatter.ofPattern("hh:mm a"));
                slotDtos.add(new SlotDto(time, isBooked, displayString));
                System.out.println("Added slot: " + displayString);
            }
        }
        System.out.println("Added all the slots...");
        System.out.println("Returning the slots.");

//        Dummy dates for testing
//        SlotDto slotDto1 = new SlotDto();
//        slotDto1.setTime(LocalTime.of(9,0));
//        slotDto1.setBooked(false);
//        slotDto1.setDisplayString("9.00 a.m. to 9.30 a.m.");
//        SlotDto slotDto2 = new SlotDto();
//        slotDto2.setTime(LocalTime.of(9,30));
//        slotDto2.setBooked(false);
//        slotDto2.setDisplayString("9.30 a.m. to 10.00 a.m.");
//        slotDtos.add(slotDto1);
//        slotDtos.add(slotDto2);

        return slotDtos;
    }

    @Override
    public Long getPatientIdByUsername(String username) {
        return patientRepository.findByMobile(username).get().getId();
    }
}
