package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@WithMockUser(username = "user@mail.fr")
public class UserControllerIT {

    @Autowired
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;


    @Test
    @DisplayName("should find user by its id")
    void shouldFindUserById() throws Exception {


        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("user@mail.fr"))
                .andExpect(jsonPath("$.lastName").value("userLastName"))
                .andExpect(jsonPath("$.firstName").value("userFirstName"))
                .andExpect(jsonPath("$.admin").value(true));

    }

    @Test
    @DisplayName("should not find user by its id -> user not found")
    void shouldNotFindUserById() throws Exception {

        mockMvc.perform(get("/api/user/3"))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("should not find user by its id -> bad request")
    void shouldNotFindUserByIdBadRequest() throws Exception {

        mockMvc.perform(get("/api/user/A"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "otherUser@mail.fr")
    @DisplayName("should delete user")
    void shouldDeleteUserByItsId() throws Exception {

        mockMvc.perform(delete("/api/user/2"))
                .andExpect(status().isOk());

        Optional<User> userDeleted = this.userRepository.findById(2L);
        assertFalse(userDeleted.isPresent());
    }

    @Test
    @DisplayName("should not delete user -> user not found")
    void shouldNotDeleteUserByItsIdUserNotFound() throws Exception {

        mockMvc.perform(delete("/api/user/5"))
                .andExpect(status().isNotFound());

    }

    @Test
    @WithAnonymousUser
    @DisplayName("should not delete user -> unauthorized")
    void shouldNotDeleteUserByItsIdUnauthorized() throws Exception {

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @DisplayName("should delete user by its id -> bad request")
    void shouldNotDeleteUserByIdBadRequest() throws Exception {

        mockMvc.perform(delete("/api/user/A"))
                .andExpect(status().isBadRequest());
    }

}
