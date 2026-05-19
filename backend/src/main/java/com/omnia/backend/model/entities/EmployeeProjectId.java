package com.omnia.backend.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Clave compuesta de la tabla PR_EMPLEADOS_PROYECTO.
 *
 * Une el identificador del proyecto y el del empleado para
 * formar la clave primaria de la asignación.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EmployeeProjectId implements Serializable {

    @Column(name = "ID_PROYECTO")
    private Integer idProject;

    @Column(name = "ID_EMPLEADO")
    private Integer idEmployee;
}
