package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthTokenFilterTest {

    @Mock
    JwtUtils jwtUtils;

    @Mock
    UserDetailsServiceImpl userDetailsService;

    @Mock
    FilterChain filterChain;

    @Mock
    SecurityContext securityContext;

    @InjectMocks
    AuthTokenFilter authTokenFilter;

    private UserDetails userDetails;

    @BeforeEach
    void init() {

        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .admin(true)
                .username("user@mail.fr")
                .lastName("userLastName")
                .firstName("userFirstName")
                .build();
    }

    @Test
    @DisplayName("should pass internal filter with a valid token")
    void doFilterInternalWithValidToken() throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer validToken");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtils.validateJwtToken("validToken")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("validToken")).thenReturn("user@mail.fr");
        when(userDetailsService.loadUserByUsername("user@mail.fr")).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);
        doNothing().when(securityContext).setAuthentication(any());
        doNothing().when(filterChain).doFilter(request, response);

        this.authTokenFilter.doFilterInternal(request, response, filterChain);

        ArgumentCaptor<UsernamePasswordAuthenticationToken> authentification = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);

        verify(securityContext).setAuthentication(authentification.capture());

        UsernamePasswordAuthenticationToken authenticationValue = authentification.getValue();

        assertEquals(userDetails, authenticationValue.getPrincipal());

        verify(jwtUtils).validateJwtToken("validToken");
        verify(jwtUtils).getUserNameFromJwtToken("validToken");
        verify(userDetailsService).loadUserByUsername("user@mail.fr");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("should not pass internal filter with invalid token")
    void doFilterInternalWithInvalidToken() throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "validToken");
        MockHttpServletResponse response = new MockHttpServletResponse();

        doNothing().when(filterChain).doFilter(request, response);

        this.authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("should not pass internal filter -> UsernameNotFoundException")
    void doFilterInternalUserNotFoundException() throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer validToken");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtils.validateJwtToken("validToken")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("validToken")).thenReturn("user@mail.fr");
        when(userDetailsService.loadUserByUsername("user@mail.fr")).thenThrow(UsernameNotFoundException.class);
        doNothing().when(filterChain).doFilter(request, response);

        this.authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils).validateJwtToken("validToken");
        verify(jwtUtils).getUserNameFromJwtToken("validToken");
        verify(userDetailsService).loadUserByUsername("user@mail.fr");
        verify(filterChain).doFilter(request, response);
    }


}