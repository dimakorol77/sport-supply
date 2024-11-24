package org.example.controllers;

import org.example.dto.*;
import org.example.enums.Role;
import org.example.exceptions.IdNotFoundException;
import org.example.exceptions.UserAlreadyExistsException;
import org.example.exceptions.UserNotFoundException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.mappers.UserMapper;
import org.example.models.User;
import org.example.repositories.UserRepository;
import org.example.services.impl.UserServiceImpl;
import org.example.services.interfaces.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
// Убедитесь, что вы импортируете правильный класс AccessDeniedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// Импортируем только необходимые методы из Hamcrest
import static org.hamcrest.Matchers.is;
// Импортируем все методы из Mockito ArgumentMatchers
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
// Импортируем Mockito методы
import static org.mockito.Mockito.*;
// Импортируем методы для MockMvc


class UserControllerTest {

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
    private UserDto userDto;
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

        userDto = new UserDto();
        userDto.setEmail("newemail@example.com");
        userDto.setName("New Name");
        userDto.setPassword("newpassword");

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
    void testGetUserDetailsById_Success() {
        // Устанавливаем SecurityContext с аутентифицированным пользователем
        org.springframework.security.core.userdetails.User userPrincipal =
                new org.springframework.security.core.userdetails.User("test@example.com", "", Collections.emptyList());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(userMapper.toUserListDto(user)).thenReturn(userListDto);

        UserListDto result = userService.getUserDetailsById(1L);

        assertNotNull(result);
        assertEquals(userListDto, result);

        // Очищаем SecurityContext после теста
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetUserDetailsById_NotFound() {
        // Устанавливаем SecurityContext с аутентифицированным пользователем
        org.springframework.security.core.userdetails.User userPrincipal =
                new org.springframework.security.core.userdetails.User("test@example.com", "", Collections.emptyList());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        IdNotFoundException exception = assertThrows(IdNotFoundException.class, () -> userService.getUserDetailsById(1L));
        assertEquals(ErrorMessage.ID_NOT_FOUND, exception.getMessage());

        // Очищаем SecurityContext после теста
        SecurityContextHolder.clearContext();
    }

    @Test
    void testCreateUser_Success() {
        when(userRepository.existsByEmail("newemail@example.com")).thenReturn(false);
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserAfterCreationDto(user)).thenReturn(userAfterCreationDto);

        UserAfterCreationDto result = userService.createUser(userDto);

        assertNotNull(result);
        assertEquals(userAfterCreationDto, result);
        verify(cartService, times(1)).createCart(user.getId());
    }

    @Test
    void testCreateUser_AlreadyExists() {
        when(userRepository.existsByEmail("newemail@example.com")).thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userDto));
        assertEquals(ErrorMessage.USER_ALREADY_EXISTS, exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateUser_Success() {
        // Устанавливаем SecurityContext с аутентифицированным пользователем
        org.springframework.security.core.userdetails.User userPrincipal =
                new org.springframework.security.core.userdetails.User("test@example.com", "", Collections.emptyList());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        doNothing().when(userMapper).updateEntityFromDto(userDto, user);
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserAfterUpdateDto(user)).thenReturn(userAfterUpdateDto);

        UserAfterUpdateDto result = userService.updateUser(1L, userDto);

        assertNotNull(result);
        assertEquals(userAfterUpdateDto, result);

        // Очищаем SecurityContext после теста
        SecurityContextHolder.clearContext();
    }

    @Test
    void testUpdateUser_AccessDenied() {
        // Устанавливаем SecurityContext с другим пользователем
        org.springframework.security.core.userdetails.User userPrincipal =
                new org.springframework.security.core.userdetails.User("other@example.com", "", Collections.emptyList());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setEmail("other@example.com");
        otherUser.setRole(Role.USER);

        when(userRepository.findByEmail("other@example.com")).thenReturn(java.util.Optional.of(otherUser));
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        org.springframework.security.access.AccessDeniedException exception = assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> userService.updateUser(1L, userDto));
        assertEquals(ErrorMessage.ACCESS_DENIED, exception.getMessage());

        // Очищаем SecurityContext после теста
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDeleteUser_Success() {
        // Устанавливаем SecurityContext с аутентифицированным пользователем
        org.springframework.security.core.userdetails.User userPrincipal =
                new org.springframework.security.core.userdetails.User("test@example.com", "", Collections.emptyList());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));
        when(userRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository, times(1)).deleteById(1L);

        // Очищаем SecurityContext после теста
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDeleteUser_AccessDenied() {
        // Устанавливаем SecurityContext с другим пользователем
        org.springframework.security.core.userdetails.User userPrincipal =
                new org.springframework.security.core.userdetails.User("other@example.com", "", Collections.emptyList());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setEmail("other@example.com");
        otherUser.setRole(Role.USER);

        when(userRepository.findByEmail("other@example.com")).thenReturn(java.util.Optional.of(otherUser));
        when(userRepository.existsById(1L)).thenReturn(true);

        org.springframework.security.access.AccessDeniedException exception = assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> userService.deleteUser(1L));
        assertEquals(ErrorMessage.ACCESS_DENIED, exception.getMessage());

        // Очищаем SecurityContext после теста
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDeleteUser_NotFound() {
        // Устанавливаем SecurityContext с аутентифицированным пользователем
        org.springframework.security.core.userdetails.User userPrincipal =
                new org.springframework.security.core.userdetails.User("test@example.com", "", Collections.emptyList());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));
        when(userRepository.existsById(1L)).thenReturn(false);

        IdNotFoundException exception = assertThrows(IdNotFoundException.class, () -> userService.deleteUser(1L));
        assertEquals(ErrorMessage.ID_NOT_FOUND, exception.getMessage());
        verify(userRepository, never()).deleteById(anyLong());

        // Очищаем SecurityContext после теста
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetUserByEmail_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));

        User result = userService.getUserByEmail("test@example.com");

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testGetUserByEmail_NotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("test@example.com"));
        assertEquals(ErrorMessage.USER_NOT_FOUND, exception.getMessage());
    }
}
