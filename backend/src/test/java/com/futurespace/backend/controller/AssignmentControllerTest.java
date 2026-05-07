package com.futurespace.backend.controller;

import com.futurespace.backend.exception.GlobalExceptionHandler;
import com.futurespace.backend.model.dto.AssignmentDTO;
import com.futurespace.backend.service.EmployeeProjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Pruebas para la visualización global de asignaciones.
 */
@WebMvcTest(AssignmentController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class AssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeProjectService employeeProjectService;

    @Test
    @DisplayName("GET /assignments - Debería retornar la lista global de asignaciones")
    void getAllAssignments_ShouldReturnList() throws Exception {
        AssignmentDTO dto = new AssignmentDTO();
        dto.setIdProject(1);
        dto.setIdEmployee(1);

        when(employeeProjectService.getAllAssignments()).thenReturn(List.of(dto));

        mockMvc.perform(get("/assignments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idProject").value(1))
                .andExpect(jsonPath("$[0].idEmployee").value(1));
    }
}
