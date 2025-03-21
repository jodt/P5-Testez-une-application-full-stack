package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AuthControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;


    @Test
    @DisplayName("should login user")
    void shouldLoginUser() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("test!1234");
        loginRequest.setEmail("user@mail.fr");

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/Json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should login user -> bad mail")
    void shouldNotLoginUserBadMail() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("test!1234");
        loginRequest.setEmail("badUser@mail.fr");

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/Json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("should login user -> bad password")
    void shouldNotLoginUserBadPassword() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("test!1236");
        loginRequest.setEmail("user@mail.fr");

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/Json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("should register new user")
    void shouldRegisterUser() throws Exception {

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setPassword("test!1234");
        signupRequest.setEmail("newUser@mail.fr");
        signupRequest.setFirstName("newUserFirstName");
        signupRequest.setLastName("newUserLastName");

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/Json")
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        Optional<User> userRegistered = this.userRepository.findByEmail("newUser@mail.fr");
        assertTrue(userRegistered.isPresent());
    }

    @Test
    @DisplayName("should not register new user -> user already exist")
    void shoulRegisterUser() throws Exception {

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setPassword("test!1234");
        signupRequest.setEmail("user@mail.fr");
        signupRequest.setFirstName("userFirstName");
        signupRequest.setLastName("userLastName");

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/Json")
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));
    }

}
