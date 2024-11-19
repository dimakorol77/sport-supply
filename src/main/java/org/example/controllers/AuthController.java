package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.AuthenticationResponseDto;
import org.example.dto.UserAfterCreationDto;
import org.example.dto.UserCreateDto;
import org.example.dto.UserLoginDto;
import org.example.enums.Role;
import org.example.models.User;
import org.example.repositories.UserRepository;
import org.example.services.impl.JwtSecurityService;
import org.example.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtSecurityService jwtSecurityService;

    /**
     * Метод для регистрации нового пользователя
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        UserAfterCreationDto createdUser = userService.createUser(userCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Метод для логина пользователя
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserLoginDto userLoginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginDto.getEmail(),
                            userLoginDto.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtSecurityService.generateToken((org.springframework.security.core.userdetails.User) authentication.getPrincipal());

            // Генерация refresh-токена
            String refreshToken = jwtSecurityService.generateRefreshToken(new HashMap<>(), (org.springframework.security.core.userdetails.User) authentication.getPrincipal());

            AuthenticationResponseDto responseDto = new AuthenticationResponseDto(jwt, refreshToken);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Неверные учетные данные");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
}