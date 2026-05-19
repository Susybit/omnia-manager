package com.omnia.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omnia.backend.model.dto.ProjectDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests de integración para la API de proyectos.
 * Asegura que el flujo de datos entre el cliente y el servidor de proyectos es íntegro.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Validación Bean: Rechaza un proyecto si faltan datos descriptivos")
    void createProject_MissingData_Returns400() throws Exception {
        ProjectDTO invalidDto = new ProjectDTO();
        invalidDto.setStartDate(LocalDate.now());
        // Faltan descripción, ubicación, etc.

        mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Excepción de Negocio: Valida que el servidor rechaza fechas incoherentes en el alta")
    void createProject_IncoherentDates_ReturnsBadRequest() throws Exception {
        ProjectDTO testDto = new ProjectDTO();
        testDto.setDescription("Proyecto Error");
        testDto.setStartDate(LocalDate.now());
        testDto.setEndDate(LocalDate.now().minusDays(30)); // Fecha fin anterior a inicio
        testDto.setLocation("Madrid");
        testDto.setObservations("Tests de validación.");

        mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validación: La fecha de fin debe ser posterior a la de inicio."));
    }
}
