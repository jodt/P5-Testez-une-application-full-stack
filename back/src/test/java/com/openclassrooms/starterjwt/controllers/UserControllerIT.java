package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user@mail.fr")
public class UserControllerIT {

    @Autowired
    UserController userController;

    @Autowired
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserRepository userRepository;

    private User user;

    @BeforeEach
    void init() {
        user = User.builder()
                .id(1L)
                .admin(true)
                .password("password")
                .email("user@mail.fr")
                .firstName("userFirstName")
                .lastName("userLastName")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }


    @Test
    @DisplayName("should find user by its id")
    void shouldFindUserById() throws Exception {

        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("user@mail.fr"))
                .andExpect(jsonPath("$.lastName").value("userLastName"))
                .andExpect(jsonPath("$.firstName").value("userFirstName"))
                .andExpect(jsonPath("$.admin").value(true));

        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("should not find user by its id -> user not found")
    void shouldNotFindUserById() throws Exception {

        when(this.userRepository.findById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/user/2"))
                .andExpect(status().isNotFound());

        verify(userRepository).findById(2L);
    }

    @Test
    @DisplayName("should not find user by its id -> bad request")
    void shouldNotFindUserByIdBadRequest() throws Exception {

        mockMvc.perform(get("/api/user/A"))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("should delete user")
    void shouldDeleteUserByItsId() throws Exception {

        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isOk());

        verify(userRepository).findById(1L);

    }

    @Test
    @DisplayName("should not delete user -> user not found")
    void shouldNotDeleteUserByItsIdUserNotFound() throws Exception {

        when(this.userRepository.findById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/user/2"))
                .andExpect(status().isNotFound());

        verify(userRepository).findById(2L);


    }

    @Test
    @DisplayName("should not delete user -> unauthorized")
    void shouldNotDeleteUserByItsIdUnauthorized() throws Exception {

        User secondUser = User.builder()
                .email("secondUser@gmail.com")
                .lastName("secondUserLastName")
                .firstName("secondUserFirstname")
                .admin(false)
                .password("anotherPassword")
                .build();

        when(this.userRepository.findById(3L)).thenReturn(Optional.of(secondUser));

        mockMvc.perform(delete("/api/user/3"))
                .andExpect(status().isUnauthorized());

        verify(userRepository).findById(3L);

    }

    @Test
    @DisplayName("should delete user by its id -> bad request")
    void shouldNotDeleteUserByIdBadRequest() throws Exception {

        mockMvc.perform(delete("/api/user/A"))
                .andExpect(status().isBadRequest());
    }

}
