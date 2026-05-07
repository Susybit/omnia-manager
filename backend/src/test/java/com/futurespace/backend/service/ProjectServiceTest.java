package com.futurespace.backend.service;

import com.futurespace.backend.exception.BusinessException;
import com.futurespace.backend.model.dto.ProjectDTO;
import com.futurespace.backend.model.entities.Project;
import com.futurespace.backend.repository.EmployeeProjectRepository;
import com.futurespace.backend.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Suite de tests unitarios para validar la lógica de negocio de proyectos.
 * Valida reglas críticas como:
 * - Coherencia temporal (fecha fin > fecha inicio).
 * - Inmutabilidad de la fecha de inicio en actualizaciones.
 * - Restricciones de borrado por integridad referencial.
 */
@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private EmployeeProjectRepository assignmentRepository;

    @InjectMocks
    private ProjectService projectService;

    private ProjectDTO testDto;

    @BeforeEach
    void setUp() {
        testDto = new ProjectDTO();
        testDto.setDescription("Sistema de Inteligencia 2026");
        testDto.setStartDate(LocalDate.now());
        testDto.setEndDate(LocalDate.now().plusMonths(6));
        testDto.setLocation("Madrid - Hub Central");
        testDto.setObservations("Proyecto estratégico de alta prioridad.");
    }

    @Test
    @DisplayName("Guarda un proyecto correctamente cuando las fechas son coherentes")
    void saveProject_Success() {
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> {
            Project p = i.getArgument(0);
            p.setIdProject(1);
            return p;
        });

        ProjectDTO saved = projectService.saveProject(testDto);

        assertThat(saved.getIdProject()).isEqualTo(1);
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    @DisplayName("Rechaza el alta si la fecha de fin es anterior a la de inicio")
    void saveProject_InvalidDates_ThrowsException() {
        testDto.setEndDate(testDto.getStartDate().minusDays(1));

        assertThatThrownBy(() -> projectService.saveProject(testDto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("fecha de fin debe ser posterior");

        verify(projectRepository, never()).save(any());
    }

    @Test
    @DisplayName("Bloquea la actualización si se intenta modificar la fecha de inicio (campo inmutable)")
    void updateProject_ChangeStartDate_Forbidden() {
        Project current = new Project();
        current.setIdProject(1);
        current.setStartDate(testDto.getStartDate().minusDays(10)); // Fecha original diferente

        when(projectRepository.findById(1)).thenReturn(Optional.of(current));

        assertThatThrownBy(() -> projectService.updateProject(1, testDto))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("inmutable");

        verify(projectRepository, never()).save(any());
    }

    @Test
    @DisplayName("Impide la eliminación (Lógica o Física) si el proyecto tiene personal asignado")
    void deactivateProject_WithResources_Forbidden() {
        Project current = new Project();
        current.setIdProject(1);
        current.setDescription("Proyecto Alpha");

        when(projectRepository.findById(1)).thenReturn(Optional.of(current));
        // Simulamos que existen asignaciones en este proyecto
        when(assignmentRepository.existsByProject_IdProject(1)).thenReturn(true);

        assertThatThrownBy(() -> projectService.deactivateProject(1))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("al menos un recurso");

        verify(projectRepository, never()).save(any());
    }

    @Test
    @DisplayName("Valida la baja física eliminando el registro si no hay dependencias")
    void deleteProject_Physical_Success() {
        Project current = new Project();
        current.setIdProject(1);
        when(projectRepository.findById(1)).thenReturn(Optional.of(current));
        when(assignmentRepository.existsByProject_IdProject(1)).thenReturn(false);

        projectService.deleteProject(1);

        verify(projectRepository).delete(current);
    }
}
