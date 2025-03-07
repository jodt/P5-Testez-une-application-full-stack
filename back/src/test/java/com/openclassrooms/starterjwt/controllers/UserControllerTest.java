package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserMapper userMapper;

    @Mock
    UserService userService;

    @Mock
    SecurityContext securityContext;

    @Mock
    Authentication authentication;

    @InjectMocks
    UserController userController;

    private User user;

    private UserDetailsImpl userDetails;

    @BeforeEach
    void init() {

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

        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .admin(true)
                .username("user@mail.fr")
                .lastName("userLastName")
                .firstName("userFirstName")
                .build();

    }


    @Test
    @DisplayName("should find user by its id")
    void shouldFindUserById() {

        UserDto userDto = new UserDto(
                1L, "user@mail.com",
                "userLastName",
                "userFirstName",
                true,
                "password",
                LocalDateTime.now(),
                LocalDateTime.now());


        when(this.userService.findById(1L)).thenReturn(user);
        when(this.userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<?> response = this.userController.findById("1");

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.hasBody());
        assertEquals(userDto, response.getBody());

        verify(this.userService).findById(1L);
        verify(this.userMapper).toDto(user);
    }

    @Test
    @DisplayName("should not find user by its id -> user not found")
    void shouldNotFindUserById() throws Exception {

        when(this.userService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = this.userController.findById("1");

        assertEquals(404, response.getStatusCodeValue());
        assertFalse(response.hasBody());

        verify(this.userService).findById(1L);
    }

    @Test
    @DisplayName("should not find user by its id -> bad request")
    void shouldNotFindUserByIdBadRequest() throws Exception {

        ResponseEntity<?> response = this.userController.findById("A");

        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.hasBody());
    }


    @Test
    @DisplayName("should delete user")
    void shouldDeleteUserByItsId() {

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(this.userService.findById(1L)).thenReturn(user);

        doNothing().when(this.userService).delete(1L);

        ResponseEntity<?> response = this.userController.save("1");

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.hasBody());

        verify(authentication).getPrincipal();
        verify(securityContext).getAuthentication();
    }

    @Test
    @DisplayName("should not delete user -> user not found")
    void shouldNotDeleteUserByItsIdUserNotFound() {

        when(this.userService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = this.userController.save("1");

        assertEquals(404, response.getStatusCodeValue());
        assertFalse(response.hasBody());
    }

    @Test
    @DisplayName("should not delete user -> unauthorized")
    void shouldNotDeleteUserByItsIdUnauthorized() {

        user.setEmail("badMail@mail.com");

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(this.userService.findById(1L)).thenReturn(user);

        ResponseEntity<?> response = this.userController.save("1");

        assertEquals(401, response.getStatusCodeValue());
        assertFalse(response.hasBody());

        verify(authentication).getPrincipal();
        verify(securityContext).getAuthentication();
    }

    @Test
    @DisplayName("should delete user by its id -> bad request")
    void shouldNotDeleteUserByIdBadRequest() throws Exception {

        ResponseEntity<?> response = this.userController.findById("A");

        assertEquals(400, response.getStatusCodeValue());
        assertFalse(response.hasBody());
    }
}