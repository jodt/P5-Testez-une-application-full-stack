package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@WithMockUser(username = "user@mail.fr")
public class SessionControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    UserRepository userRepository;

    private SessionDto sessionDto;

    @BeforeEach
    void init() {

        objectMapper.registerModule(new JavaTimeModule());

        sessionDto = new SessionDto(
                3L,
                "Hypnosis",
                new Date(2025 - 1900, 11, 10),
                1L,
                "Hypnosis for beginners",
                new ArrayList<>(),
                LocalDateTime.of(2025, 03, 21, 10, 00),
                LocalDateTime.of(2025, 03, 21, 10, 00)
        );
    }

    @Test
    @DisplayName("should find session by its id")
    void shouldFindSessionById() throws Exception {

        mockMvc.perform(get("/api/session/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Yoga"))
                .andExpect(jsonPath("$.description").value("Yoga for beginners"))
                .andExpect(jsonPath("$.teacher_id").value(1))
                .andExpect(jsonPath("$.date").value("2025-06-05T00:00:00.000+00:00"))
                .andExpect(jsonPath("$.users", hasSize(1)))
                .andExpect(jsonPath("$.users[0]").value(1));
    }

    @Test
    @DisplayName("should not find session by its id -> session not found")
    void shouldNotFindSessionById() throws Exception {

        mockMvc.perform(get("/api/session/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should not find session by its id -> bad request")
    void shouldNotFindSessionByIdBadRequest() throws Exception {

        mockMvc.perform(get("/api/session/A"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should find all sessions")
    void shouldFindAllSessions() throws Exception {

        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Yoga"))
                .andExpect(jsonPath("$[0].description").value("Yoga for beginners"))
                .andExpect(jsonPath("$[0].teacher_id").value(1))
                .andExpect(jsonPath("$[0].date").value("2025-06-05T00:00:00.000+00:00"))
                .andExpect(jsonPath("$[0].users", hasSize(1)))
                .andExpect(jsonPath("$[0].users[0]").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Relaxation"))
                .andExpect(jsonPath("$[1].description").value("Relaxation for beginners"))
                .andExpect(jsonPath("$[1].teacher_id").value(1))
                .andExpect(jsonPath("$[1].date").value("2025-06-10T00:00:00.000+00:00"))
                .andExpect(jsonPath("$[1].users").isEmpty());

    }

    @Test
    @DisplayName("should create a session")
    void shouldCreateSession() throws Exception {

        mockMvc.perform(post("/api/session")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Hypnosis"))
                .andExpect(jsonPath("$.description").value("Hypnosis for beginners"))
                .andExpect(jsonPath("$.teacher_id").value(1))
                .andExpect(jsonPath("$.date").value("2025-12-09T23:00:00.000+00:00"));

        Optional<Session> sessionCreated = this.sessionRepository.findById(3L);
        assertTrue(sessionCreated.isPresent());
    }

    @Test
    @DisplayName("Should update session")
    void shouldUpdateSession() throws Exception {

        sessionDto.setName("tai chi");
        sessionDto.setDescription("tai chi for beginners");

        mockMvc.perform(put("/api/session/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("tai chi"))
                .andExpect(jsonPath("$.description").value("tai chi for beginners"))
                .andExpect(jsonPath("$.teacher_id").value(1))
                .andExpect(jsonPath("$.date").value("2025-12-09T23:00:00.000+00:00"));

    }

    @Test
    @DisplayName("Should not update session -> bad requests")
    void shouldNotUpdateSession() throws Exception {

        mockMvc.perform(put("/api/session/A")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should delete session by its id")
    void shouldDeleteSession() throws Exception {

        mockMvc.perform(delete("/api/session/2"))
                .andExpect(status().isOk());

        Optional<Session> sessionDeleted = this.sessionRepository.findById(2L);
        assertFalse(sessionDeleted.isPresent());
    }

    @Test
    @DisplayName("Should not delete session by its id -> session not found")
    void shouldNotDeleteSessionNotFound() throws Exception {

        mockMvc.perform(delete("/api/session/50"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should not delete session by its id -> bad request")
    void shouldNotDeleteSessionBadRequest() throws Exception {

        mockMvc.perform(delete("/api/session/A"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should participate in a session")
    void shouldParticipate() throws Exception {

        mockMvc.perform(post("/api/session/2/participate/1"))
                .andExpect(status().isOk());

        Optional<Session> session = this.sessionRepository.findById(2L);
        assertTrue(session.isPresent());
        assertFalse(session.get().getUsers().isEmpty());
        assertEquals(1, session.get().getUsers().size());

        User participatingUser = session.get().getUsers().get(0);
        Optional<User> user = this.userRepository.findById(1L);
        assertEquals(user.get(), participatingUser);
    }

    @Test
    @DisplayName("should not participate in a session -> bad request")
    void shouldNotParticipateBadRequest() throws Exception {

        mockMvc.perform(post("/api/session/1/participate/1"))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("should not participate in a session -> session not found")
    void shouldNotParticipateSessionNotFound() throws Exception {

        mockMvc.perform(post("/api/session/1/participate/10"))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("should not participate in a session -> user not found")
    void shouldNotParticipateUserNotFound() throws Exception {

        mockMvc.perform(post("/api/session/1/participate/15"))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("should no longer participate")
    void shouldNoLongerParticipate() throws Exception {

        mockMvc.perform(delete("/api/session/1/participate/1"))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("should no longer participate -> badRequest")
    void shouldNoLongerParticipateBadRequest() throws Exception {

        mockMvc.perform(delete("/api/session/1/participate/2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should no longer participate -> session not found")
    void shouldNoLongerParticipateSessionNotFound() throws Exception {

        mockMvc.perform(delete("/api/session/10/participate/1"))
                .andExpect(status().isNotFound());

    }

}
