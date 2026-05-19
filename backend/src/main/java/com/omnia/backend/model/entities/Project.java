package com.omnia.backend.model.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entidad JPA mapeada a la tabla PR_PROYECTOS.
 *
 * Cada instancia representa un proyecto de la compañía. La baja
 * se gestiona como borrado lógico con {@code terminationDate}
 * (columna F_BAJA): cuando es nulo el proyecto está vigente.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "PR_PROYECTOS")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROYECTO")
    @EqualsAndHashCode.Include
    private Integer idProject;

    @Column(name = "TX_DESCRIPCION", nullable = false, length = 125)
    private String description;

    @Column(name = "F_INICIO", nullable = false)
    private LocalDate startDate;

    @Column(name = "F_FIN")
    private LocalDate endDate;

    /** Fecha de baja lógica. Si es {@code null} el proyecto sigue activo. */
    @Column(name = "F_BAJA")
    private LocalDate terminationDate;

    @Column(name = "TX_LUGAR", nullable = false, length = 30)
    private String location;

    @Column(name = "TX_OBSERVACIONES", length = 300)
    private String observations;
}
