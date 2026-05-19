package com.omnia.backend.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de entrada para el acceso de usuarios administradores.
 */
@Data
public class LoginRequestDTO {

    @NotBlank
    @Email
    @Pattern(
        regexp = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.(com|es|org|net|io|eu|edu|gov|co|app|dev|biz|info|tech|digital|cloud|online)$",
        message = "El correo debe tener un dominio válido (ej: usuario@empresa.com)"
    )
    @Size(max = 100)
    private String email;

    @NotBlank
    private String password;
}
