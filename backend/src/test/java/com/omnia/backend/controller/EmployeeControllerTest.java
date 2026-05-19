package com.omnia.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.omnia.backend.exception.BusinessException;
import com.omnia.backend.exception.GlobalExceptionHandler;
import com.omnia.backend.model.dto.EmployeeDTO;
import com.omnia.backend.service.EmployeeService;
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
 * Tests de integración del {@link EmployeeController} usando
 * {@code @WebMvcTest}.
 *
 * No carga el contexto completo: solo la capa MVC y los componentes
 * estrictamente necesarios para mapear request/response. El servicio
 * se sustituye por un mock.
 */
@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private ObjectMapper objectMapper;
    private EmployeeDTO validDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        validDto = new EmployeeDTO();
        validDto.setNif("12345678A");
        validDto.setFirstName("Susana");
        validDto.setLastName("Bitar");
        validDto.setSecondLastName("Azevedo");
        validDto.setBirthDate(LocalDate.of(1995, 5, 20));
        validDto.setPhone1("600111222");
        validDto.setPhone2("911222333");
        validDto.setEmail("susana@omnia.com");
        validDto.setHireDate(LocalDate.of(2024, 1, 15));
        validDto.setMaritalStatus("S");
        validDto.setHasUniversityEducation("S");
    }

    @Test
    @DisplayName("POST /employees con datos válidos: 201 y body con id")
    void createEmployee_valid_returns201() throws Exception {
        EmployeeDTO saved = new EmployeeDTO();
        saved.setIdEmployee(1);
        saved.setNif(validDto.getNif());
        when(employeeService.saveEmployee(any(EmployeeDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idEmployee").value(1));
    }

    @Test
    @DisplayName("POST /employees con NIF inválido: 400 y mensaje de validación")
    void createEmployee_invalidNif_returns400() throws Exception {
        validDto.setNif("XYZ");

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("validación")));
    }

    @Test
    @DisplayName("POST /employees cuando el servicio lanza BusinessException: 400 con mensaje de negocio")
    void createEmployee_businessException_returns400() throws Exception {
        when(employeeService.saveEmployee(any(EmployeeDTO.class)))
                .thenThrow(new BusinessException("Ya existe un empleado con el NIF indicado"));

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Ya existe un empleado con el NIF indicado"));
    }
}
