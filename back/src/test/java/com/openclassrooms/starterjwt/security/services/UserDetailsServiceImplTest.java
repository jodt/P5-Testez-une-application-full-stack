package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Test
    void shouldLoadUserByUsername() {

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

        when(this.userRepository.findByEmail("user@mail.fr")).thenReturn(Optional.of(user));

        UserDetails userDetails = this.userDetailsService.loadUserByUsername("user@mail.fr");

        assertNotNull(userDetails);
        assertEquals(user.getId(), ((UserDetailsImpl) userDetails).getId());
        assertEquals(user.getEmail(), userDetails.getUsername());
        assertEquals(user.getFirstName(), ((UserDetailsImpl) userDetails).getFirstName());
        assertEquals(user.getLastName(), ((UserDetailsImpl) userDetails).getLastName());
        assertEquals(user.getPassword(), userDetails.getPassword());

        verify(this.userRepository).findByEmail("user@mail.fr");
    }

    @Test
    void shouldNotLoadUserByUsername() {

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

        when(this.userRepository.findByEmail("user@mail.fr")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> this.userDetailsService.loadUserByUsername("user@mail.fr"));

        verify(this.userRepository).findByEmail("user@mail.fr");
    }
}