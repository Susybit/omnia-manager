package com.omnia.backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO para actualizar el nombre de visualización del administrador.
 */
@Data
public class UserProfileDTO {

    @NotBlank(message = "El email es obligatorio")
    private String email;

    private String name;
}
