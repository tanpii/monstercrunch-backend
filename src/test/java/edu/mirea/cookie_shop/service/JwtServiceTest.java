package edu.mirea.cookie_shop.service;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JwtServiceTest {
    private JwtService jwtService;
    private UserDetails userDetails;
    private String token;

    @BeforeEach
    void setUp() {
        // Arrange
        jwtService = new JwtService("48a868a4042f634ac04a117f00a87202131dd7c46c4b32c4acb3edc5e15f4511",
                3600000L);
        userDetails = createTestUser();
        // Act
        token = jwtService.generateToken(userDetails);
    }

    private UserDetails createTestUser() {
        String username = "testUser";
        String password = "testPassword";
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new User(username, password, authorities);
    }

    @Test
    @DisplayName("JwtService#generateToken test")
    public void generateToken_shouldReturnNewJwtToken() {
        // Assert
        assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("JwtService#extractUserName test")
    public void extractUserName_shouldReturnCorrectUserName() {
        //Act
        String extractedUserName = jwtService.extractUserName(token);
        // Assert
        assertThat(extractedUserName).isEqualTo("testUser");
    }

    @Test
    @DisplayName("JwtService#isTokenValid test")
    public void isTokenValid_shouldReturnTrue_whenTokenIsNotExpiredAndUsernameIsCorrect() {
        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);
        // Assert
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("JwtService#isTokenExpired test")
    public void isTokenExpired_shouldReturnFalse_whenTokenIsNotExpired() {
        // Act
        boolean isExpired = jwtService.isTokenExpired(token);
        // Assert
        assertThat(isExpired).isFalse();
    }

    @Test
    @DisplayName("JwtService#isTokenValid incorrect username test")
    public void isTokenValid_shouldReturnFalse_whenUsernameIncorrect() {
        // Arrange
        UserDetails invalidUserDetails = new User("invalidUsername", "password", new ArrayList<>());
        // Act
        boolean isValid = jwtService.isTokenValid(token, invalidUserDetails);
        // Assert
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("JwtService#extractUsername invalid token test")
    public void extractUserName_shouldThrowException_whenTokenFormatIsInvalid() {
        // Act & Assert
        assertThatThrownBy(() -> jwtService.extractUserName("invalidToken"))
                .isInstanceOf(MalformedJwtException.class);
    }

    @Test
    @DisplayName("JwtService#isTokenValid tampered token test")
    public void isTokenValid_shouldThrowException_whenTokenIsTampered() {
        // Arrange
        String tamperedToken = token + "tampered";
        // Act & Assert
        assertThatThrownBy(() -> jwtService.isTokenValid(tamperedToken, userDetails))
                .isInstanceOf(SignatureException.class);
    }

    @Test
    @DisplayName("JwtService#isTokenExpired invalid token test")
    public void isTokenExpired_shouldThrowException_whenTokenFormatIsInvalid() {
        // Act & Assert
        assertThatThrownBy(() -> jwtService.isTokenExpired("invalidToken"))
                .isInstanceOf(MalformedJwtException.class);
    }
}
