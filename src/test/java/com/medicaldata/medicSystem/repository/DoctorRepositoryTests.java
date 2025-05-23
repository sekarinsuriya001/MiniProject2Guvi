package com.medicaldata.medicSystem.repository;

import com.medicaldata.medicSystem.model.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DoctorRepositoryTests {
    @Autowired
    private DoctorRepository doctorRepository;
    private Doctor doctor;

    @BeforeEach
    public void setUp(){
        doctor = new Doctor(
                null,
                "John",
                "Doe",
                "Male",
                "7777777777",
                "johndoe@gmail.com",
                "General",
                5,
                "MBBS",
                "English",
                "USA"
        );
    }

    @DisplayName(value = "JUnit test for find all method when no data given")
    @Test
    public void givenNoDoctorList_whenFindAll_thenReturnEmptyList(){

        List<Doctor> doctors = doctorRepository.findAll();

        assertThat(doctors).isEmpty();
    }
    @DisplayName(value = "JUnit test for find all method")
    @Test
    public void givenDoctorList_whenFindAll_thenReturnDoctorList(){
        Doctor doctor1 = new Doctor(
                null,
                "Rebecca",
                "Johnson",
                "Female",
                "7777777778",
                "rjohn@gmail.com",
                "Orthopedics",
                5,
                "MBBS",
                "English",
                "USA"
        );

        doctorRepository.save(doctor);
        doctorRepository.save(doctor1);
        List<Doctor> doctors = doctorRepository.findAll();

        assertThat(doctors).isNotEmpty();
        assertThat(doctors.size()).isEqualTo(2);

        assertThat(doctors.get(0).getId()).isGreaterThan(0);
        assertThat(doctors.get(0)).isEqualTo(doctor);
    }

    @DisplayName(value = "JUnit test for find doctor by id method")
    @Test
    public void givenDoctor_whenFindById_thenReturnDoctor(){
        Long id = doctorRepository.save(doctor).getId();
        Optional<Doctor> fetchedDoctor = doctorRepository.findById(id);

        assertThat(fetchedDoctor).isNotEmpty();

        assertThat(fetchedDoctor.get().getId()).isGreaterThan(0);
        assertThat(fetchedDoctor.get()).isEqualTo(doctor);
    }

    @DisplayName(value = "JUnit test for find doctor by id method without a doctor")
    @Test
    public void givenNoDoctor_whenFindById_thenReturnEmptyOptional(){
        Optional<Doctor> fetchedDoctor = doctorRepository.findById(1L);

        assertThat(fetchedDoctor).isEmpty();
    }

    @DisplayName(value = "JUnit test method for save doctor method.")
    @Test
    public void givenDoctorObject_whenSave_thenReturnSavedDoctorObject(){
        Doctor savedDoctor  = doctorRepository.save(doctor);

        assertThat(savedDoctor).isNotNull();
        assertThat(savedDoctor.getId()).isGreaterThan(0);
        assertThat(savedDoctor).isEqualTo(doctor);
    }

    @DisplayName(value = "JUnit test method for save doctor method during updation.")
    @Test
    public void givenDoctorObject_whenUpdate_thenReturnUpdatedDoctorObject(){
        Long id  = doctorRepository.save(doctor).getId();
        Doctor savedDoctor = doctorRepository.findById(id).get();

        savedDoctor.setFirstName("Monica");
        savedDoctor.setLastName("Bell");
        savedDoctor.setSpeciality("Pediatrics");

        Doctor updatedDoctor  = doctorRepository.save(savedDoctor);

        assertThat(updatedDoctor).isNotNull();
        assertThat(updatedDoctor.getId()).isGreaterThan(0);

        assertThat(updatedDoctor.getId()).isEqualTo(savedDoctor.getId());
        assertThat(updatedDoctor.getFirstName()).isEqualTo(savedDoctor.getFirstName());
        assertThat(updatedDoctor.getLastName()).isEqualTo(savedDoctor.getLastName());
        assertThat(updatedDoctor.getSpeciality()).isEqualTo(savedDoctor.getSpeciality());
    }

    @DisplayName(value = "JUnit test method for exists by id method.")
    @Test
    public void givenDoctorObject_whenExists_thenReturnTrue(){
        Long id  = doctorRepository.save(doctor).getId();
        Boolean doctorSaved = doctorRepository.existsById(id);

        assertThat(doctorSaved).isTrue();
    }

    @DisplayName(value = "JUnit test method for exists by id method without any object.")
    @Test
    public void givenNoObject_whenExists_thenReturnFalse(){
        Boolean doctorSaved = doctorRepository.existsById(1L);

        assertThat(doctorSaved).isFalse();
    }



    @DisplayName(value = "JUnit test method for delete by id method.")
    @Test
    public void givenDoctorObject_whenDelete_thenRemoveObject(){
        Long id  = doctorRepository.save(doctor).getId();

        doctorRepository.deleteById(id);

        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
        assertThat(optionalDoctor).isEmpty();
    }

    @DisplayName("JUnit test method for getAllBySpeciality method")
    @Test
    public void givenSpeciality_whenGetAllBySpeciality_thenReturnListOfDoctors() {
        // Given
        Doctor doctor1 = new Doctor(
                null,
                "Alice",
                "Smith",
                "Female",
                "8888888888",
                "alice@example.com",
                "Cardiology",
                10,
                "MBBS",
                "English",
                "USA"
        );
        Doctor doctor2 = new Doctor(
                null,
                "Bob",
                "Brown",
                "Male",
                "9999999999",
                "bob@example.com",
                "Cardiology",
                8,
                "MBBS",
                "English",
                "USA"
        );

        doctorRepository.save(doctor);  // General
        doctorRepository.save(doctor1); // Cardiology
        doctorRepository.save(doctor2); // Cardiology

        // When
        List<Doctor> cardiologists = doctorRepository.getAllBySpeciality("Cardiology");

        // Then
        assertThat(cardiologists).isNotEmpty();
        assertThat(cardiologists.size()).isEqualTo(2);
        assertThat(cardiologists).containsExactlyInAnyOrder(doctor1, doctor2);
    }

    @DisplayName("JUnit test method for getDistinctSpeciality method")
    @Test
    public void whenGetDistinctSpeciality_thenReturnDistinctSpecialties() {
        // Given
        Doctor doctor1 = new Doctor(
                null,
                "Alice",
                "Smith",
                "Female",
                "8888888888",
                "alice@example.com",
                "Cardiology",
                10,
                "MBBS",
                "English",
                "USA"
        );
        Doctor doctor2 = new Doctor(
                null,
                "Bob",
                "Brown",
                "Male",
                "9999999999",
                "bob@example.com",
                "General",
                8,
                "MBBS",
                "English",
                "USA"
        );

        doctorRepository.save(doctor);  // General
        doctorRepository.save(doctor1); // Cardiology
        doctorRepository.save(doctor2); // General

        // When
        List<String> distinctSpecialities = doctorRepository.getDistinctSpeciality();

        // Then
        assertThat(distinctSpecialities).isNotEmpty();
        assertThat(distinctSpecialities.size()).isEqualTo(2);
        assertThat(distinctSpecialities).containsExactlyInAnyOrder("Cardiology", "General");
    }

    @DisplayName("JUnit test method for findByMobile method")
    @Test
    public void givenMobile_whenFindByMobile_thenReturnDoctor() {
        // Given
        doctorRepository.save(doctor);

        // When
        Optional<Doctor> fetchedDoctor = doctorRepository.findByMobile("7777777777");

        // Then
        assertThat(fetchedDoctor).isPresent();
        assertThat(fetchedDoctor.get()).isEqualTo(doctor);
    }

    @DisplayName("JUnit test method for findByMobile method when no doctor found")
    @Test
    public void givenInvalidMobile_whenFindByMobile_thenReturnEmptyOptional() {
        // Given nothing

        // When
        Optional<Doctor> fetchedDoctor = doctorRepository.findByMobile("7777777777");

        // Then
        assertThat(fetchedDoctor).isEmpty();
    }


}
