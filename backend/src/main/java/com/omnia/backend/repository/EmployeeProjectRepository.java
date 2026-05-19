package com.omnia.backend.repository;

import com.omnia.backend.model.entities.EmployeeProject;
import com.omnia.backend.model.entities.EmployeeProjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Repositorio JPA para las asignaciones {@link EmployeeProject}.
 *
 * Los métodos derivados del nombre con guion bajo navegan a las
 * entidades relacionadas (por ejemplo {@code Employee_IdEmployee}
 * accede al campo {@code idEmployee} de la entidad {@code Employee}).
 */
public interface EmployeeProjectRepository extends JpaRepository<EmployeeProject, EmployeeProjectId> {

    boolean existsByEmployee_IdEmployee(Integer idEmployee);

    boolean existsByProject_IdProject(Integer idProject);

    List<EmployeeProject> findByProject_IdProject(Integer idProject);

    /** Devuelve las descripciones de los proyectos a los que está asignado un empleado. */
    @Query("SELECT a.project.description FROM EmployeeProject a WHERE a.employee.idEmployee = :idEmployee")
    Set<String> findProjectDescriptionsByEmployeeId(@Param("idEmployee") Integer idEmployee);

    /**
     * Devuelve todas las asignaciones como tuplas planas (idProject, idEmployee, F_ALTA).
     * Pensado para vistas analíticas que necesitan el grafo completo en una sola consulta.
     */
    @Query("SELECT new com.omnia.backend.model.dto.AssignmentDTO(" +
            "a.project.idProject, a.employee.idEmployee, a.assignmentDate) " +
            "FROM EmployeeProject a")
    List<com.omnia.backend.model.dto.AssignmentDTO> findAllFlat();
}
