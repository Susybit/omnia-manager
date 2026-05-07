package com.futurespace.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.futurespace.backend.exception.GlobalExceptionHandler;
import com.futurespace.backend.model.dto.AuthResponseDTO;
import com.futurespace.backend.model.dto.LoginRequestDTO;
import com.futurespace.backend.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de integración para el controlador de Autenticación.
 * Valida la exposición de los endpoints REST y la correcta gestión de tipos MIME.
 */
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /auth/login - Debería retornar 200 ante credenciales válidas")
    void login_ShouldReturnOk() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("admin@futurespace.com");
        request.setPassword("12345678S*");

        AuthResponseDTO response = new AuthResponseDTO();
        response.setEmail("admin@futurespace.com");
        response.setMessage("Login correcto");

        when(authService.login(any(LoginRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@futurespace.com"))
                .andExpect(jsonPath("$.message").value("Login correcto"));
    }

    @Test
    @DisplayName("POST /auth/login - Debería retornar 400 ante cuerpo de petición inválido")
    void login_InvalidRequest_ShouldReturnBadRequest() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("email-invalido"); // La validación @Email debería fallar si se aplicara aquí

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
