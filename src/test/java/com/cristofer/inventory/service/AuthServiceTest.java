package com.cristofer.inventory.service;

import com.cristofer.inventory.dto.response.AuthResponse;
import com.cristofer.inventory.dto.request.LoginRequest;
import com.cristofer.inventory.dto.request.RegisterRequest;
import com.cristofer.inventory.model.User;
import com.cristofer.inventory.model.enums.Role;
import com.cristofer.inventory.repository.UserRepository;
import com.cristofer.inventory.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequest.builder()
                .username("cristofer")
                .email("cristofer@example.com")
                .password("secret123")
                .build();

        loginRequest = LoginRequest.builder()
                .username("cristofer")
                .password("secret123")
                .build();

        user = User.builder()
                .id(1L)
                .username("cristofer")
                .email("cristofer@example.com")
                .password("encoded_password")
                .role(Role.USER)
                .build();
    }

    @Test
    @DisplayName("Should register a new user successfully and return a JWT token")
    void register_ShouldReturnToken_WhenUserIsNew() {
        when(userRepository.existsByUsername("cristofer")).thenReturn(false);
        when(userRepository.existsByEmail("cristofer@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("encoded_password");
        when(jwtService.generateToken(any(User.class))).thenReturn("mocked.jwt.token");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("mocked.jwt.token", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when registering an existing username")
    void register_ShouldThrowException_WhenUsernameAlreadyExists() {
        when(userRepository.existsByUsername("cristofer")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(registerRequest)
        );

        assertEquals("Username is already taken", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should authenticate user and return a JWT token on login")
    void login_ShouldReturnToken_WhenCredentialsAreValid() {
        when(userRepository.findByUsername("cristofer")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("mocked.jwt.token");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("mocked.jwt.token", response.getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}