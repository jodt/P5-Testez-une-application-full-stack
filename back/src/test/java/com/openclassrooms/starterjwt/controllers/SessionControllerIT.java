package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user@mail.fr")
public class SessionControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SessionController sessionController;

    @Autowired
    SessionService sessionService;

    @MockBean
    SessionRepository sessionRepository;

    @MockBean
    UserRepository userRepository;

    private Session session;

    private Session otherSession;

    private SessionDto sessionDto;

    private SessionDto otherSessionDto;

    private Teacher teacher;

    private User user;

    @BeforeEach
    void init() {

        objectMapper.registerModule(new JavaTimeModule());

        teacher = Teacher.builder()
                .id(1L)
                .firstName("teacherFirstName")
                .lastName("teacherLastName")
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        session = Session.builder()
                .id(1L)
                .name("yoga")
                .description("yoga for beginners")
                .date(new Date(2025 - 1900, 11, 10))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .teacher(teacher)
                .users(new ArrayList<>())
                .build();

        otherSession = Session.builder()
                .id(2L)
                .name("relaxation")
                .description("relaxation for beginners")
                .date(new Date(2025 - 1900, 11, 10))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .teacher(teacher)
                .users(new ArrayList<>())
                .build();


        sessionDto = new SessionDto(
                1L,
                "yoga",
                new Date(2025 - 1900, 11, 10),
                1L,
                "yoga for beginners",
                new ArrayList<>(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        otherSessionDto = new SessionDto(
                2L,
                "relaxation",
                new Date(2025 - 1900, 11, 10),
                1L,
                "relaxation for beginners",
                new ArrayList<>(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

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
    @DisplayName("should find session by its id")
    void shouldFindSessionById() throws Exception {

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        mockMvc.perform(get("/api/session/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("yoga"))
                .andExpect(jsonPath("$.description").value("yoga for beginners"))
                .andExpect(jsonPath("$.teacher_id").value(1));

        verify(this.sessionRepository).findById(1L);
    }

    @Test
    @DisplayName("should not find session by its id -> session not found")
    void shouldNotFindSessionById() throws Exception {

        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/session/1"))
                .andExpect(status().isNotFound());

        verify(this.sessionRepository).findById(1L);
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

        List<Session> sessions = new ArrayList<>();
        sessions.add(session);
        sessions.add(otherSession);

        when(sessionRepository.findAll()).thenReturn(sessions);

        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("yoga"))
                .andExpect(jsonPath("$[0].description").value("yoga for beginners"))
                .andExpect(jsonPath("$[0].teacher_id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("relaxation"))
                .andExpect(jsonPath("$[1].description").value("relaxation for beginners"))
                .andExpect(jsonPath("$[1].teacher_id").value(1));

        verify(this.sessionRepository).findAll();
    }

    @Test
    @DisplayName("should create a session")
    void shouldCreateSession() throws Exception {

        when(this.sessionRepository.save(session)).thenReturn(session);

        mockMvc.perform(post("/api/session")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("yoga"))
                .andExpect(jsonPath("$.description").value("yoga for beginners"))
                .andExpect(jsonPath("$.teacher_id").value(1))
                .andExpect(jsonPath("$.date").value("2025-12-09T23:00:00.000+00:00"));

        verify(this.sessionRepository).save(session);
    }

    @Test
    @DisplayName("Should update session")
    void shouldUpdateSession() throws Exception {

        when(this.sessionRepository.save(session)).thenReturn(session);

        mockMvc.perform(put("/api/session/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("yoga"))
                .andExpect(jsonPath("$.description").value("yoga for beginners"))
                .andExpect(jsonPath("$.teacher_id").value(1))
                .andExpect(jsonPath("$.date").value("2025-12-09T23:00:00.000+00:00"));

        verify(this.sessionRepository).save(session);
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

        when(this.sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        doNothing().when(this.sessionRepository).deleteById(1L);

        mockMvc.perform(delete("/api/session/1"))
                .andExpect(status().isOk());

        verify(this.sessionRepository).findById(1L);
        verify(this.sessionRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should not delete session by its id -> session not found")
    void shouldNotDeleteSessionNotFound() throws Exception {

        when(this.sessionRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/session/1"))
                .andExpect(status().isNotFound());

        verify(this.sessionRepository).findById(1L);
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

        when(this.sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/session/1/participate/1"))
                .andExpect(status().isOk());

        verify(this.sessionRepository).findById(1L);
        verify(this.userRepository).findById(1L);
    }

    @Test
    @DisplayName("should not participate in a session -> bad request")
    void shouldNotParticipateBadRequest() throws Exception {

        List<User> users = new ArrayList<>();
        users.add(user);
        session.setUsers(users);

        when(this.sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/session/1/participate/1"))
                .andExpect(status().isBadRequest());

        verify(this.sessionRepository).findById(1L);
        verify(this.userRepository).findById(1L);

    }

    @Test
    @DisplayName("should not participate in a session -> session not found")
    void shouldNotParticipateSessionNotFound() throws Exception {

        when(this.sessionRepository.findById(1L)).thenReturn(Optional.empty());
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/session/1/participate/1"))
                .andExpect(status().isNotFound());

        verify(this.sessionRepository).findById(1L);
        verify(this.userRepository).findById(1L);
    }

    @Test
    @DisplayName("should not participate in a session -> user not found")
    void shouldNotParticipateUserNotFound() throws Exception {

        when(this.sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(this.userRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/session/1/participate/1"))
                .andExpect(status().isNotFound());

        verify(this.sessionRepository).findById(1L);
        verify(this.userRepository).findById(1L);
    }

    @Test
    @DisplayName("should no longer participate")
    void shouldNoLongerParticipate() throws Exception {

        List<User> users = new ArrayList<>();
        users.add(user);
        session.setUsers(users);

        when(this.sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        mockMvc.perform(delete("/api/session/1/participate/1"))
                .andExpect(status().isOk());

        verify(this.sessionRepository).findById(1L);
    }

    @Test
    @DisplayName("should no longer participate -> badRequest")
    void shouldNoLongerParticipateBadRequest() throws Exception {

        when(this.sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        mockMvc.perform(delete("/api/session/1/participate/1"))
                .andExpect(status().isBadRequest());

        verify(this.sessionRepository).findById(1L);
    }

    @Test
    @DisplayName("should no longer participate -> session not found")
    void shouldNoLongerParticipateSessionNotFound() throws Exception {

        when(this.sessionRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/session/1/participate/1"))
                .andExpect(status().isNotFound());

        verify(this.sessionRepository).findById(1L);
    }

}
