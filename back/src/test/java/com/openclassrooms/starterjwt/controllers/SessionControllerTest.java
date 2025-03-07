package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

    @Mock
    SessionMapper sessionMapper;

    @Mock
    SessionService sessionService;

    @InjectMocks
    SessionController sessionController;

    private Session session;

    private Session otherSession;

    private SessionDto sessionDto;

    private SessionDto otherSessionDto;

    private Teacher teacher;

    @BeforeEach
    void init() {

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
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .teacher(teacher)
                .users(new ArrayList<>())
                .build();

        otherSession = Session.builder()
                .id(2L)
                .name("relaxation")
                .description("relaxation for beginners")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .teacher(teacher)
                .users(new ArrayList<>())
                .build();


        sessionDto = new SessionDto(
                1L,
                "yoga",
                new Date(),
                1L,
                "yoga for beginners",
                new ArrayList<>(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        otherSessionDto = new SessionDto(
                2L,
                "relaxation",
                new Date(),
                1L,
                "relaxation for beginners",
                new ArrayList<>(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("should find session by its id")
    void shouldFindSessionById() {

        when(this.sessionService.getById(1L)).thenReturn(session);
        when(this.sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = this.sessionController.findById("1");

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.hasBody());
        assertEquals(sessionDto, response.getBody());

        verify(this.sessionService).getById(1L);
        verify(this.sessionMapper).toDto(session);
    }

    @Test
    @DisplayName("should not find session by its id -> session not found")
    void shouldNotFindSessionById() {

        when(this.sessionService.getById(1L)).thenReturn(null);

        ResponseEntity<?> response = this.sessionController.findById("1");

        assertEquals(404, response.getStatusCodeValue());
        assertFalse(response.hasBody());

        verify(this.sessionService).getById(1L);
    }

    @Test
    @DisplayName("should not find session by its id -> bad request")
    void shouldNotFindSessionByIdBadRequest() {

        ResponseEntity<?> response = this.sessionController.findById("A");

        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.hasBody());

    }

    @Test
    @DisplayName("should find all sessions")
    void shouldFindAllSessions() {

        List<Session> sessionsFound = new ArrayList<>();
        sessionsFound.add(session);
        sessionsFound.add(otherSession);

        List<SessionDto> sessionsDtoFound = new ArrayList<>();
        sessionsDtoFound.add(sessionDto);
        sessionsDtoFound.add(otherSessionDto);

        when(this.sessionService.findAll()).thenReturn(sessionsFound);
        when(this.sessionMapper.toDto(sessionsFound)).thenReturn(sessionsDtoFound);

        ResponseEntity<?> response = this.sessionController.findAll();

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.hasBody());
        assertEquals(2, ((List<SessionDto>) response.getBody()).size());
        assertEquals(sessionDto, ((List<SessionDto>) response.getBody()).get(0));
        assertEquals(otherSessionDto, ((List<SessionDto>) response.getBody()).get(1));

        verify(this.sessionService).findAll();
        verify(this.sessionMapper).toDto(sessionsFound);
    }

    @Test
    @DisplayName("should create a session")
    void shouldCreateSession() {

        when(this.sessionMapper.toEntity(otherSessionDto)).thenReturn(otherSession);
        when(this.sessionService.create(otherSession)).thenReturn(otherSession);
        when(this.sessionMapper.toDto(otherSession)).thenReturn(otherSessionDto);

        ResponseEntity<?> response = this.sessionController.create(otherSessionDto);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.hasBody());
        assertEquals(otherSessionDto, response.getBody());

        verify(this.sessionMapper).toEntity(otherSessionDto);
        verify(this.sessionService).create(otherSession);
    }

    @Test
    @DisplayName("Should update session")
    void shouldUpdateSession() {

        when(this.sessionMapper.toEntity(otherSessionDto)).thenReturn(otherSession);
        when(this.sessionService.update(2L, otherSession)).thenReturn(otherSession);
        when(this.sessionMapper.toDto(otherSession)).thenReturn(otherSessionDto);

        ResponseEntity<?> response = this.sessionController.update("2", otherSessionDto);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.hasBody());
        assertEquals(otherSessionDto, response.getBody());

        verify(this.sessionMapper).toEntity(otherSessionDto);
        verify(this.sessionService).update(2L, otherSession);
    }

    @Test
    @DisplayName("Should not update session -> bad requests")
    void shouldNotUpdateSession() {

        ResponseEntity<?> response = this.sessionController.update("A", otherSessionDto);

        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.hasBody());
    }

    @Test
    @DisplayName("Should delete session by its id")
    void shouldDeleteSession() {

        when(this.sessionService.getById(1L)).thenReturn(session);
        doNothing().when(this.sessionService).delete(1L);

        ResponseEntity<?> response = this.sessionController.save("1");

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.hasBody());

        verify(this.sessionService).getById(1L);
        verify(this.sessionService).delete(1L);
    }

    @Test
    @DisplayName("Should not delete session by its id -> session not found")
    void shouldNotDeleteSessionNotFound() {

        when(this.sessionService.getById(1L)).thenReturn(null);

        ResponseEntity<?> response = this.sessionController.save("1");

        assertEquals(404, response.getStatusCodeValue());
        assertFalse(response.hasBody());
    }

    @Test
    @DisplayName("Should not delete session by its id -> bad request")
    void shouldNotDeleteSessionBadRequest() {

        ResponseEntity<?> response = this.sessionController.save("A");

        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.hasBody());
    }

    @Test
    @DisplayName("should participate in a session")
    void shouldParticipate() {

        doNothing().when(this.sessionService).participate(1L, 1L);

        ResponseEntity<?> response = this.sessionController.participate("1", "1");

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.hasBody());
    }

    @Test
    @DisplayName("should not participate in a session -> bad request")
    void shouldNotParticipateBadRequest() {

        ResponseEntity<?> response = this.sessionController.participate("A", "1");

        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.hasBody());
    }

    @Test
    @DisplayName("should no longer participate")
    void shouldNoLongerParticipate() {

        doNothing().when(this.sessionService).noLongerParticipate(1L, 1L);

        ResponseEntity<?> response = this.sessionController.noLongerParticipate("1", "1");

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.hasBody());
    }

    @Test
    @DisplayName("should no longer participate -> badRequest")
    void shouldNoLongerParticipateBadRequest() {

        ResponseEntity<?> response = this.sessionController.noLongerParticipate("A", "1");

        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.hasBody());
    }
}