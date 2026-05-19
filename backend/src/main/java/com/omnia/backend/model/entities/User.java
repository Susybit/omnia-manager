package com.omnia.backend.model.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entidad JPA mapeada a la tabla US_USUARIOS.
 *
 * Representa un usuario administrador del sistema. Es independiente de
 * los empleados de la compañía: un empleado no implica acceso a la
 * aplicación.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "US_USUARIOS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    @EqualsAndHashCode.Include
    private Integer idUser;

    @Column(name = "TX_EMAIL", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "TX_PASSWORD", nullable = false, length = 100)
    private String password;

    @Column(name = "TX_ROLE", nullable = false, length = 20)
    private String role;

    @Column(name = "F_ALTA", nullable = false)
    private LocalDate createdAt;

    @Column(name = "TX_NOMBRE", length = 100)
    private String name;
}
