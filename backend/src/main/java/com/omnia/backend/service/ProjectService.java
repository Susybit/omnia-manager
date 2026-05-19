package com.omnia.backend.service;

import com.omnia.backend.exception.BusinessException;
import com.omnia.backend.model.dto.ProjectDTO;
import com.omnia.backend.model.entities.Project;
import com.omnia.backend.repository.EmployeeProjectRepository;
import com.omnia.backend.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Servicio para la gestión de proyectos corporativos.
 * Implementa la lógica de negocio, validación de ciclos de vida y persistencia.
 */
@Service
public class ProjectService {

    private static final Logger log = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectRepository projectRepository;
    private final EmployeeProjectRepository assignmentRepository;

    public ProjectService(ProjectRepository projectRepository,
                          EmployeeProjectRepository assignmentRepository) {
        this.projectRepository = projectRepository;
        this.assignmentRepository = assignmentRepository;
    }

    /**
     * Recupera proyectos activos con soporte para paginación.
     */
    @Transactional(readOnly = true)
    public Page<ProjectDTO> getActiveProjects(Pageable pageable) {
        return projectRepository.findByTerminationDateIsNull(pageable).map(this::toDTO);
    }

    /**
     * Recupera todos los proyectos (activos e inactivos) con soporte para paginación.
     */
    @Transactional(readOnly = true)
    public Page<ProjectDTO> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable).map(this::toDTO);
    }

    /**
     * Recupera un proyecto por su identificador único.
     * @param id ID del proyecto.
     * @return ProjectDTO correspondiente.
     * @throws BusinessException Si el proyecto no existe o no está activo.
     */
    @Transactional(readOnly = true)
    public ProjectDTO getProjectById(Integer id) {
        return toDTO(fetchActiveProject(id));
    }

    /**
     * Registra un nuevo proyecto validando la coherencia de fechas.
     * @param dto Datos del proyecto.
     * @return DTO del proyecto persistido.
     * @throws BusinessException Si hay errores en las fechas o identificadores.
     */
    @Transactional
    public ProjectDTO saveProject(ProjectDTO dto) {
        if (dto.getIdProject() != null) {
            throw new BusinessException("Restricción: El identificador debe ser autogenerado.");
        }

        Project entity = toEntity(dto);
        validateProjectDates(entity);

        Project saved = projectRepository.save(entity);
        log.info("Nuevo proyecto registrado: ID={}, Desc=\"{}\"", saved.getIdProject(), saved.getDescription());
        return toDTO(saved);
    }

    /**
     * Actualiza un proyecto activo validando campos inmutables (Fecha Inicio).
     * @param id ID del proyecto a actualizar.
     * @param dto Nuevos datos.
     * @return DTO actualizado.
     */
    @Transactional
    public ProjectDTO updateProject(Integer id, ProjectDTO dto) {
        validateUpdateInput(id, dto);
        Project current = fetchActiveProject(id);
        validateImmutableFields(id, dto, current);

        applyUpdatableFields(dto, current);
        validateProjectDates(current);

        Project saved = projectRepository.save(current);
        log.info("Proyecto actualizado: ID={}", saved.getIdProject());
        return toDTO(saved);
    }

    /**
     * Ejecuta la baja lógica del proyecto tras verificar la ausencia de recursos asignados.
     * @param id ID del proyecto a desactivar.
     * @throws BusinessException Si el proyecto tiene empleados vinculados.
     */
    @Transactional
    public void deactivateProject(Integer id) {
        Project project = fetchActiveProject(id);

        if (assignmentRepository.existsByProject_IdProject(id)) {
            log.warn("Baja denegada: Proyecto ID={} con recursos vinculados.", id);
            throw new BusinessException(
                    "No se puede dar de baja el proyecto " + project.getDescription() +
                    " porque tiene asignado al menos un recurso");
        }

        project.setTerminationDate(LocalDate.now());
        projectRepository.save(project);
        log.info("Baja lógica procesada: ID={}", id);
    }

    /**
     * Realiza la eliminación física del proyecto en la base de datos.
     * @param id ID del proyecto a eliminar.
     */
    @Transactional
    public void deleteProject(Integer id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Recurso no encontrado."));

        if (assignmentRepository.existsByProject_IdProject(id)) {
            throw new BusinessException("Restricción: No se puede eliminar físicamente un proyecto con recursos vinculados.");
        }

        projectRepository.delete(project);
        log.info("Eliminación física procesada: ID={}", id);
    }

    // -----------------------------------------------------------------
    // Métodos auxiliares de soporte
    // -----------------------------------------------------------------

    private Project fetchActiveProject(Integer id) {
        if (id == null || id <= 0) {
            throw new BusinessException("Error: Identificador de recurso no válido.");
        }
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Recurso no encontrado."));
        if (project.getTerminationDate() != null) {
            throw new BusinessException("Recurso inactivo.");
        }
        return project;
    }

    private void validateUpdateInput(Integer id, ProjectDTO dto) {
        if (id == null || id <= 0 || dto == null) {
            throw new BusinessException("Error: Entrada de datos no válida para actualización.");
        }
    }

    private void validateImmutableFields(Integer id, ProjectDTO dto, Project current) {
        if (dto.getIdProject() != null && !Objects.equals(id, dto.getIdProject())) {
            throw new BusinessException("Error: Discrepancia en identificadores de recurso.");
        }
        if (!Objects.equals(current.getStartDate(), dto.getStartDate())) {
            throw new BusinessException("Restricción: La fecha de inicio es inmutable.");
        }
    }

    private void applyUpdatableFields(ProjectDTO dto, Project entity) {
        entity.setDescription(dto.getDescription());
        entity.setEndDate(dto.getEndDate());
        entity.setLocation(dto.getLocation());
        entity.setObservations(dto.getObservations());
    }

    private void validateProjectDates(Project p) {
        if (p.getStartDate() != null && p.getEndDate() != null
                && p.getEndDate().isBefore(p.getStartDate())) {
            throw new BusinessException("Validación: La fecha de fin debe ser posterior a la de inicio.");
        }
    }

    private ProjectDTO toDTO(Project p) {
        ProjectDTO dto = new ProjectDTO();
        dto.setIdProject(p.getIdProject());
        dto.setDescription(p.getDescription());
        dto.setStartDate(p.getStartDate());
        dto.setEndDate(p.getEndDate());
        dto.setTerminationDate(p.getTerminationDate());
        dto.setLocation(p.getLocation());
        dto.setObservations(p.getObservations());
        return dto;
    }

    private Project toEntity(ProjectDTO dto) {
        Project p = new Project();
        p.setDescription(dto.getDescription());
        p.setStartDate(dto.getStartDate());
        p.setEndDate(dto.getEndDate());
        p.setLocation(dto.getLocation());
        p.setObservations(dto.getObservations());
        return p;
    }
}
