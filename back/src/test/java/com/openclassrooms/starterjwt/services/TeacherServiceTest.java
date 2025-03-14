package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    TeacherRepository teacherRepository;

    @InjectMocks
    TeacherService teacherService;

    private Teacher firstTeacher;
    private Teacher secondTeacher;

    @BeforeEach
    void init() {
        firstTeacher = Teacher.builder()
                .id(1L)
                .firstName("firstTeacherFirstName")
                .lastName("firstTeacherLastName")
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        secondTeacher = Teacher.builder()
                .id(2L)
                .firstName("firstTeacherFirstName")
                .lastName("firstTeacherLastName")
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("should find all teachers")
    void shouldFindAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(firstTeacher);
        teachers.add(secondTeacher);

        when(this.teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> teachersFound = this.teacherService.findAll();

        assertNotNull(teachersFound);
        assertEquals(2, teachersFound.size());
        assertEquals(firstTeacher, teachersFound.get(0));
        assertEquals(secondTeacher, teachersFound.get(1));

        verify(this.teacherRepository).findAll();
    }

    @Test
    @DisplayName("should find teacher by its id")
    void shouldFindTeacherById() {
        when(this.teacherRepository.findById(1L)).thenReturn(Optional.of(firstTeacher));

        Teacher teacherFound = this.teacherService.findById(1L);

        assertNotNull(teacherFound);
        assertEquals(firstTeacher, teacherFound);

        verify(this.teacherRepository).findById(1L);
    }

    @Test
    @DisplayName("should not find teacher by its id")
    void shouldNotFindTeacherById() {
        when(this.teacherRepository.findById(3L)).thenReturn(Optional.empty());

        Teacher teacherFound = this.teacherService.findById(3L);

        assertNull(teacherFound);

        verify(this.teacherRepository).findById(3L);
    }
}