package com.omnia.backend.exception;

/**
 * Excepción para errores de lógica de negocio.
 *
 * Se lanza cuando una regla del dominio no se cumple (datos inválidos,
 * recurso inactivo, intento de modificar un campo inmutable, etc.) y
 * se traduce a una respuesta HTTP 400 desde {@link GlobalExceptionHandler}.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
