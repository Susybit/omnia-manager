package com.omnia.backend.repository;

import com.omnia.backend.model.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Employee}.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    /** Devuelve los empleados activos sin paginar (uso interno: pantalla de asignación). */
    List<Employee> findByTerminationDateIsNull();

    /** Devuelve los empleados activos paginados (uso público: listado principal). */
    Page<Employee> findByTerminationDateIsNull(Pageable pageable);

    /** Busca un empleado por NIF. Útil para validar duplicados antes del alta. */
    Optional<Employee> findByNif(String nif);
}
