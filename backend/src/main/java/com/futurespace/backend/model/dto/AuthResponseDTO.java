package com.futurespace.backend.model.dto;

import lombok.Data;

/**
 * DTO de salida para las operaciones de autenticación.
 */
@Data
public class AuthResponseDTO {

    private Integer idUser;
    private String email;
    private String role;
    private String name;
    private String message;
}
