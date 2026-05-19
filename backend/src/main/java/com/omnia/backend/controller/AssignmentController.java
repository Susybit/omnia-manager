package com.omnia.backend.controller;

import com.omnia.backend.model.dto.AssignmentDTO;
import com.omnia.backend.service.EmployeeProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador para la visualización global de relaciones empleado-proyecto.
 * Proporciona una vista plana de todas las asignaciones para análisis y métricas.
 */
@RestController
@RequestMapping("/assignments")
@Tag(name = "Asignaciones (Vista Global)")
public class AssignmentController {

    private final EmployeeProjectService employeeProjectService;

    public AssignmentController(EmployeeProjectService employeeProjectService) {
        this.employeeProjectService = employeeProjectService;
    }

    /** Lista todas las asignaciones existentes como tuplas planas. */
    @GetMapping
    @Operation(summary = "Lista todas las asignaciones empleado-proyecto")
    public List<AssignmentDTO> getAllAssignments() {
        return employeeProjectService.getAllAssignments();
    }
}
