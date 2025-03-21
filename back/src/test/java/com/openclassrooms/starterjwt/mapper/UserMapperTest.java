package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserMapperTest {

    private User user;

    private UserDto userDto;

    @BeforeEach
    void init() {

        user = User.builder()
                .id(1L)
                .email("firstUserMail@mail.com")
                .password("firstUserPassword")
                .firstName("firstUserFirstName")
                .lastName("firstUserLastName")
                .admin(true)
                .createdAt(LocalDateTime.of(2025, 12, 24, 10, 10))
                .updatedAt(LocalDateTime.of(2025, 12, 24, 10, 10))
                .build();

        userDto = new UserDto(
                1L,
                "firstUserMail@mail.com",
                "firstUserLastName",
                "firstUserFirstName",
                true,
                "firstUserPassword",
                LocalDateTime.of(2025, 12, 24, 10, 10),
                LocalDateTime.of(2025, 12, 24, 10, 10));
    }

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    @DisplayName("should map user Dto to user Entity")
    void shouldMapDtoToEntity() {

        User userEntity = this.userMapper.toEntity(userDto);

        assertNotNull(userEntity);
        assertEquals(userDto.getId(), userEntity.getId());
        assertEquals(userDto.getPassword(), userEntity.getPassword());
        assertEquals(userDto.isAdmin(), userEntity.isAdmin());
        assertEquals(userDto.getEmail(), userEntity.getEmail());
        assertEquals(userDto.getFirstName(), userEntity.getFirstName());
        assertEquals(userDto.getLastName(), userEntity.getLastName());
        assertEquals(userDto.getCreatedAt(), userEntity.getCreatedAt());
        assertEquals(userDto.getUpdatedAt(), userEntity.getUpdatedAt());
    }

    @Test
    @DisplayName("should map user Entity to user Dto")
    void shouldEntityToDto() {

        UserDto userDtoCreated = this.userMapper.toDto(user);

        assertNotNull(userDtoCreated);
        assertEquals(user.getId(), userDtoCreated.getId());
        assertEquals(user.getPassword(), userDtoCreated.getPassword());
        assertEquals(user.isAdmin(), userDtoCreated.isAdmin());
        assertEquals(user.getEmail(), userDtoCreated.getEmail());
        assertEquals(user.getFirstName(), userDtoCreated.getFirstName());
        assertEquals(user.getLastName(), userDtoCreated.getLastName());
        assertEquals(user.getCreatedAt(), userDtoCreated.getCreatedAt());
        assertEquals(user.getUpdatedAt(), userDtoCreated.getUpdatedAt());
    }

    @Test
    @DisplayName("should map list of user Dto to list of user Entity")
    void shouldMapDtoListToEntityList() {

        UserDto otherUserDto = new UserDto(
                2L,
                "otherUserMail@mail.com",
                "otherUserLastName",
                "otherUserFirstName",
                true,
                "otherUserPassword",
                LocalDateTime.of(2025, 12, 24, 10, 10),
                LocalDateTime.of(2025, 12, 24, 10, 10));

        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(userDto);
        userDtos.add(otherUserDto);

        List<User> userEntitiesList = this.userMapper.toEntity(userDtos);

        assertNotNull(userEntitiesList);
        assertEquals(2, userEntitiesList.size());

        assertEquals(userDto.getId(), userEntitiesList.get(0).getId());
        assertEquals(userDto.getFirstName(), userEntitiesList.get(0).getFirstName());
        assertEquals(userDto.getLastName(), userEntitiesList.get(0).getLastName());
        assertEquals(userDto.getEmail(), userEntitiesList.get(0).getEmail());
        assertEquals(userDto.isAdmin(), userEntitiesList.get(0).isAdmin());
        assertEquals(userDto.getPassword(), userEntitiesList.get(0).getPassword());
        assertEquals(userDto.getCreatedAt(), userEntitiesList.get(0).getCreatedAt());
        assertEquals(userDto.getUpdatedAt(), userEntitiesList.get(0).getUpdatedAt());

        assertEquals(otherUserDto.getId(), userEntitiesList.get(1).getId());
        assertEquals(otherUserDto.getFirstName(), userEntitiesList.get(1).getFirstName());
        assertEquals(otherUserDto.getLastName(), userEntitiesList.get(1).getLastName());
        assertEquals(otherUserDto.getEmail(), userEntitiesList.get(1).getEmail());
        assertEquals(otherUserDto.isAdmin(), userEntitiesList.get(1).isAdmin());
        assertEquals(otherUserDto.getPassword(), userEntitiesList.get(1).getPassword());
        assertEquals(otherUserDto.getCreatedAt(), userEntitiesList.get(1).getCreatedAt());
        assertEquals(otherUserDto.getUpdatedAt(), userEntitiesList.get(1).getUpdatedAt());
    }

    @Test
    @DisplayName("should map list of user Entity to list of user Dto")
    void shouldMapEntityListToDtoList() {

        User otherUser = User.builder()
                .id(2L)
                .firstName("otherUserFirstName")
                .lastName("otherUserLastName")
                .admin(true)
                .password("otherUserPassword")
                .email("otherUserMail@mail.com")
                .createdAt(LocalDateTime.of(2025, 12, 24, 10, 10))
                .updatedAt(LocalDateTime.of(2025, 12, 24, 10, 10))
                .build();

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(otherUser);

        List<UserDto> userDtosList = this.userMapper.toDto(users);

        assertNotNull(userDtosList);
        assertEquals(2, userDtosList.size());

        assertEquals(user.getId(), userDtosList.get(0).getId());
        assertEquals(user.getFirstName(), userDtosList.get(0).getFirstName());
        assertEquals(user.getLastName(), userDtosList.get(0).getLastName());
        assertEquals(user.getEmail(), userDtosList.get(0).getEmail());
        assertEquals(user.isAdmin(), userDtosList.get(0).isAdmin());
        assertEquals(user.getPassword(), userDtosList.get(0).getPassword());
        assertEquals(user.getCreatedAt(), userDtosList.get(0).getCreatedAt());
        assertEquals(user.getUpdatedAt(), userDtosList.get(0).getUpdatedAt());

        assertEquals(otherUser.getId(), userDtosList.get(1).getId());
        assertEquals(otherUser.getFirstName(), userDtosList.get(1).getFirstName());
        assertEquals(otherUser.getLastName(), userDtosList.get(1).getLastName());
        assertEquals(otherUser.getEmail(), userDtosList.get(1).getEmail());
        assertEquals(otherUser.isAdmin(), userDtosList.get(1).isAdmin());
        assertEquals(otherUser.getPassword(), userDtosList.get(1).getPassword());
        assertEquals(otherUser.getCreatedAt(), userDtosList.get(1).getCreatedAt());
        assertEquals(otherUser.getUpdatedAt(), userDtosList.get(1).getUpdatedAt());
    }

}