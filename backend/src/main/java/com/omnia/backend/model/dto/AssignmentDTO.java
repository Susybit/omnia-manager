package com.omnia.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Vista plana de la tabla PR_EMPLEADOS_PROYECTO.
 *
 * Pensada para los cuadros de mando que necesitan reconstruir el
 * grafo proyecto-empleado en el cliente sin tener que recorrer las
 * asignaciones proyecto a proyecto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDTO {

    private Integer idProject;
    private Integer idEmployee;
    private LocalDate assignmentDate;
}
