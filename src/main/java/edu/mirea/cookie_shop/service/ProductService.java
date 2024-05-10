package edu.mirea.cookie_shop.service;

import edu.mirea.cookie_shop.dao.entity.CustomerEntity;
import edu.mirea.cookie_shop.dao.entity.ProductEntity;
import edu.mirea.cookie_shop.dao.repository.CustomerRepository;
import edu.mirea.cookie_shop.dao.repository.ProductRepository;
import edu.mirea.cookie_shop.dto.Role;
import edu.mirea.cookie_shop.dto.requests.AddNewProductRequest;
import edu.mirea.cookie_shop.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final FavoriteProductService favoriteProductService;
    private final CartService cartService;

    @Transactional(readOnly = true)
    public List<ProductEntity> getProducts() {
        return productRepository.findAll(Sort.by(Sort.Direction.ASC, "productId"))
                .stream().filter(entity -> !entity.isRemoved()).toList();
    }

    @Transactional
    public void addProduct(AddNewProductRequest request) {
        ProductEntity product =
                new ProductEntity(request.productName(), request.description(), request.price(), request.amount());
        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        ProductEntity product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        List<CustomerEntity> customers = customerRepository.findAllByRole(Role.ROLE_USER);
        customers.forEach(customer -> {
            favoriteProductService.removeFavoriteProduct(customer.getEmail(), productId);
            cartService.removeFromCart(customer.getEmail(), productId);
        });
        product.setRemoved(true);
    }
}
