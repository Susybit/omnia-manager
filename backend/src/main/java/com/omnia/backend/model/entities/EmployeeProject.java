package com.omnia.backend.model.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entidad JPA mapeada a la tabla PR_EMPLEADOS_PROYECTO.
 *
 * Representa la asignación de un empleado a un proyecto. La fecha de
 * alta de la asignación se almacena en {@code assignmentDate} (F_ALTA).
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "PR_EMPLEADOS_PROYECTO")
public class EmployeeProject {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private EmployeeProjectId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idEmployee")
    @JoinColumn(name = "ID_EMPLEADO")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idProject")
    @JoinColumn(name = "ID_PROYECTO")
    private Project project;

    @Column(name = "F_ALTA", nullable = false)
    private LocalDate assignmentDate;
}
