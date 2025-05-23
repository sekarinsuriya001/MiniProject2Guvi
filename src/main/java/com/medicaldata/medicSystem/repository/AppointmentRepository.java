package com.medicaldata.medicSystem.repository;

import com.medicaldata.medicSystem.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByPatientId(Long patientId);
    int deleteAllByPatientId(Long patientId);
    boolean existsByDoctorId(Long doctorId);
    int deleteAllByDoctorId(Long doctorId);
    List<Appointment> getAllByPatientId(Long patientId);
    boolean existsByDoctorIdAndAppointmentDateTimeBetween(Long doctorId, LocalDateTime startTime, LocalDateTime endTime);
}
