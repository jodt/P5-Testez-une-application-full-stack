package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionMapperTest {

    @Mock
    TeacherService teacherService;

    @Mock
    UserService userService;

    @InjectMocks
    private SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);

    private Session session;

    private SessionDto sessionDto;

    private User user;

    private Teacher teacher;


    @BeforeEach
    void init() {

        teacher = Teacher.builder()
                .id(1L)
                .firstName("firstTeacherFirstName")
                .lastName("firstTeacherLastName")
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

        session = Session.builder()
                .id(1l)
                .date(new Date(2026, Calendar.JANUARY, 10))
                .name("yoga")
                .description("yoga for beginners")
                .teacher(teacher)
                .users(new ArrayList<>())
                .updatedAt(LocalDateTime.of(2025, 12, 24, 10, 10))
                .createdAt(LocalDateTime.of(2025, 12, 24, 10, 10))
                .build();

        sessionDto = new SessionDto(
                1L,
                "yoga",
                new Date(2026, Calendar.JANUARY, 10),
                1L,
                "yoga for beginners",
                new ArrayList<>(),
                LocalDateTime.of(2025, 12, 24, 10, 10),
                LocalDateTime.of(2025, 12, 24, 10, 10)
        );
    }

    @Test
    @DisplayName("should map session Dto without participating user to session Entity")
    void shouldMapDtoToEntityWithoutParticipatingUser() {

        Long teacherId = sessionDto.getTeacher_id();
        when(teacherService.findById(teacherId)).thenReturn(teacher);

        Session sessionEntity = this.sessionMapper.toEntity(sessionDto);

        assertNotNull(sessionEntity);
        assertEquals(sessionDto.getId(), sessionEntity.getId());
        assertEquals(sessionDto.getName(), sessionEntity.getName());
        assertEquals(sessionDto.getDate(), sessionEntity.getDate());
        assertEquals(teacher, sessionEntity.getTeacher());
        assertEquals(sessionDto.getDescription(), sessionEntity.getDescription());

        assertTrue(sessionEntity.getUsers().isEmpty());
        assertEquals(sessionDto.getCreatedAt(), sessionEntity.getCreatedAt());
        assertEquals(sessionDto.getUpdatedAt(), sessionEntity.getUpdatedAt());

        verify(this.teacherService).findById(sessionDto.getTeacher_id());
    }

    @Test
    @DisplayName("should map session Dto with participating user to session Entity")
    void shouldMapDtoToEntityWithParticipatingUser() {

        sessionDto.setUsers(Collections.singletonList(1L));
        sessionDto.setTeacher_id(1L);

        Long teacherId = sessionDto.getTeacher_id();
        when(teacherService.findById(teacherId)).thenReturn(teacher);

        Long userId = sessionDto.getUsers().get(0);
        when(userService.findById(userId)).thenReturn(user);

        Session sessionEntity = this.sessionMapper.toEntity(sessionDto);

        assertNotNull(sessionEntity);
        assertEquals(sessionDto.getId(), sessionEntity.getId());
        assertEquals(sessionDto.getName(), sessionEntity.getName());
        assertEquals(sessionDto.getDate(), sessionEntity.getDate());
        assertEquals(teacher, sessionEntity.getTeacher());
        assertEquals(sessionDto.getDescription(), sessionEntity.getDescription());

        assertEquals(1, sessionEntity.getUsers().size());
        assertEquals(user, sessionEntity.getUsers().get(0));
        assertEquals(sessionDto.getCreatedAt(), sessionEntity.getCreatedAt());
        assertEquals(sessionDto.getUpdatedAt(), sessionEntity.getUpdatedAt());

        verify(this.teacherService).findById(sessionDto.getTeacher_id());
        verify(this.userService).findById(sessionDto.getUsers().get(0));
    }

    @Test
    @DisplayName("should map list of session Dto without participating user to list of session Entity")
    void shouldMapDtoListToEntityListWithoutParticipatingUser() {

        SessionDto otherSessionDto = new SessionDto(
                2L,
                "relaxation",
                new Date(2026, Calendar.JANUARY, 30),
                1L,
                "relaxation for beginners",
                new ArrayList<>(),
                LocalDateTime.of(2025, 12, 24, 10, 10),
                LocalDateTime.of(2025, 12, 24, 10, 10));

        Long teacherId = sessionDto.getTeacher_id();
        when(teacherService.findById(teacherId)).thenReturn(teacher);

        List<SessionDto> sessionDtosList = new ArrayList<>();
        sessionDtosList.add(sessionDto);
        sessionDtosList.add(otherSessionDto);

        List<Session> sessionEntitiesList = this.sessionMapper.toEntity(sessionDtosList);

        assertNotNull(sessionEntitiesList);
        assertEquals(2, sessionEntitiesList.size());

        assertEquals(sessionDto.getId(), sessionEntitiesList.get(0).getId());
        assertEquals(sessionDto.getName(), sessionEntitiesList.get(0).getName());
        assertEquals(sessionDto.getDate(), sessionEntitiesList.get(0).getDate());
        assertEquals(teacher, sessionEntitiesList.get(0).getTeacher());
        assertEquals(sessionDto.getDescription(), sessionEntitiesList.get(0).getDescription());
        assertTrue(sessionEntitiesList.get(0).getUsers().isEmpty());
        assertEquals(sessionDto.getCreatedAt(), sessionEntitiesList.get(0).getCreatedAt());
        assertEquals(sessionDto.getUpdatedAt(), sessionEntitiesList.get(0).getUpdatedAt());

        assertEquals(otherSessionDto.getId(), sessionEntitiesList.get(1).getId());
        assertEquals(otherSessionDto.getName(), sessionEntitiesList.get(1).getName());
        assertEquals(otherSessionDto.getDate(), sessionEntitiesList.get(1).getDate());
        assertEquals(teacher, sessionEntitiesList.get(1).getTeacher());
        assertEquals(otherSessionDto.getDescription(), sessionEntitiesList.get(1).getDescription());
        assertTrue(sessionEntitiesList.get(1).getUsers().isEmpty());
        assertEquals(otherSessionDto.getCreatedAt(), sessionEntitiesList.get(1).getCreatedAt());
        assertEquals(otherSessionDto.getUpdatedAt(), sessionEntitiesList.get(1).getUpdatedAt());

        verify(this.teacherService, times(2)).findById(sessionDto.getTeacher_id());
    }

    @Test
    @DisplayName("Should map session Entity without participating user to session Dto")
    void shouldMapEntityToDtoWithoutParticipatingUser() {

        SessionDto sessionDtoCreated = this.sessionMapper.toDto(session);

        assertNotNull(sessionDtoCreated);
        assertEquals(session.getName(), sessionDtoCreated.getName());
        assertEquals(session.getDate(), sessionDtoCreated.getDate());
        assertEquals(session.getTeacher().getId(), sessionDtoCreated.getTeacher_id());
        assertEquals(session.getDescription(), sessionDtoCreated.getDescription());

        assertTrue(sessionDtoCreated.getUsers().isEmpty());
        assertEquals(session.getCreatedAt(), sessionDtoCreated.getCreatedAt());
        assertEquals(session.getUpdatedAt(), sessionDtoCreated.getUpdatedAt());
    }

    @Test
    @DisplayName("Should map session Entity with participating user to session Dto")
    void shouldMapEntityToDtoWithParticipatingUser() {
        List<User> users = new ArrayList<>();
        users.add(user);

        session.setUsers(users);
        session.setTeacher(teacher);

        SessionDto sessionDtoCreated = this.sessionMapper.toDto(session);

        assertNotNull(sessionDtoCreated);
        assertEquals(session.getName(), sessionDtoCreated.getName());
        assertEquals(session.getDate(), sessionDtoCreated.getDate());
        assertEquals(session.getTeacher().getId(), sessionDtoCreated.getTeacher_id());
        assertEquals(session.getDescription(), sessionDtoCreated.getDescription());

        assertEquals(1, sessionDtoCreated.getUsers().size());
        assertEquals(session.getUsers().get(0).getId(), sessionDtoCreated.getUsers().get(0));
        assertEquals(session.getCreatedAt(), sessionDtoCreated.getCreatedAt());
        assertEquals(session.getUpdatedAt(), sessionDtoCreated.getUpdatedAt());
    }

    @Test
    @DisplayName("should map list of session Entity without participating user to list of session Dto")
    void shouldMapEntityListToDtoListWithoutParticipatingUser() {

        Session otherSession = Session.builder()
                .id(2L)
                .date(new Date(2026, Calendar.JANUARY, 30))
                .name("yoga")
                .description("yoga for beginners")
                .teacher(teacher)
                .users(new ArrayList<>())
                .updatedAt(LocalDateTime.of(2025, 12, 24, 10, 10))
                .createdAt(LocalDateTime.of(2025, 12, 24, 10, 10))
                .build();

        List<Session> sessionEntitiesList = new ArrayList<>();
        sessionEntitiesList.add(session);
        sessionEntitiesList.add(otherSession);

        List<SessionDto> sessionDtosList = this.sessionMapper.toDto(sessionEntitiesList);

        assertNotNull(sessionDtosList);
        assertEquals(2, sessionDtosList.size());

        assertEquals(session.getId(), sessionDtosList.get(0).getId());
        assertEquals(session.getName(), sessionDtosList.get(0).getName());
        assertEquals(session.getDate(), sessionDtosList.get(0).getDate());
        assertEquals(session.getTeacher().getId(), sessionDtosList.get(0).getTeacher_id());
        assertEquals(session.getDescription(), sessionDtosList.get(0).getDescription());
        assertTrue(sessionDtosList.get(0).getUsers().isEmpty());
        assertEquals(session.getCreatedAt(), sessionDtosList.get(0).getCreatedAt());
        assertEquals(session.getUpdatedAt(), sessionDtosList.get(0).getUpdatedAt());

        assertEquals(otherSession.getId(), sessionDtosList.get(1).getId());
        assertEquals(otherSession.getName(), sessionDtosList.get(1).getName());
        assertEquals(otherSession.getDate(), sessionDtosList.get(1).getDate());
        assertEquals(otherSession.getTeacher().getId(), sessionDtosList.get(1).getTeacher_id());
        assertEquals(otherSession.getDescription(), sessionDtosList.get(1).getDescription());
        assertTrue(sessionDtosList.get(1).getUsers().isEmpty());
        assertEquals(otherSession.getCreatedAt(), sessionDtosList.get(1).getCreatedAt());
        assertEquals(otherSession.getUpdatedAt(), sessionDtosList.get(1).getUpdatedAt());
    }

}