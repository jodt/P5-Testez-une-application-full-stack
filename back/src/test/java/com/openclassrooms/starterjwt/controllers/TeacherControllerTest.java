package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    @Mock
    TeacherService teacherService;

    @Mock
    TeacherMapper teacherMapper;

    @InjectMocks
    TeacherController teacherController;

    private Teacher firstTeacher;

    private Teacher secondTeacher;

    private TeacherDto firstTeacherDto;

    private TeacherDto secondTeacherDto;

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
                .firstName("secondTeacherFirstName")
                .lastName("secondTeacherLastName")
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        firstTeacherDto = new TeacherDto(
                1L,
                "firstTeacherLastName",
                "firstTeacherFirstName",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        secondTeacherDto = new TeacherDto(
                2L,
                "secondTeacherLastName",
                "secondTeacherFirstName",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

    }


    @Test
    @DisplayName("should find teacher by its id")
    void shouldFindTeacherById() {

        when(this.teacherService.findById(1L)).thenReturn(firstTeacher);
        when(this.teacherMapper.toDto(firstTeacher)).thenReturn(firstTeacherDto);

        ResponseEntity<?> response = this.teacherController.findById("1");

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.hasBody());
        assertEquals(firstTeacherDto, response.getBody());

        verify(this.teacherService).findById(1L);
        verify(this.teacherMapper).toDto(firstTeacher);
    }

    @Test
    @DisplayName("should not find teacher by its id -> teacher not found")
    void shouldNotFindTeacherById() {

        when(this.teacherService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = this.teacherController.findById("1");

        assertEquals(404, response.getStatusCodeValue());
        assertFalse(response.hasBody());

        verify(this.teacherService).findById(1L);
    }

    @Test
    @DisplayName("should not find teacher by its id -> bad request")
    void shouldNotFindTeacherByIdBadRequest() {

        ResponseEntity<?> response = this.teacherController.findById("A");

        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.hasBody());

    }

    @Test
    @DisplayName("should find all teachers")
    void shouldFindAllTeachers() {
        List<Teacher> teachersFound = new ArrayList<>();
        teachersFound.add(firstTeacher);
        teachersFound.add(secondTeacher);

        List<TeacherDto> teachersFoundDto = new ArrayList<>();
        teachersFoundDto.add(firstTeacherDto);
        teachersFoundDto.add(secondTeacherDto);

        when(this.teacherService.findAll()).thenReturn(teachersFound);
        when(this.teacherMapper.toDto(teachersFound)).thenReturn(teachersFoundDto);

        ResponseEntity<?> response = this.teacherController.findAll();

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.hasBody());
        assertEquals(2, ((List<TeacherDto>) response.getBody()).size());
        assertEquals(firstTeacherDto, ((List<TeacherDto>) response.getBody()).get(0));
        assertEquals(secondTeacherDto, ((List<TeacherDto>) response.getBody()).get(1));

        verify(this.teacherService).findAll();
        verify(this.teacherMapper).toDto(teachersFound);
    }
}