package org.example.services.impl;

import jakarta.transaction.Transactional;
import org.example.dto.*;
import org.example.enums.Role;
import org.example.exception.IdNotFoundException;
import org.example.exception.UserAlreadyExistsException;
import org.example.exception.UserNotFoundException;
import org.example.exception.errorMessage.ErrorMessage;
import org.example.mappers.UserMapper;
import org.example.models.User;
import org.example.repositories.UserRepository;
import org.example.services.interfaces.CartService;
import org.example.services.interfaces.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CartService cartService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, CartService cartService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.cartService = cartService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserListDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserListDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> getUserById(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new IdNotFoundException(ErrorMessage.ID_NOT_FOUND);
        }
        return userOpt;
    }

    @Override
    public Optional<UserListDto> getUserDetailsById(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new IdNotFoundException(ErrorMessage.ID_NOT_FOUND);
        }
        return userOpt.map(userMapper::toUserListDto);
    }

    @Override
    @Transactional
    public UserAfterCreationDto createUser(UserCreateDto userCreateDto) {
        if (userRepository.existsByEmail(userCreateDto.getEmail())) {
            throw new UserAlreadyExistsException(ErrorMessage.USER_ALREADY_EXISTS);
        }
        User user = userMapper.toEntity(userCreateDto);
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User createdUser = userRepository.save(user);

        cartService.createCart(createdUser.getId());

        return userMapper.toUserAfterCreationDto(createdUser);
    }

    @Override
    public Optional<UserAfterUpdateDto> updateUser(Long id, UserUpdateDto userUpdateDto, User currentUser) {
        if (!id.equals(currentUser.getId()) && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new org.springframework.security.access.AccessDeniedException(ErrorMessage.ACCESS_DENIED);
        }

        return userRepository.findById(id).map(user -> {
            userMapper.updateEntityFromDto(userUpdateDto, user);

            if (userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
            }

            user.setUpdatedAt(LocalDateTime.now());
            User updatedUser = userRepository.save(user);

            return userMapper.toUserAfterUpdateDto(updatedUser);
        });
    }

    @Override
    @Transactional
    public void deleteUser(Long id, User currentUser) {
        if (!id.equals(currentUser.getId()) && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new org.springframework.security.access.AccessDeniedException(ErrorMessage.ACCESS_DENIED);
        }

        if (!existsById(id)) {
            throw new IdNotFoundException(ErrorMessage.ID_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    private boolean hasAdminRole(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorMessage.USER_NOT_FOUND));
        return user.getRole() == Role.ADMIN;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorMessage.USER_NOT_FOUND));
    }
}
