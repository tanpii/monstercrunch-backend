package edu.mirea.cookie_shop.service;

import edu.mirea.cookie_shop.dao.entity.CustomerEntity;
import edu.mirea.cookie_shop.dao.repository.CustomerRepository;
import edu.mirea.cookie_shop.dto.Role;
import edu.mirea.cookie_shop.dto.requests.SignInRequest;
import edu.mirea.cookie_shop.dto.requests.SignUpRequest;
import edu.mirea.cookie_shop.dto.response.JwtAuthenticationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {
    @InjectMocks
    private AuthenticationService authenticationService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerService customerService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        // Arrange
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("AuthenticationService#signup test")
    void signup_shouldReturnJwtToken_afterSuccessfulRegister() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest("John", "Doe", "john@example.com", "password");
        CustomerEntity savedCustomer = new CustomerEntity();
        savedCustomer.setEmail(signUpRequest.email());
        when(jwtService.generateToken(any())).thenReturn("dummyToken");
        when(customerService.save(any())).thenReturn(savedCustomer);
        // Act
        JwtAuthenticationResponse response = authenticationService.signup(signUpRequest);
        // Assert
        assertThat(response).isNotNull();
        assertThat(response.token()).isNotEmpty();
    }

    @Test
    @DisplayName("AuthenticationService#signin test")
    void signin_shouldReturnJwtToken_afterSuccessfulAuthentication() {
        // Arrange
        SignInRequest signInRequest = new SignInRequest("john@example.com", "password");
        CustomerEntity user = new CustomerEntity();
        user.setEmail(signInRequest.email());
        user.setPassword("password");
        when(jwtService.generateToken(user)).thenReturn("dummyToken");
        when(customerRepository.findByEmail(signInRequest.email())).thenReturn(java.util.Optional.of(user));
        // Act
        JwtAuthenticationResponse response = authenticationService.signin(signInRequest);
        // Assert
        assertThat(response).isNotNull();
        assertThat(response.token()).isNotEmpty();
    }

    @Test
    @DisplayName("AuthenticationService#signin bad data test")
    void signin_signup_shouldThrowException_whenBadData() {
        // Arrange
        SignInRequest signInRequest = new SignInRequest("john@example.com", "password");
        when(customerRepository.findByEmail(signInRequest.email())).thenReturn(java.util.Optional.empty());
        // Act & Assert
        assertThatThrownBy(() -> authenticationService.signin(signInRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("AuthenticationService#isAuthorized test")
    void isAuthorized_shouldReturnTrue_whenTokenIsNotExpired() {
        // Arrange
        String token = "dummyToken";
        when(jwtService.isTokenExpired(token)).thenReturn(false);
        // Act
        boolean isAuthorized = authenticationService.isAuthorized(token);
        // Assert
        assertThat(isAuthorized).isTrue();
    }

    @Test
    @DisplayName("AuthenticationService#isAdmin admin test")
    void isAdmin_shouldReturnTrue_whenUserIsAdmin() {
        // Arrange
        String token = "dummyToken";
        CustomerEntity adminUser = new CustomerEntity();
        adminUser.setEmail("admin@example.com");
        adminUser.setRole(Role.ROLE_ADMIN);
        when(jwtService.extractUserName(token)).thenReturn(adminUser.getEmail());
        when(customerRepository.findByEmail(adminUser.getEmail())).thenReturn(java.util.Optional.of(adminUser));
        // Act
        boolean isAuthorized = authenticationService.isAdmin(token);
        // Assert
        assertThat(isAuthorized).isTrue();
    }

    @Test
    @DisplayName("AuthenticationService#isAdmin user test")
    void iisAdmin_shouldReturnTrue_whenUserIsNotAdmin() {
        // Arrange
        String token = "dummyToken";
        CustomerEntity regularUser = new CustomerEntity();
        regularUser.setEmail("user@example.com");
        regularUser.setRole(Role.ROLE_USER);
        when(jwtService.extractUserName(token)).thenReturn(regularUser.getEmail());
        when(customerRepository.findByEmail(regularUser.getEmail())).thenReturn(java.util.Optional.of(regularUser));
        // Act
        boolean isAdmin = authenticationService.isAdmin(token);
        // Assert
        assertThat(isAdmin).isFalse();
    }
}
