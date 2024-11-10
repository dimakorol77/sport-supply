package org.example.repositories;

import org.example.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Метод для поиска пользователя по ID
    Optional<User> findById(Long id);

    // Метод для проверки существования пользователя с указанным email
    boolean existsByEmail(String email);

    // JpaRepository уже предоставляет метод existsById
}
