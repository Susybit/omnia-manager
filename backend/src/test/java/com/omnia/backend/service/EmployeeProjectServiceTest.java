package com.omnia.backend.service;

import com.omnia.backend.model.dto.EmployeeAssignmentDTO;
import com.omnia.backend.model.entities.Employee;
import com.omnia.backend.model.entities.EmployeeProject;
import com.omnia.backend.model.entities.EmployeeProjectId;
import com.omnia.backend.model.entities.Project;
import com.omnia.backend.repository.EmployeeProjectRepository;
import com.omnia.backend.repository.EmployeeRepository;
import com.omnia.backend.repository.ProjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Suite de tests unitarios para validar la matriz de asignaciones.
 * Garantiza que la vinculación entre capital humano y proyectos sea coherente e idempotente.
 */
@ExtendWith(MockitoExtension.class)
class EmployeeProjectServiceTest {

    @Mock
    private EmployeeProjectRepository assignmentRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private EmployeeProjectService employeeProjectService;

    @Test
    @DisplayName("Crea una asignación nueva si no existe previamente")
    void assignEmployee_NewAssignment_Success() {
        Project p = new Project(); p.setIdProject(1);
        Employee e = new Employee(); e.setIdEmployee(100);

        when(projectRepository.findById(1)).thenReturn(Optional.of(p));
        when(employeeRepository.findById(100)).thenReturn(Optional.of(e));
        when(assignmentRepository.existsById(any(EmployeeProjectId.class))).thenReturn(false);

        employeeProjectService.assignEmployee(1, 100);

        verify(assignmentRepository).save(any(EmployeeProject.class));
    }

    @Test
    @DisplayName("Ignora la asignación si el vínculo ya existe (Idempotencia)")
    void assignEmployee_ExistingAssignment_DoesNothing() {
        Project p = new Project(); p.setIdProject(1);
        Employee e = new Employee(); e.setIdEmployee(100);

        when(projectRepository.findById(1)).thenReturn(Optional.of(p));
        when(employeeRepository.findById(100)).thenReturn(Optional.of(e));
        when(assignmentRepository.existsById(any(EmployeeProjectId.class))).thenReturn(true);

        employeeProjectService.assignEmployee(1, 100);

        verify(assignmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Elimina un vínculo existente correctamente")
    void unassignEmployee_Success() {
        EmployeeProjectId pk = new EmployeeProjectId(1, 100);
        EmployeeProject assignment = new EmployeeProject();
        
        when(assignmentRepository.findById(pk)).thenReturn(Optional.of(assignment));

        employeeProjectService.unassignEmployee(1, 100);

        verify(assignmentRepository).delete(assignment);
    }

    @Test
    @DisplayName("Genera correctamente la lista de estados de asignación para la matriz del frontend")
    void getAssignmentsForProject_Success() {
        Project p = new Project(); p.setIdProject(1);
        when(projectRepository.findById(1)).thenReturn(Optional.of(p));

        Employee e1 = new Employee(); e1.setIdEmployee(1); e1.setFirstName("Ana");
        Employee e2 = new Employee(); e2.setIdEmployee(2); e2.setFirstName("Juan");
        
        when(employeeRepository.findByTerminationDateIsNull()).thenReturn(List.of(e1, e2));
        
        // Simulamos que Ana ya está asignada al proyecto 1
        EmployeeProject ap = new EmployeeProject();
        ap.setEmployee(e1);
        when(assignmentRepository.findByProject_IdProject(1)).thenReturn(List.of(ap));

        List<EmployeeAssignmentDTO> result = employeeProjectService.getAssignmentsForProject(1);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getFirstName()).isEqualTo("Ana");
        assertThat(result.get(0).isAssigned()).isTrue();
        assertThat(result.get(1).getFirstName()).isEqualTo("Juan");
        assertThat(result.get(1).isAssigned()).isFalse();
    }
}
