package com.omnia.backend.repository;

import com.omnia.backend.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link User}.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /** Busca un usuario por email. Útil para validar credenciales. */
    Optional<User> findByEmail(String email);
}
