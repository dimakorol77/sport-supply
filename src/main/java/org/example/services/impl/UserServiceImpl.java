package org.example.services.impl;

import jakarta.transaction.Transactional;
import org.example.dto.*;
import org.example.enums.Role;
import org.example.exceptions.IdNotFoundException;
import org.example.exceptions.UserAlreadyExistsException;

import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.UserMapper;
import org.example.models.User;
import org.example.repositories.UserRepository;
import org.example.security.SecurityUtils;
import org.example.services.interfaces.CartService;
import org.example.services.interfaces.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CartService cartService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, CartService cartService, PasswordEncoder passwordEncoder,  SecurityUtils securityUtils) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.cartService = cartService;
        this.passwordEncoder = passwordEncoder;
        this.securityUtils = securityUtils;
    }
    private User getCurrentUser() {
        return securityUtils.getCurrentUser();
    }
    @Override
    public List<UserListDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserListDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserListDto getUserDetailsById(Long id) {
        validateAccess(id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(ErrorMessage.ID_NOT_FOUND));
        return userMapper.toUserListDto(user);
    }

    @Override
    @Transactional
    public UserAfterCreationDto createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistsException(ErrorMessage.USER_ALREADY_EXISTS);
        }
        User user = userMapper.toEntity(userDto);
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User createdUser = userRepository.save(user);

        cartService.createCart(createdUser.getId());

        return userMapper.toUserAfterCreationDto(createdUser);
    }

    @Override
    public UserAfterUpdateDto updateUser(Long id, UserDto userDto) {
        validateAccess(id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException(ErrorMessage.ID_NOT_FOUND));
        userMapper.updateEntityFromDto(userDto, user);

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);

        return userMapper.toUserAfterUpdateDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User currentUser = securityUtils.getCurrentUser();


        if (!id.equals(currentUser.getId()) && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException(ErrorMessage.ACCESS_DENIED);
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


    private void validateAccess(Long id) {
        User currentUser = securityUtils.getCurrentUser();
        if (!id.equals(currentUser.getId()) && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException(ErrorMessage.ACCESS_DENIED);
        }
    }


}
