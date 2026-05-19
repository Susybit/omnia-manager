package com.omnia.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para la pantalla "Asignar empleados a proyectos".
 *
 * Representa a un empleado activo junto con la información de si
 * está asignado o no al proyecto que se está consultando.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeAssignmentDTO {

    private Integer idEmployee;
    private String firstName;
    private String lastName;
    private String secondLastName;
    private String nif;
    private String email;

    /** Indica si el empleado está actualmente asignado al proyecto consultado. */
    private boolean assigned;
}
