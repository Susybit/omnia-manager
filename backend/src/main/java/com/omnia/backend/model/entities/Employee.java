package com.omnia.backend.model.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entidad JPA mapeada a la tabla EM_EMPLEADOS.
 *
 * Cada instancia representa a un empleado de la compañía.
 * La baja se gestiona como borrado lógico mediante {@code terminationDate}
 * (columna F_BAJA): cuando es nulo el empleado está activo.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "EM_EMPLEADOS")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EMPLEADO")
    @EqualsAndHashCode.Include
    private Integer idEmployee;

    @Column(name = "TX_NIF", nullable = false, unique = true, length = 9)
    private String nif;

    @Column(name = "TX_NOMBRE", nullable = false, length = 30)
    private String firstName;

    @Column(name = "TX_APELLIDO1", nullable = false, length = 40)
    private String lastName;

    @Column(name = "TX_APELLIDO2", nullable = false, length = 40)
    private String secondLastName;

    @Column(name = "F_NACIMIENTO", nullable = false)
    private LocalDate birthDate;

    @Column(name = "N_TELEFONO1", nullable = false, length = 12)
    private String phone1;

    @Column(name = "N_TELEFONO2", nullable = false, length = 12)
    private String phone2;

    @Column(name = "TX_EMAIL", nullable = false, length = 40)
    private String email;

    @Column(name = "F_ALTA", nullable = false)
    private LocalDate hireDate;

    /** Fecha de baja lógica. Si es {@code null} el empleado sigue activo. */
    @Column(name = "F_BAJA")
    private LocalDate terminationDate;

    /** Estado civil: {@code S} soltero, {@code C} casado. */
    @Column(name = "CX_EDOCIVIL", nullable = false, length = 1)
    private String maritalStatus;

    /** Formación universitaria: {@code S} sí, {@code N} no. */
    @Column(name = "B_FORMACIONU", nullable = false, length = 1)
    private String hasUniversityEducation;
}
