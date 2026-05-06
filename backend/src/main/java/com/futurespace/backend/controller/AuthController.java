package com.futurespace.backend.controller;

import com.futurespace.backend.model.dto.AuthResponseDTO;
import com.futurespace.backend.model.dto.LoginRequestDTO;
import com.futurespace.backend.model.dto.RegisterRequestDTO;
import com.futurespace.backend.model.dto.ResetPasswordRequestDTO;
import com.futurespace.backend.model.dto.UserProfileDTO;
import com.futurespace.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API REST para la gestión de acceso y seguridad de administradores.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Endpoints para login, registro y reseteo de claves")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /** Valida las credenciales de un usuario administrador. */
    @PostMapping("/login")
    @Operation(summary = "Autentica un usuario administrador")
    public AuthResponseDTO login(@Valid @RequestBody LoginRequestDTO dto) {
        return authService.login(dto);
    }

    /** Registra un nuevo usuario administrador (solo dominio @futurespace.com). */
    @PostMapping("/register")
    @Operation(summary = "Registra un nuevo usuario administrador")
    public AuthResponseDTO register(@Valid @RequestBody RegisterRequestDTO dto) {
        return authService.register(dto);
    }

    /** Resetea la contraseña de un administrador existente. */
    @PostMapping("/reset-password")
    @Operation(summary = "Resetea la contraseña de un administrador")
    public AuthResponseDTO resetPassword(@Valid @RequestBody ResetPasswordRequestDTO dto) {
        return authService.resetPassword(dto);
    }

    /** Actualiza el nombre de visualización del administrador. */
    @PutMapping("/profile")
    @Operation(summary = "Actualiza el nombre de perfil del administrador")
    public AuthResponseDTO updateProfile(@Valid @RequestBody UserProfileDTO dto) {
        return authService.updateProfile(dto.getEmail(), dto.getName());
    }
}
