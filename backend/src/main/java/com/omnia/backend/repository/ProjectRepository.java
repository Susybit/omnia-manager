package com.omnia.backend.repository;

import com.omnia.backend.model.entities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio JPA para la entidad {@link Project}.
 */
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    /** Devuelve los proyectos activos sin paginar (uso interno). */
    List<Project> findByTerminationDateIsNull();

    /** Devuelve los proyectos activos paginados (uso público: listado principal). */
    Page<Project> findByTerminationDateIsNull(Pageable pageable);
}
