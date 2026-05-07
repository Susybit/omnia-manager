package com.futurespace.backend.service;

import com.futurespace.backend.exception.BusinessException;
import com.futurespace.backend.model.dto.AuthResponseDTO;
import com.futurespace.backend.model.dto.LoginRequestDTO;
import com.futurespace.backend.model.dto.RegisterRequestDTO;
import com.futurespace.backend.model.dto.ResetPasswordRequestDTO;
import com.futurespace.backend.model.entities.User;
import com.futurespace.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Suite de tests unitarios para el servicio de Autenticación.
 * Valida el flujo de acceso, registro y seguridad de usuarios administradores.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private LoginRequestDTO loginRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setIdUser(1);
        testUser.setEmail("admin@futurespace.com");
        testUser.setPassword("hashedPassword");
        testUser.setRole("ADMIN");

        loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("admin@futurespace.com");
        loginRequest.setPassword("12345678S*");
    }

    @Test
    @DisplayName("Login exitoso con credenciales válidas")
    void login_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        AuthResponseDTO response = authService.login(loginRequest);

        assertThat(response.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(response.getMessage()).isEqualTo("Login correcto");
        verify(userRepository).findByEmail(anyString());
    }

    @Test
    @DisplayName("Error de login cuando el usuario no existe")
    void login_UserNotFound_ThrowsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("credenciales incorrectas");
    }

    @Test
    @DisplayName("Error de login cuando la contraseña no coincide")
    void login_WrongPassword_ThrowsException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("credenciales incorrectas");
    }

    @Test
    @DisplayName("Registro exitoso de un nuevo administrador")
    void register_Success() {
        RegisterRequestDTO registerDto = new RegisterRequestDTO();
        registerDto.setEmail("new@futurespace.com");
        registerDto.setPassword("Password123*");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("newHashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        AuthResponseDTO response = authService.register(registerDto);

        assertThat(response).isNotNull();
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Reset de contraseña exitoso validando clave actual")
    void resetPassword_Success() {
        ResetPasswordRequestDTO resetDto = new ResetPasswordRequestDTO();
        resetDto.setEmail("admin@futurespace.com");
        resetDto.setCurrentPassword("12345678S*");
        resetDto.setNewPassword("NewPassword123*");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("newHashedPassword");

        AuthResponseDTO response = authService.resetPassword(resetDto);

        assertThat(response.getMessage()).isEqualTo("Contraseña actualizada correctamente");
        verify(userRepository).save(testUser);
    }
}
