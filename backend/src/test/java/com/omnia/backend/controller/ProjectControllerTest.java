package com.omnia.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.omnia.backend.exception.GlobalExceptionHandler;
import com.omnia.backend.model.dto.ProjectDTO;
import com.omnia.backend.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests de integración del {@link ProjectController} usando
 * {@code @WebMvcTest}.
 */
@WebMvcTest(ProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    private ObjectMapper objectMapper;
    private ProjectDTO validDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        validDto = new ProjectDTO();
        validDto.setDescription("Migración de plataforma");
        validDto.setStartDate(LocalDate.of(2024, 3, 1));
        validDto.setEndDate(LocalDate.of(2024, 12, 31));
        validDto.setLocation("Madrid");
        validDto.setObservations("Proyecto crítico para Q4");
    }

    @Test
    @DisplayName("POST /projects con datos válidos: 201 y body con id")
    void createProject_valid_returns201() throws Exception {
        ProjectDTO saved = new ProjectDTO();
        saved.setIdProject(10);
        saved.setDescription(validDto.getDescription());
        when(projectService.saveProject(any(ProjectDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idProject").value(10));
    }

    @Test
    @DisplayName("POST /projects sin description: 400 con mensaje de validación")
    void createProject_missingDescription_returns400() throws Exception {
        validDto.setDescription(null);

        mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("validación")));
    }
}
