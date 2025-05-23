package com.medicaldata.medicSystem.repository;

import com.medicaldata.medicSystem.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> getAllBySpeciality(String specialty);

    @Query("SELECT DISTINCT d.speciality FROM Doctor d ORDER BY d.speciality ASC")
    List<String> getDistinctSpeciality();

    Optional<Doctor> findByMobile(String mobile);
}
