package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    UserRepository userRepository;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    Authentication authentication;

    @Mock
    JwtUtils jwtUtils;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    AuthController authController;


    @Test
    @DisplayName("should authenticate user")
    void shouldAuthenticateUser() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@mail.fr");
        loginRequest.setPassword("password");

        User user = User.builder()
                .id(1L)
                .admin(true)
                .password("password")
                .email("user@mail.fr")
                .firstName("userFirstName")
                .lastName("userLastName")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .admin(true)
                .username("user@mail.fr")
                .lastName("userLastName")
                .firstName("userFirstName")
                .build();


        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("token");
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

        ResponseEntity<?> response = this.authController.authenticateUser(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.hasBody());
        assertEquals("token", ((JwtResponse) response.getBody()).getToken());
        assertEquals(1L, ((JwtResponse) response.getBody()).getId());
        assertEquals("userFirstName", ((JwtResponse) response.getBody()).getFirstName());
        assertEquals("userLastName", ((JwtResponse) response.getBody()).getLastName());
        assertEquals("user@mail.fr", ((JwtResponse) response.getBody()).getUsername());
    }

    @Test
    @DisplayName("should register a user")
    void shouldRegisterUser() {

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("user@mail.fr");
        signupRequest.setFirstName("userFirstName");
        signupRequest.setLastName("userLastName");
        signupRequest.setPassword("password");

        when(this.userRepository.existsByEmail("user@mail.fr")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("passwordEncoded");

        ResponseEntity<?> response = this.authController.registerUser(signupRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.hasBody());
        assertEquals("User registered successfully!", ((MessageResponse) response.getBody()).getMessage());

        verify(this.userRepository).existsByEmail("user@mail.fr");
        verify(this.passwordEncoder).encode("password");
    }

    @Test
    @DisplayName("should register a user -> user already register")
    void shouldNotRegisterUserAlreadyExist() {

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("user@mail.fr");
        signupRequest.setFirstName("userFirstName");
        signupRequest.setLastName("userLastName");
        signupRequest.setPassword("password");

        when(this.userRepository.existsByEmail("user@mail.fr")).thenReturn(true);

        ResponseEntity<?> response = this.authController.registerUser(signupRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.hasBody());
        assertEquals("Error: Email is already taken!", ((MessageResponse) response.getBody()).getMessage());

        verify(this.userRepository).existsByEmail("user@mail.fr");
    }
}