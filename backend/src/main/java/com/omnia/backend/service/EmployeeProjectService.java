package com.omnia.backend.service;

import com.omnia.backend.exception.BusinessException;
import com.omnia.backend.model.dto.AssignmentDTO;
import com.omnia.backend.model.dto.EmployeeAssignmentDTO;
import com.omnia.backend.model.entities.Employee;
import com.omnia.backend.model.entities.EmployeeProject;
import com.omnia.backend.model.entities.EmployeeProjectId;
import com.omnia.backend.model.entities.Project;
import com.omnia.backend.repository.EmployeeProjectRepository;
import com.omnia.backend.repository.EmployeeRepository;
import com.omnia.backend.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de relaciones entre empleados y proyectos.
 * Implementa la persistencia de la matriz de asignaciones y validaciones de integridad referencial.
 */
@Service
public class EmployeeProjectService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeProjectService.class);

    private final EmployeeProjectRepository assignmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;

    public EmployeeProjectService(EmployeeProjectRepository assignmentRepository,
                                  EmployeeRepository employeeRepository,
                                  ProjectRepository projectRepository) {
        this.assignmentRepository = assignmentRepository;
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
    }

    /**
     * Recupera el histórico total de asignaciones en formato plano.
     * Utilizado para métricas globales y análisis de ocupación.
     */
    @Transactional(readOnly = true)
    public List<AssignmentDTO> getAllAssignments() {
        return assignmentRepository.findAllFlat();
    }

    /**
     * Recupera el estado de asignación de todos los empleados activos para un proyecto específico.
     * @param projectId ID del proyecto de referencia.
     * @return Lista de EmployeeAssignmentDTO con el estado de vinculación.
     */
    @Transactional(readOnly = true)
    public List<EmployeeAssignmentDTO> getAssignmentsForProject(Integer projectId) {
        ensureProjectActive(projectId);

        Set<Integer> assignedIds = assignmentRepository.findByProject_IdProject(projectId).stream()
                .map(a -> a.getEmployee().getIdEmployee())
                .collect(Collectors.toSet());

        return employeeRepository.findByTerminationDateIsNull().stream()
                .map(emp -> new EmployeeAssignmentDTO(
                        emp.getIdEmployee(),
                        emp.getFirstName(),
                        emp.getLastName(),
                        emp.getSecondLastName(),
                        emp.getNif(),
                        emp.getEmail(),
                        assignedIds.contains(emp.getIdEmployee())))
                .toList();
    }

    /**
     * Vincula un empleado a un proyecto. Operación idempotente.
     * @param projectId ID del proyecto.
     * @param employeeId ID del empleado.
     * @throws BusinessException Si el proyecto o empleado no existen o están inactivos.
     */
    @Transactional
    public void assignEmployee(Integer projectId, Integer employeeId) {
        Project project = ensureProjectActive(projectId);
        Employee employee = ensureEmployeeActive(employeeId);

        EmployeeProjectId pk = new EmployeeProjectId(projectId, employeeId);
        if (assignmentRepository.existsById(pk)) {
            return;
        }

        EmployeeProject assignment = new EmployeeProject();
        assignment.setId(pk);
        assignment.setProject(project);
        assignment.setEmployee(employee);
        assignment.setAssignmentDate(LocalDate.now());

        assignmentRepository.save(assignment);
        log.info("Vínculo creado: ProjectID={}, EmployeeID={}", projectId, employeeId);
    }

    /**
     * Elimina el vínculo entre un empleado y un proyecto. Operación idempotente.
     * @param projectId ID del proyecto.
     * @param employeeId ID del empleado.
     */
    @Transactional
    public void unassignEmployee(Integer projectId, Integer employeeId) {
        if (projectId == null || projectId <= 0 || employeeId == null || employeeId <= 0) {
            throw new BusinessException("Error: Identificadores de recurso no válidos.");
        }

        EmployeeProjectId pk = new EmployeeProjectId(projectId, employeeId);
        assignmentRepository.findById(pk).ifPresent(existing -> {
            assignmentRepository.delete(existing);
            log.info("Vínculo eliminado: ProjectID={}, EmployeeID={}", projectId, employeeId);
        });
    }

    private Project ensureProjectActive(Integer id) {
        if (id == null || id <= 0) {
            throw new BusinessException("Error: Identificador de proyecto no válido.");
        }
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Proyecto no encontrado."));
        if (project.getTerminationDate() != null) {
            throw new BusinessException("Restricción: Proyecto inactivo.");
        }
        return project;
    }

    private Employee ensureEmployeeActive(Integer id) {
        if (id == null || id <= 0) {
            throw new BusinessException("Error: Identificador de empleado no válido.");
        }
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Empleado no encontrado."));
        if (employee.getTerminationDate() != null) {
            throw new BusinessException("Restricción: Empleado inactivo.");
        }
        return employee;
    }
}
