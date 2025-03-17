package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
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
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user@mail.fr")
public class TeacherControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TeacherService teacherService;

    @MockBean
    TeacherRepository teacherRepository;

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
    void shouldFindTeacherById() throws Exception {

        when(this.teacherRepository.findById(1L)).thenReturn(Optional.of(firstTeacher));

        mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.lastName").value("firstTeacherLastName"))
                .andExpect(jsonPath("$.firstName").value("firstTeacherFirstName"));

        verify(this.teacherRepository).findById(1L);
    }

    @Test
    @DisplayName("should not find teacher by its id -> teacher not found")
    void shouldNotFindTeacherById() throws Exception {

        when(this.teacherRepository.findById(3L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/teacher/3"))
                .andExpect(status().isNotFound());

        verify(this.teacherRepository).findById(3L);
    }

    @Test
    @DisplayName("should not find teacher by its id -> bad request")
    void shouldNotFindTeacherByIdBadRequest() throws Exception {

        mockMvc.perform(get("/api/teacher/A"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should find all teachers")
    void shouldFindAllTeachers() throws Exception {

        List<Teacher> teachers = new ArrayList<>();
        teachers.add(firstTeacher);
        teachers.add(secondTeacher);

        when(this.teacherRepository.findAll()).thenReturn(teachers);

        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].lastName").value("firstTeacherLastName"))
                .andExpect(jsonPath("$[0].firstName").value("firstTeacherFirstName"))
                .andExpect(jsonPath("$[1].lastName").value("secondTeacherLastName"))
                .andExpect(jsonPath("$[1].firstName").value("secondTeacherFirstName"));

        verify(this.teacherRepository).findAll();
    }
}
