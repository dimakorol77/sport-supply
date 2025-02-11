package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.annotations.AuthAnnotations.AuthenticateUser;
import org.example.annotations.AuthAnnotations.RegisterUser;
import org.example.dto.AuthenticationResponseDto;
import org.example.dto.UserAfterCreationDto;
import org.example.dto.UserDto;
import org.example.dto.UserLoginDto;
import org.example.services.impl.JwtSecurityService;
import org.example.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
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


    @RegisterUser
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto) {
        UserAfterCreationDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @AuthenticateUser
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


            String refreshToken = jwtSecurityService.generateRefreshToken(new HashMap<>(), (org.springframework.security.core.userdetails.User) authentication.getPrincipal());

            AuthenticationResponseDto responseDto = new AuthenticationResponseDto(jwt, refreshToken);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Incorrect credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
}