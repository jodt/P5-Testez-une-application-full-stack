package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("Should delete user")
    void shouldDeleteUser() {
        doNothing().when(this.userRepository).deleteById(anyLong());
        userService.delete(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should find user by id")
    void shouldFindById() {

        User user = User.builder()
                .id(1L)
                .admin(true)
                .password("password")
                .email("user@mail.fr")
                .firstName("userFirstName")
                .lastName("userLastName")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(this.userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User userFound = this.userService.findById(1L);

        assertEquals(user, userFound);
        verify(this.userRepository).findById(1L);
    }

    @Test
    @DisplayName("Should not find user by id")
    void shouldNotFindById() {

        when(this.userRepository.findById(anyLong())).thenReturn(Optional.empty());

        User userFound = this.userService.findById(1L);

        assertNull(userFound);
        verify(this.userRepository).findById(1L);
    }
}