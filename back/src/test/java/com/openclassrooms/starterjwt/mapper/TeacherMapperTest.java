package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class TeacherMapperTest {

    private Teacher teacher;

    private TeacherDto teacherDto;

    @BeforeEach
    void init() {

        teacher = Teacher.builder()
                .id(1L)
                .firstName("firstTeacherFirstName")
                .lastName("firstTeacherLastName")
                .createdAt(LocalDateTime.of(2025, 12, 24, 10, 10))
                .updatedAt(LocalDateTime.of(2025, 12, 24, 10, 10))
                .build();

        teacherDto = new TeacherDto(
                1L,
                "firstTeacherLastName",
                "firstTeacherFirstName",
                LocalDateTime.of(2025, 12, 24, 10, 10),
                LocalDateTime.of(2025, 12, 24, 10, 10));
    }

    private final TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);

    @Test
    @DisplayName("should map teacher Dto to teacher Entity")
    void shouldMapDtoToEntity() {

        Teacher teacherEntity = this.teacherMapper.toEntity(teacherDto);

        assertNotNull(teacherEntity);
        assertEquals(teacherDto.getId(), teacherEntity.getId());
        assertEquals(teacherDto.getFirstName(), teacherEntity.getFirstName());
        assertEquals(teacherDto.getLastName(), teacherEntity.getLastName());

        assertEquals(teacherDto.getCreatedAt(), teacherEntity.getCreatedAt());
        assertEquals(teacherDto.getUpdatedAt(), teacherEntity.getUpdatedAt());
    }

    @Test
    @DisplayName("should map teacher Entity to teacher Dto")
    void shouldEntityToDto() {

        TeacherDto teacherDtoCreated = this.teacherMapper.toDto(teacher);

        assertNotNull(teacherDtoCreated);
        assertEquals(teacher.getId(), teacherDtoCreated.getId());
        assertEquals(teacher.getFirstName(), teacherDtoCreated.getFirstName());
        assertEquals(teacher.getLastName(), teacherDtoCreated.getLastName());
        assertEquals(teacher.getCreatedAt(), teacherDtoCreated.getCreatedAt());
        assertEquals(teacher.getUpdatedAt(), teacherDtoCreated.getUpdatedAt());
    }

    @Test
    @DisplayName("should map list of teacher Dto to list of teacher Entity")
    void shouldMapDtoListToEntityList() {

        TeacherDto otherTeacherDto = new TeacherDto(
                2L,
                "otherTeacherLastName",
                "otherTeacherFirstName",
                LocalDateTime.of(2025, 12, 24, 10, 10),
                LocalDateTime.of(2025, 12, 24, 10, 10));

        List<TeacherDto> teacherDtos = new ArrayList<>();
        teacherDtos.add(teacherDto);
        teacherDtos.add(otherTeacherDto);

        List<Teacher> teacherEntitiesList = this.teacherMapper.toEntity(teacherDtos);

        assertNotNull(teacherEntitiesList);
        assertEquals(2, teacherEntitiesList.size());

        assertEquals(teacherDto.getId(), teacherEntitiesList.get(0).getId());
        assertEquals(teacherDto.getFirstName(), teacherEntitiesList.get(0).getFirstName());
        assertEquals(teacherDto.getLastName(), teacherEntitiesList.get(0).getLastName());
        assertEquals(teacherDto.getCreatedAt(), teacherEntitiesList.get(0).getCreatedAt());
        assertEquals(teacherDto.getUpdatedAt(), teacherEntitiesList.get(0).getUpdatedAt());

        assertEquals(otherTeacherDto.getId(), teacherEntitiesList.get(1).getId());
        assertEquals(otherTeacherDto.getFirstName(), teacherEntitiesList.get(1).getFirstName());
        assertEquals(otherTeacherDto.getLastName(), teacherEntitiesList.get(1).getLastName());
        assertEquals(otherTeacherDto.getCreatedAt(), teacherEntitiesList.get(1).getCreatedAt());
        assertEquals(otherTeacherDto.getUpdatedAt(), teacherEntitiesList.get(1).getUpdatedAt());
    }

    @Test
    @DisplayName("should map list of teacher Entity to list of teacher Dto")
    void shouldMapEntityListToDtoList() {

        Teacher otherTeacher = Teacher.builder()
                .id(2L)
                .firstName("firstTeacherFirstName")
                .lastName("firstTeacherLastName")
                .createdAt(LocalDateTime.of(2025, 12, 24, 10, 10))
                .updatedAt(LocalDateTime.of(2025, 12, 24, 10, 10))
                .build();

        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher);
        teachers.add(otherTeacher);

        List<TeacherDto> teacherDtosList = this.teacherMapper.toDto(teachers);

        assertNotNull(teacherDtosList);
        assertEquals(2, teacherDtosList.size());

        assertEquals(teacher.getId(), teacherDtosList.get(0).getId());
        assertEquals(teacher.getFirstName(), teacherDtosList.get(0).getFirstName());
        assertEquals(teacher.getLastName(), teacherDtosList.get(0).getLastName());
        assertEquals(teacher.getCreatedAt(), teacherDtosList.get(0).getCreatedAt());
        assertEquals(teacher.getUpdatedAt(), teacherDtosList.get(0).getUpdatedAt());

        assertEquals(otherTeacher.getId(), teacherDtosList.get(1).getId());
        assertEquals(otherTeacher.getFirstName(), teacherDtosList.get(1).getFirstName());
        assertEquals(otherTeacher.getLastName(), teacherDtosList.get(1).getLastName());
        assertEquals(otherTeacher.getCreatedAt(), teacherDtosList.get(1).getCreatedAt());
        assertEquals(otherTeacher.getUpdatedAt(), teacherDtosList.get(1).getUpdatedAt());
    }

}