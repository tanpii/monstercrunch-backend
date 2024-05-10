package edu.mirea.cookie_shop.service;

import edu.mirea.cookie_shop.dao.entity.CartEntity;
import edu.mirea.cookie_shop.dao.entity.CustomerEntity;
import edu.mirea.cookie_shop.dao.entity.OrderEntity;
import edu.mirea.cookie_shop.dao.repository.CartProductRepository;
import edu.mirea.cookie_shop.dao.repository.CustomerRepository;
import edu.mirea.cookie_shop.dto.OrderDto;
import edu.mirea.cookie_shop.exception.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CartProductRepository cartProductRepository;
    @Mock
    private PictureService pictureService;
    @InjectMocks
    private OrderService orderService;
    private CustomerEntity customer;

    @BeforeEach
    void setUp() {
        //Arrange
        customer = new CustomerEntity();
        CartEntity cart = new CartEntity();
        customer.setCart(cart);
    }

    @Test
    @DisplayName("OrderService#makeOrder correct input test")
    public void makeOrder_shouldCorrectlyAddNewOrder_whenEmailIsCorrect() {
        //Arrange
        String email = "test@example.com";
        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(customer));
        //Act
        orderService.makeOrder(email);
        //Assert
        assertThat(customer.getOrders()).hasSize(1);
    }


    @Test
    @DisplayName("OrderService#makeOrder wrong input test")
    public void makeOrder_shouldThrowException_whenEmailIsIncorrect() {
        //Arrange
        String email = "invalid@example.com";
        when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());
        //Act & Assert
        assertThatThrownBy(() -> orderService.makeOrder(email))
                .isInstanceOf(CustomerNotFoundException.class);
        verify(cartProductRepository, never()).delete(any());
    }

    @Test
    @DisplayName("OrderService#getOrders test")
    void getOrders_shouldReturnOrdersList_whenEmailIsCorrect() {
        // Arrange
        List<OrderEntity> orders = new ArrayList<>();
        OrderEntity order1 = new OrderEntity();
        order1.setOrderId(1L);
        order1.setPrice(100L);
        order1.setOrderTime(OffsetDateTime.now());
        order1.setProductInOrder(new ArrayList<>());
        OrderEntity order2 = new OrderEntity();
        order2.setOrderId(2L);
        order2.setPrice(200L);
        order2.setOrderTime(OffsetDateTime.now());
        order2.setProductInOrder(new ArrayList<>());
        orders.add(order1);
        orders.add(order2);
        customer.setOrders(orders);
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        // Act
        List<OrderDto> orderDtos = orderService.getOrders("test@example.com");
        // Assert
        assertThat(orderDtos).hasSize(2);
    }

    @Test
    @DisplayName("OrderService#getOrders wrong email test")
    void getOrders_shouldThrowException_whenEmailIsIncorrect() {
        // Arrange
        when(customerRepository.findByEmail("invalid@example.com")).thenReturn(Optional.empty());
        // Act & Assert
        assertThatThrownBy(() -> orderService.getOrders("invalid@example.com"))
                .isInstanceOf(CustomerNotFoundException.class);
    }
}
