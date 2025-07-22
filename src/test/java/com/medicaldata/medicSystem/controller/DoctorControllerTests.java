package com.medicaldata.medicSystem.controller;

import com.medicaldata.medicSystem.controller.restful.DoctorController;
import com.medicaldata.medicSystem.dto.DoctorDto;
import com.medicaldata.medicSystem.dto.save.SaveDoctorDto;
import com.medicaldata.medicSystem.service.DoctorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
public class DoctorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    @Autowired
    private ObjectMapper objectMapper;

    private DoctorDto doctorDto;
    private SaveDoctorDto saveDoctorDto;

    @BeforeEach
    public void setup() {
        doctorDto = new DoctorDto(
                1L,
                "John",
                "Smith",
                "Male",
                "9988776655",
                "jsmith@gmail.com",
                "General",
                5,
                "MBBS",
                "English",
                "USA"
        );
        saveDoctorDto = new SaveDoctorDto(
                "John",
                "Smith",
                "Male",
                "9988776655",
                "jsmith@gmail.com",
                "General",
                5,
                "MBBS",
                "English",
                "USA"
        );
    }

    @WithMockUser(roles = {"PATIENT"})
    @DisplayName("JUnit test for getAllDoctors method")
    @Test
    public void givenDoctors_whenGetAllDoctors_thenReturnDoctorList() throws Exception {
        given(doctorService.getAllDoctors()).willReturn(List.of(doctorDto));

        ResultActions resultActions = mockMvc.perform(get("/doctors"));

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(doctorDto))))
                .andExpect(jsonPath("$.size()", Matchers.is(1)));
    }

    @WithMockUser(roles = {"PATIENT"})
    @DisplayName("JUnit test for getDoctorById method")
    @Test
    public void givenDoctorId_whenGetDoctorById_thenReturnDoctor() throws Exception {
        given(doctorService.getDoctorById(1L)).willReturn(doctorDto);

        ResultActions resultActions = mockMvc.perform(get("/doctors/1"));

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(doctorDto)));
    }

    @WithMockUser(roles = {"PATIENT"})
    @DisplayName("JUnit test for addDoctor method")
    @Test
    public void givenDoctor_whenAddDoctor_thenReturnSavedDoctor() throws Exception {
        given(doctorService.addDoctor(any(SaveDoctorDto.class))).willReturn(doctorDto);

        ResultActions resultActions = mockMvc.perform(
                post("/doctors")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveDoctorDto))
        );

        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(doctorDto)));
    }

    @WithMockUser(roles = {"PATIENT"})
    @DisplayName("JUnit test for addAllDoctors method")
    @Test
    public void givenDoctors_whenAddAllDoctors_thenReturnSavedDoctors() throws Exception {
        List<SaveDoctorDto> saveDoctorDtoList = List.of(saveDoctorDto);
        List<DoctorDto> savedDoctorDtoList = List.of(doctorDto);

        given(doctorService.addAllDoctors(saveDoctorDtoList)).willReturn(savedDoctorDtoList);

        ResultActions resultActions = mockMvc.perform(
                post("/doctors/batch")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveDoctorDtoList))
        );

        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(savedDoctorDtoList)))
                .andExpect(jsonPath("$.size()", Matchers.is(1)));
    }

    @WithMockUser(roles = {"PATIENT"})
    @DisplayName("JUnit test for updateDoctor method")
    @Test
    public void givenDoctor_whenUpdateDoctor_thenReturnUpdatedDoctor() throws Exception {
        given(doctorService.updateDoctor(eq(1L), any(SaveDoctorDto.class))).willReturn(doctorDto);

        ResultActions resultActions = mockMvc.perform(
                put("/doctors/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveDoctorDto))
        );

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(doctorDto)));
    }

    @WithMockUser(roles = {"PATIENT"})
    @DisplayName("JUnit test for deleteDoctorById method")
    @Test
    public void givenDoctorId_whenDeleteDoctorById_thenReturnSuccessMessage() throws Exception {
        willDoNothing().given(doctorService).deleteDoctorById(1L);

        ResultActions resultActions = mockMvc.perform(
                delete("/doctors/1").with(csrf())
        );

        resultActions.andDo(print())
                .andExpect(status().isOk());
    }
}