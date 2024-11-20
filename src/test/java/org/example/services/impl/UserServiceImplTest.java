// src/test/java/org/example/services/impl/UserServiceImplTest.java

package org.example.services.impl;

import org.example.dto.*;
import org.example.enums.Role;
import org.example.exceptions.IdNotFoundException;
import org.example.exceptions.UserAlreadyExistsException;
import org.example.exceptions.UserNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.UserMapper;
import org.example.models.User;
import org.example.repositories.UserRepository;
import org.example.services.interfaces.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private CartService cartService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserCreateDto userCreateDto;
    private UserUpdateDto userUpdateDto;
    private UserAfterCreationDto userAfterCreationDto;
    private UserAfterUpdateDto userAfterUpdateDto;
    private UserListDto userListDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setPassword("encodedPassword");
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userCreateDto = new UserCreateDto();
        userCreateDto.setEmail("test@example.com");
        userCreateDto.setName("Test User");
        userCreateDto.setPassword("password");

        userUpdateDto = new UserUpdateDto();
        userUpdateDto.setEmail("newemail@example.com");
        userUpdateDto.setName("New Name");
        userUpdateDto.setPassword("newpassword");

        userAfterCreationDto = new UserAfterCreationDto();
        userAfterCreationDto.setId(1L);
        userAfterCreationDto.setEmail("test@example.com");
        userAfterCreationDto.setName("Test User");

        userAfterUpdateDto = new UserAfterUpdateDto();
        userAfterUpdateDto.setId(1L);
        userAfterUpdateDto.setEmail("newemail@example.com");
        userAfterUpdateDto.setName("New Name");

        userListDto = new UserListDto();
        userListDto.setId(1L);
        userListDto.setEmail("test@example.com");
        userListDto.setName("Test User");
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        when(userMapper.toUserListDto(user)).thenReturn(userListDto);

        List<UserListDto> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(userListDto, users.get(0));
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        IdNotFoundException exception = assertThrows(IdNotFoundException.class, () -> userService.getUserById(1L));
        assertEquals(ErrorMessage.ID_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testCreateUser_Success() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userMapper.toEntity(userCreateDto)).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserAfterCreationDto(user)).thenReturn(userAfterCreationDto);

        UserAfterCreationDto result = userService.createUser(userCreateDto);

        assertNotNull(result);
        assertEquals(userAfterCreationDto, result);
        verify(cartService, times(1)).createCart(user.getId());
    }

    @Test
    void testCreateUser_AlreadyExists() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userCreateDto));
        assertEquals(ErrorMessage.USER_ALREADY_EXISTS, exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateUser_Success() {
        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setRole(Role.USER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userMapper).updateEntityFromDto(userUpdateDto, user);
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserAfterUpdateDto(user)).thenReturn(userAfterUpdateDto);

        Optional<UserAfterUpdateDto> result = userService.updateUser(1L, userUpdateDto, currentUser);

        assertTrue(result.isPresent());
        assertEquals(userAfterUpdateDto, result.get());
    }

    @Test
    void testUpdateUser_AccessDenied() {
        User currentUser = new User();
        currentUser.setId(2L); // Разный ID пользователя
        currentUser.setRole(Role.USER);

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> userService.updateUser(1L, userUpdateDto, currentUser));
        assertEquals(ErrorMessage.ACCESS_DENIED, exception.getMessage());
    }

    @Test
    void testDeleteUser_Success() {
        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setRole(Role.USER);

        when(userRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> userService.deleteUser(1L, currentUser));
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_AccessDenied() {
        User currentUser = new User();
        currentUser.setId(2L); // Разный ID пользователя
        currentUser.setRole(Role.USER);

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> userService.deleteUser(1L, currentUser));
        assertEquals(ErrorMessage.ACCESS_DENIED, exception.getMessage());
    }

    @Test
    void testDeleteUser_NotFound() {
        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setRole(Role.USER);

        when(userRepository.existsById(1L)).thenReturn(false);

        IdNotFoundException exception = assertThrows(IdNotFoundException.class, () -> userService.deleteUser(1L, currentUser));
        assertEquals(ErrorMessage.ID_NOT_FOUND, exception.getMessage());
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetUserByEmail_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        User result = userService.getUserByEmail("test@example.com");

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testGetUserByEmail_NotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("test@example.com"));
        assertEquals(ErrorMessage.USER_NOT_FOUND, exception.getMessage());
    }
}
