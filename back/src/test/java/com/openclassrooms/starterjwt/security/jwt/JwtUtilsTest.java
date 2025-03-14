package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    @Mock
    Authentication authentication;

    private UserDetailsImpl userDetails;

    private String jwtSecret = "secret";

    private int jwtExpirationMs = 86400000;

    @InjectMocks
    JwtUtils jwtUtils;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);

        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .admin(true)
                .username("user@mail.fr")
                .lastName("userLastName")
                .firstName("userFirstName")
                .build();
    }

    @Test
    @DisplayName("Should generate token")
    void shouldGenerateJwtToken() {

        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Should get userName from token")
    void shouldGetUserNameFromJwtToken() {

        String token = Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        String userName = this.jwtUtils.getUserNameFromJwtToken(token);

        assertFalse(userName.isEmpty());
        assertEquals("user@mail.fr", userName);

    }

    @Test
    @DisplayName("should validate token")
    void shouldValidateJwtToken() {

        String token = Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        boolean result = this.jwtUtils.validateJwtToken(token);

        assertTrue(result);
    }

    @Test
    @DisplayName("should not validate token -> bad secret key")
    void shouldNotValidateJwtTokenBadSecretKey() {

        String token = Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, "BadSecretKey")
                .compact();

        boolean result = this.jwtUtils.validateJwtToken(token);

        assertFalse(result);
    }

    @Test
    @DisplayName("should not validate token -> token expired")
    void shouldNotValidateJwtTokenExpired() {

        String token = Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() - 360000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        boolean result = this.jwtUtils.validateJwtToken(token);

        assertFalse(result);
    }

    @Test
    @DisplayName("should not validate token -> malFormedToken")
    void shouldNotValidateJwtMalformedToken() {

        String token = "token";

        boolean result = this.jwtUtils.validateJwtToken(token);

        assertFalse(result);
    }

    @Test
    @DisplayName("should not validate token -> IllegalArgumentException")
    void shouldNotValidateJwtIllegalArgumentException() {

        String token = "";

        boolean result = this.jwtUtils.validateJwtToken(token);

        assertFalse(result);
    }

}