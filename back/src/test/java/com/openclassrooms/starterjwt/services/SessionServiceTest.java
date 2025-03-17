package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    SessionRepository sessionRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    SessionService sessionService;

    private Session firstSession;
    private Session secondSession;

    private User user;

    @BeforeEach
    void init() {


        firstSession = Session.builder()
                .id(1l)
                .date(new Date(2025,Calendar.DECEMBER,10))
                .name("yoga")
                .description("yoga for beginners")
                .users(new ArrayList<>())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        secondSession = Session.builder()
                .id(2l)
                .date(new Date(2025,Calendar.DECEMBER,10))
                .name("relaxation")
                .description("relaxation for beginners")
                .users(new ArrayList<>())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

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
    @DisplayName("should create a session")
    void shouldCreateSession() {
        when(this.sessionRepository.save(firstSession)).thenReturn(firstSession);

        Session sessionCreated = this.sessionService.create(firstSession);

        assertEquals(firstSession, sessionCreated);
        verify(this.sessionRepository).save(firstSession);
    }

    @Test
    @DisplayName("should delete a session")
    void shouldDeleteSession() {
        doNothing().when(this.sessionRepository).deleteById(firstSession.getId());

        this.sessionService.delete(firstSession.getId());

        verify(this.sessionRepository).deleteById(firstSession.getId());
    }

    @Test
    @DisplayName("should find all sessions")
    void shouldFindAllSessions() {
        List<Session> sessions = new ArrayList<>();
        sessions.add(firstSession);
        sessions.add(secondSession);

        when(this.sessionRepository.findAll()).thenReturn(sessions);
        List<Session> sessionsFound = this.sessionService.findAll();

        assertNotNull(sessionsFound);
        assertEquals(2, sessionsFound.size());
        assertEquals(firstSession, sessionsFound.get(0));
        assertEquals(secondSession, sessionsFound.get(1));

        verify(this.sessionRepository).findAll();
    }


    @Test
    @DisplayName("should get a session by its id")
    void shouldGetSessionById() {
        when(this.sessionRepository.findById(1L)).thenReturn(Optional.of(firstSession));

        Session sessionFound = this.sessionService.getById(1L);

        assertNotNull(sessionFound);
        assertEquals(firstSession, sessionFound);

        verify(this.sessionRepository).findById(1L);
    }

    @Test
    @DisplayName("should not get a session by its id")
    void shouldNotGetSessionById() {
        when(this.sessionRepository.findById(3L)).thenReturn(Optional.empty());

        Session sessionFound = this.sessionService.getById(3L);

        assertNull(sessionFound);

        verify(this.sessionRepository).findById(3L);
    }

    @Test
    @DisplayName("should update a session")
    void shouldUpdateSession() {
        firstSession.setName("hypnosis");
        firstSession.setDescription("hypnosis for beginners");

        Session sessionUpdated = Session.builder()
                .id(1l)
                .date(new Date(2025,Calendar.DECEMBER,10))
                .name("hypnosis")
                .description("hypnosis for beginners")
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        when(this.sessionRepository.save(firstSession)).thenReturn(sessionUpdated);

        Session sessionResult = this.sessionService.update(1L, firstSession);

        assertNotNull(sessionUpdated);
        assertEquals(sessionUpdated, sessionResult);

        verify(this.sessionRepository).save(firstSession);
    }

    @Test
    @DisplayName("should participate in a session")
    void shouldParticipate() {
        when(this.sessionRepository.findById(1L)).thenReturn(Optional.of(firstSession));
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));

        this.sessionService.participate(1L,1L);

        ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);

        verify(this.sessionRepository).save(sessionCaptor.capture());

        Session sessionCaptorValue = sessionCaptor.getValue();

        assertFalse(sessionCaptorValue.getUsers().isEmpty());
        assertEquals(1, sessionCaptorValue.getUsers().size());
        assertEquals(user, sessionCaptorValue.getUsers().get(0));
    }

    @Test
    @DisplayName("should not participate in a session -> not found exception")
    void shouldNotParticipateSessionNotFoundException() {
        when(this.sessionRepository.findById(3L)).thenReturn(Optional.empty());
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(NotFoundException.class, () -> this.sessionService.participate(3L,1L));
    }

    @Test
    @DisplayName("should not participate in a session -> not found exception")
    void shouldNotParticipateUserNotFoundException() {
        when(this.sessionRepository.findById(1L)).thenReturn(Optional.of(firstSession));
        when(this.userRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> this.sessionService.participate(1L,3L));
    }

    @Test
    @DisplayName("should not participate in a session, user already participate -> bad request exception")
    void shouldNotParticipateUserBadRequestException() {
        List<User>users = new ArrayList<>();
        users.add(user);
        firstSession.setUsers(users);

        when(this.sessionRepository.findById(1L)).thenReturn(Optional.of(firstSession));
        when(this.userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> this.sessionService.participate(1L,1L));
    }

    @Test
    @DisplayName("should no longer participate in a session")
    void shouldNoLongerParticipate() {
        List<User>users = new ArrayList<>();
        users.add(user);
        firstSession.setUsers(users);

        when(this.sessionRepository.findById(1L)).thenReturn(Optional.of(firstSession));

        this.sessionService.noLongerParticipate(1L,1L);

        ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);

        verify(this.sessionRepository).save(sessionCaptor.capture());

        Session sessionCaptorValue = sessionCaptor.getValue();

        assertTrue(sessionCaptorValue.getUsers().isEmpty());
        assertEquals(firstSession, sessionCaptorValue);
    }

    @Test
    @DisplayName("should no longer participate in a session -> not found exception")
    void shouldNoLongerParticipateSessionNotFound() {
        when(this.sessionRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> this.sessionService.noLongerParticipate(3L,1L));
    }

    @Test
    @DisplayName("should no longer participate in a session, user is already not participating -> bad request exception")
    void shouldNoLongerParticipateBadRequestException() {
        when(this.sessionRepository.findById(1L)).thenReturn(Optional.of(firstSession));

        assertThrows(BadRequestException.class, () -> this.sessionService.noLongerParticipate(1L,1L));
    }
}