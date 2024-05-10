package edu.mirea.cookie_shop.service;

import edu.mirea.cookie_shop.dao.entity.CustomerEntity;
import edu.mirea.cookie_shop.dao.repository.CustomerRepository;
import edu.mirea.cookie_shop.dto.CustomerData;
import edu.mirea.cookie_shop.exception.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CustomerServiceTest {
    @InjectMocks
    private CustomerService customerService;
    @Mock
    private CustomerRepository customerRepository;
    private CustomerEntity customer;

    @BeforeEach
    void setUp() {
        // Arrange
        MockitoAnnotations.openMocks(this);
        customer = new CustomerEntity();
        customer.setCustomerName("John");
        customer.setCustomerSurname("Doe");
        customer.setEmail("john.doe@example.com");
    }

    @Test
    @DisplayName("CustomerService#userDetailsService test")
    void userDetailsService_shouldReturnCorrectUserDetails() {
        // Arrange
        when(customerRepository.findByEmail(any())).thenReturn(Optional.of(customer));
        // Act
        UserDetails userDetails = customerService.userDetailsService().loadUserByUsername("john.doe@example.com");
        // Assert
        assertThat(userDetails.getUsername()).isEqualTo("john.doe@example.com");
    }

    @Test
    @DisplayName("CustomerService#userDetailsService bad username test")
    void userDetailsService_shouldThrowException_whenUserNotFound() {
        // Arrange
        when(customerRepository.findByEmail(any())).thenReturn(Optional.empty());
        // Act & Assert
        assertThatThrownBy(() -> customerService.userDetailsService().loadUserByUsername("nonexistent@example.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    @DisplayName("CustomerService#saveCustomer test")
    void saveCustomer_shouldReturnSavedCustomer_whenCustomerWasSaved() {
        // Arrange
        when(customerRepository.save(any())).thenReturn(customer);
        // Act
        CustomerEntity savedCustomer = customerService.save(customer);
        // Assert
        assertThat(savedCustomer).isEqualTo(customer);
    }

    @Test
    @DisplayName("CustomerService#getCustomer test")
    void getCustomer_shouldReturnCustomerData() {
        // Arrange
        when(customerRepository.findByEmail(any())).thenReturn(Optional.of(customer));
        // Act
        CustomerData customerData = customerService.getCustomer("john.doe@example.com");
        // Assert
        assertThat(customerData.customerName()).isEqualTo("John");
        assertThat(customerData.customerSurname()).isEqualTo("Doe");
        assertThat(customerData.email()).isEqualTo("john.doe@example.com");
    }

    @Test
    @DisplayName("CustomerService#getCustomer bad email test")
    void getCustomer_shouldThrowException_whenCustomerNotFound() {
        // Arrange
        when(customerRepository.findByEmail(any())).thenReturn(Optional.empty());
        // Act & Assert
        assertThatThrownBy(() -> customerService.getCustomer("nonexistent@example.com"))
                .isInstanceOf(CustomerNotFoundException.class);
    }
}
