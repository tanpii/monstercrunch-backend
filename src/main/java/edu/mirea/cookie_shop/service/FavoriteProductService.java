package edu.mirea.cookie_shop.service;

import edu.mirea.cookie_shop.dao.entity.CustomerEntity;
import edu.mirea.cookie_shop.dao.entity.ProductEntity;
import edu.mirea.cookie_shop.dao.repository.CustomerRepository;
import edu.mirea.cookie_shop.dao.repository.ProductRepository;
import edu.mirea.cookie_shop.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteProductService {
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final PictureService pictureService;

    @Transactional(readOnly = true)
    public List<ProductDto> getFavoriteProducts(String email) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return customer.getFavoriteProducts().stream().map(entity ->
                new ProductDto(
                        entity.getProductId(),
                        entity.getProductName(),
                        entity.getDescription(),
                        entity.getPrice(),
                        entity.getAmount(),
                        URI.create(pictureService.getLinkOnPicture(entity.getProductName()))
                )
        ).toList();
    }

    @Transactional(readOnly = true)
    public boolean isProductFavorite(String email, Long id) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        ProductEntity product = productRepository.findById(id).orElseThrow(RuntimeException::new);
        return customer.getFavoriteProducts().contains(product);
    }

    @Transactional
    public void addFavouriteProduct(String email, Long id) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        ProductEntity product = productRepository.findById(id).orElseThrow(RuntimeException::new);
        customer.addProductToFavorite(product);
    }

    @Transactional
    public void removeFavoriteProduct(String email, Long id) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        ProductEntity product = productRepository.findById(id).orElseThrow(RuntimeException::new);
        customer.getFavoriteProducts().remove(product);
    }
}
