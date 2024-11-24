package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controllers.handler.ResponseExceptionHandler;
import org.example.dto.AuthenticationResponseDto;
import org.example.dto.UserAfterCreationDto;
import org.example.dto.UserDto;
import org.example.dto.UserLoginDto;
import org.example.exceptions.UserAlreadyExistsException;
import org.example.exceptions.errorMessage.ErrorMessage;
import org.example.models.User;
import org.example.services.impl.JwtSecurityService;
import org.example.services.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtSecurityService jwtSecurityService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;

    private UserDto userDto;
    private UserAfterCreationDto userAfterCreationDto;
    private UserLoginDto userLoginDto;
    private AuthenticationResponseDto authenticationResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setName("Test User");
        userDto.setPassword("password");

        userAfterCreationDto = new UserAfterCreationDto();
        userAfterCreationDto.setId(1L);
        userAfterCreationDto.setEmail("test@example.com");
        userAfterCreationDto.setName("Test User");

        userLoginDto = new UserLoginDto();
        userLoginDto.setEmail("test@example.com");
        userLoginDto.setPassword("password");

        authenticationResponseDto = new AuthenticationResponseDto("jwtToken", "refreshToken");
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        when(userService.createUser(any(UserDto.class))).thenReturn(userAfterCreationDto);

        String userJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(userAfterCreationDto.getId().intValue())))
                .andExpect(jsonPath("$.email", is(userAfterCreationDto.getEmail())))
                .andExpect(jsonPath("$.name", is(userAfterCreationDto.getName())));
    }

    @Test
    void testRegisterUser_AlreadyExists() throws Exception {
        when(userService.createUser(any(UserDto.class))).thenThrow(new UserAlreadyExistsException(ErrorMessage.USER_ALREADY_EXISTS));

        String userJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isConflict())
                .andExpect(content().string(ErrorMessage.USER_ALREADY_EXISTS));
    }

    @Test
    void testAuthenticateUser_Success() throws Exception {
        // Создаем объект UserDetails
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                userLoginDto.getEmail(),
                userLoginDto.getPassword(),
                new ArrayList<>()
        );

        // Мокаем объект Authentication
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Мокаем методы
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtSecurityService.generateToken(any(UserDetails.class))).thenReturn("jwtToken");
        when(jwtSecurityService.generateRefreshToken(anyMap(), any(UserDetails.class))).thenReturn("refreshToken");
        when(userService.getUserByEmail(anyString())).thenReturn(new User());

        String loginJson = objectMapper.writeValueAsString(userLoginDto);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", is("jwtToken")))
                .andExpect(jsonPath("$.refreshToken", is("refreshToken")));
    }

    @Test
    void testAuthenticateUser_InvalidCredentials() throws Exception {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

        String loginJson = objectMapper.writeValueAsString(userLoginDto);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("Неверные учетные данные")));
    }
}
