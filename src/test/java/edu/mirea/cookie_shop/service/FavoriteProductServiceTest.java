package edu.mirea.cookie_shop.service;

import edu.mirea.cookie_shop.dao.entity.CustomerEntity;
import edu.mirea.cookie_shop.dao.entity.ProductEntity;
import edu.mirea.cookie_shop.dao.repository.CustomerRepository;
import edu.mirea.cookie_shop.dao.repository.ProductRepository;
import edu.mirea.cookie_shop.dto.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FavoriteProductServiceTest {
    @InjectMocks
    private FavoriteProductService favoriteProductService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private PictureService pictureService;

    private CustomerEntity customer;
    private ProductEntity product;

    @BeforeEach
    void setUp() {
        //Arrange
        MockitoAnnotations.openMocks(this);
        customer = new CustomerEntity();
        customer.setEmail("test@example.com");

        product = new ProductEntity();
        product.setProductId(1L);
        product.setProductName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(10);
        product.setAmount(100);

        customer.addProductToFavorite(product);
    }

    @Test
    @DisplayName("FavoriteProductService#getFavoriteProducts test")
    void getFavoriteProducts_shouldReturnFavProductsList() {
        // Arrange
        when(customerRepository.findByEmail(any())).thenReturn(Optional.of(customer));
        when(pictureService.getLinkOnPicture(any())).thenReturn("dummy");
        List<ProductDto> expectedProducts = new ArrayList<>();
        expectedProducts.add(new ProductDto(
                product.getProductId(),
                product.getProductName(),
                product.getDescription(),
                product.getPrice(),
                product.getAmount(),
                URI.create("dummy")
        ));
        // Act
        List<ProductDto> actualProducts = favoriteProductService.getFavoriteProducts("test@example.com");
        // Assert
        assertThat(actualProducts).isEqualTo(expectedProducts);
    }

    @Test
    @DisplayName("FavoriteProductService#isProductFavorite test")
    void isProductFavorite_shouldReturnTrue_whenProductInFavList() {
        // Arrange
        when(customerRepository.findByEmail(any())).thenReturn(Optional.of(customer));
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        // Act
        boolean isFavorite = favoriteProductService.isProductFavorite("test@example.com", 1L);
        // Assert
        assertThat(isFavorite).isTrue();
    }

    @Test
    @DisplayName("FavoriteProductService#addFavoriteProduct test")
    void addFavoriteProduct_shouldCorrectlyAddNewProductToFavList() {
        // Arrange
        when(customerRepository.findByEmail(any())).thenReturn(Optional.of(customer));
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        // Act
        favoriteProductService.addFavouriteProduct("test@example.com", 1L);
        // Assert
        assertThat(customer.getFavoriteProducts()).contains(product);
    }

    @Test
    @DisplayName("FavoriteProductService#removeFavoriteProduct test")
    void removeFavoriteProduct_shouldCorrectlyRemoveProductFromFavList() {
        // Arrange
        when(customerRepository.findByEmail(any())).thenReturn(Optional.of(customer));
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        // Act
        favoriteProductService.removeFavoriteProduct("test@example.com", 1L);
        // Assert
        assertThat(customer.getFavoriteProducts()).doesNotContain(product);
    }
}
