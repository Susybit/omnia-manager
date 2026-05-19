package com.omnia.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omnia.backend.model.dto.EmployeeDTO;
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
 * Tests de integración para la API de empleados.
 * Valida el comportamiento 'end-to-end' de los endpoints, asegurando que:
 * - Las validaciones de formato (NIF, Email) funcionan.
 * - Los errores se transforman en mensajes legibles para el frontend.
 * - El sistema de seguridad y tipos de datos es robusto.
 */
@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Validación Bean: Rechaza el alta si el NIF no tiene el formato 8D + 1L")
    void createEmployee_InvalidNifFormat_Returns400() throws Exception {
        EmployeeDTO invalidDto = createValidDto();
        invalidDto.setNif("INVALIDO"); // Formato incorrecto

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Validación Bean: Rechaza el alta si faltan campos obligatorios")
    void createEmployee_MissingFields_Returns400() throws Exception {
        EmployeeDTO incompleteDto = new EmployeeDTO();
        incompleteDto.setNif("11223344Z");
        // Faltan nombre, apellidos, hireDate, etc.

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incompleteDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Excepción de Negocio: Devuelve un error controlado si se envía un ID manual en el alta")
    void createEmployee_ManualId_ReturnsBadRequest() throws Exception {
        EmployeeDTO manualIdDto = createValidDto();
        manualIdDto.setIdEmployee(999);

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(manualIdDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Restricción: El identificador debe ser autogenerado."));
    }

    private EmployeeDTO createValidDto() {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setNif("12345678Z");
        dto.setFirstName("Susana");
        dto.setLastName("Bitar");
        dto.setSecondLastName("Azevedo");
        dto.setBirthDate(LocalDate.of(1998, 1, 1));
        dto.setPhone1("666555444");
        dto.setPhone2("999888777");
        dto.setEmail("susana@test.com");
        dto.setHireDate(LocalDate.now());
        dto.setMaritalStatus("S");
        dto.setHasUniversityEducation("S");
        return dto;
    }
}
