package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controllers.handler.ResponseExceptionHandler;
import org.example.dto.*;
import org.example.exceptions.UserAlreadyExistsException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.models.User;
import org.example.services.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
// Убедитесь, что вы импортируете правильный класс AccessDeniedException
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

// Импортируем только необходимые методы из Hamcrest
import static org.hamcrest.Matchers.is;
// Импортируем все методы из Mockito ArgumentMatchers
import static org.mockito.ArgumentMatchers.*;
// Импортируем Mockito методы
import static org.mockito.Mockito.*;
// Импортируем методы для MockMvc
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserCreateDto userCreateDto;
    private UserAfterCreationDto userAfterCreationDto;
    private UserUpdateDto userUpdateDto;
    private UserAfterUpdateDto userAfterUpdateDto;
    private UserListDto userListDto;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new ResponseExceptionHandler()) // Используем существующий обработчик
                .build();
        objectMapper = new ObjectMapper();

        userCreateDto = new UserCreateDto();
        userCreateDto.setEmail("test@example.com");
        userCreateDto.setName("Test User");
        userCreateDto.setPassword("password");

        userAfterCreationDto = new UserAfterCreationDto();
        userAfterCreationDto.setId(1L);
        userAfterCreationDto.setEmail("test@example.com");
        userAfterCreationDto.setName("Test User");

        userUpdateDto = new UserUpdateDto();
        userUpdateDto.setEmail("newemail@example.com");
        userUpdateDto.setName("New Name");
        userUpdateDto.setPassword("newpassword");

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
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(userListDto));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(userListDto.getId().intValue())))
                .andExpect(jsonPath("$[0].email", is(userListDto.getEmail())))
                .andExpect(jsonPath("$[0].name", is(userListDto.getName())));
    }

    @Test
    void testCreateUser_Success() throws Exception {
        when(userService.createUser(any(UserCreateDto.class))).thenReturn(userAfterCreationDto);

        String userJson = objectMapper.writeValueAsString(userCreateDto);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(userAfterCreationDto.getId().intValue())))
                .andExpect(jsonPath("$.email", is(userAfterCreationDto.getEmail())))
                .andExpect(jsonPath("$.name", is(userAfterCreationDto.getName())));
    }

    @Test
    void testCreateUser_AlreadyExists() throws Exception {
        when(userService.createUser(any(UserCreateDto.class))).thenThrow(new UserAlreadyExistsException(ErrorMessage.USER_ALREADY_EXISTS));

        String userJson = objectMapper.writeValueAsString(userCreateDto);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isConflict())
                .andExpect(content().string(ErrorMessage.USER_ALREADY_EXISTS));
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        when(userService.updateUser(eq(1L), any(UserUpdateDto.class), any(User.class))).thenReturn(Optional.of(userAfterUpdateDto));

        String userUpdateJson = objectMapper.writeValueAsString(userUpdateDto);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userUpdateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userAfterUpdateDto.getId().intValue())))
                .andExpect(jsonPath("$.email", is(userAfterUpdateDto.getEmail())))
                .andExpect(jsonPath("$.name", is(userAfterUpdateDto.getName())));
    }

    @Test
    void testUpdateUser_AccessDenied() throws Exception {
        when(userService.updateUser(eq(1L), any(UserUpdateDto.class), any(User.class)))
                .thenThrow(new org.springframework.security.access.AccessDeniedException(ErrorMessage.ACCESS_DENIED));

        String userUpdateJson = objectMapper.writeValueAsString(userUpdateDto);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userUpdateJson))
                .andExpect(status().isForbidden())
                .andExpect(content().string(ErrorMessage.ACCESS_DENIED));
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        doNothing().when(userService).deleteUser(eq(1L), any(User.class));

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUser_AccessDenied() throws Exception {
        doThrow(new org.springframework.security.access.AccessDeniedException(ErrorMessage.ACCESS_DENIED))
                .when(userService).deleteUser(eq(1L), any(User.class));

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isForbidden())
                .andExpect(content().string(ErrorMessage.ACCESS_DENIED));
    }

    // Дополнительные тесты для получения пользователя по ID можно добавить аналогично
}
