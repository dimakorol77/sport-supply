package org.example.services;

import org.example.models.User;
import org.example.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // найти всех пользователей
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


}
