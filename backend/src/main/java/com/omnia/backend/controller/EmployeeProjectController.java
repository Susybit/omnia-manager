package com.omnia.backend.controller;

import com.omnia.backend.model.dto.EmployeeAssignmentDTO;
import com.omnia.backend.service.EmployeeProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador para la gestión de asignaciones de recursos a proyectos.
 * Implementa la lógica de vinculación multirregistro descrita en los requisitos funcionales.
 */
@RestController
@RequestMapping("/projects/{projectId}/employees")
@Tag(name = "Gestión de Asignaciones")
public class EmployeeProjectController {

    private final EmployeeProjectService employeeProjectService;

    public EmployeeProjectController(EmployeeProjectService employeeProjectService) {
        this.employeeProjectService = employeeProjectService;
    }

    /**
     * Recupera el listado de empleados con su estado de asignación actual al proyecto.
     */
    @GetMapping
    @Operation(summary = "Estado de asignación de empleados por proyecto")
    public List<EmployeeAssignmentDTO> getAssignments(@PathVariable Integer projectId) {
        return employeeProjectService.getAssignmentsForProject(projectId);
    }

    /** Vincula un empleado a un proyecto específico. */
    @PutMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Asignación de empleado a proyecto")
    public void assignEmployee(@PathVariable Integer projectId,
                               @PathVariable Integer employeeId) {
        employeeProjectService.assignEmployee(projectId, employeeId);
    }

    /** Elimina la vinculación de un empleado con un proyecto específico. */
    @DeleteMapping("/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desvinculación de empleado de proyecto")
    public void unassignEmployee(@PathVariable Integer projectId,
                                 @PathVariable Integer employeeId) {
        employeeProjectService.unassignEmployee(projectId, employeeId);
    }
}
