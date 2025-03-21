package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@WithMockUser(username = "user@mail.fr")
public class TeacherControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("should find teacher by its id")
    void shouldFindTeacherById() throws Exception {

        mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.lastName").value("teacherName"))
                .andExpect(jsonPath("$.firstName").value("teacher"))
                .andExpect(jsonPath("$.createdAt").value("2025-03-21T10:00:00"))
                .andExpect(jsonPath("$.updatedAt").value("2025-03-21T10:00:00"));

    }

    @Test
    @DisplayName("should not find teacher by its id -> teacher not found")
    void shouldNotFindTeacherById() throws Exception {

        mockMvc.perform(get("/api/teacher/3"))
                .andExpect(status().isNotFound());

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

        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].lastName").value("teacherName"))
                .andExpect(jsonPath("$[0].firstName").value("teacher"))
                .andExpect(jsonPath("$[0].createdAt").value("2025-03-21T10:00:00"))
                .andExpect(jsonPath("$[0].updatedAt").value("2025-03-21T10:00:00"))
                .andExpect(jsonPath("$[1].lastName").value("otherTeacherName"))
                .andExpect(jsonPath("$[1].firstName").value("otherTeacher"))
                .andExpect(jsonPath("$[1].createdAt").value("2025-03-21T10:00:00"))
                .andExpect(jsonPath("$[1].updatedAt").value("2025-03-21T10:00:00"));

    }
}
