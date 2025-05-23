package com.medicaldata.medicSystem.controller.restful;

import com.medicaldata.medicSystem.dto.save.SaveDoctorDto;
import com.medicaldata.medicSystem.dto.DoctorDto;
import com.medicaldata.medicSystem.service.DoctorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@AllArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public List<DoctorDto> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/{id}")
    public DoctorDto getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }

    @PostMapping
    public ResponseEntity<DoctorDto> addDoctor(@Valid @RequestBody SaveDoctorDto saveDoctorDto) {
        DoctorDto savedDoctorDto = doctorService.addDoctor(saveDoctorDto);
        return new ResponseEntity<>(savedDoctorDto, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<DoctorDto>> addAllDoctors(@Valid @RequestBody List<SaveDoctorDto> saveDoctorDtoList) {
        List<DoctorDto> savedDoctorDtoList = doctorService.addAllDoctors(saveDoctorDtoList);
        return new ResponseEntity<>(savedDoctorDtoList, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public DoctorDto updateDoctor(@PathVariable Long id, @Valid @RequestBody SaveDoctorDto saveDoctorDto) {
        return doctorService.updateDoctor(id, saveDoctorDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctorById(@PathVariable Long id) {
        doctorService.deleteDoctorById(id);
        return ResponseEntity.ok().build();
    }
}