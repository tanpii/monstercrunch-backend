package edu.mirea.cookie_shop.service;

import edu.mirea.cookie_shop.dao.entity.CartEntity;
import edu.mirea.cookie_shop.dao.entity.CustomerEntity;
import edu.mirea.cookie_shop.dao.entity.ProductEntity;
import edu.mirea.cookie_shop.dao.entity.link_tables.CartProductEntity;
import edu.mirea.cookie_shop.dao.entity.link_tables.CartProductId;
import edu.mirea.cookie_shop.dao.repository.CartProductRepository;
import edu.mirea.cookie_shop.dao.repository.CustomerRepository;
import edu.mirea.cookie_shop.dao.repository.ProductRepository;
import edu.mirea.cookie_shop.dto.CartProductDto;
import edu.mirea.cookie_shop.exception.CustomerNotFoundException;
import edu.mirea.cookie_shop.exception.ProductIsOutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CartService {
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;
    private final PictureService pictureService;

    @Transactional
    public void addCartProduct(String email, Long productId) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(CustomerNotFoundException::new);
        ProductEntity product = productRepository.findById(productId).orElseThrow(RuntimeException::new);
        if (product.getAmount() == 0) {
            throw new ProductIsOutException("Product is out of stock");
        }
        CartEntity cart = customer.getCart();
        CartProductId cartProductId = new CartProductId(cart.getCartId(), productId);
        Optional<CartProductEntity> cartProduct = cartProductRepository.findById(cartProductId);
        if (cartProduct.isEmpty()) {
            CartProductEntity cartProductEntity = addNewProduct(cart, product);
            cartProductEntity.setQuantity(cartProductEntity.getQuantity() + 1);
            product.setAmount(product.getAmount() - 1);
        } else {
            cartProduct.get().setQuantity(cartProduct.get().getQuantity() + 1);
            product.setAmount(product.getAmount() - 1);
        }
    }

    @Transactional
    public void removeCartProduct(String email, Long productId) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(CustomerNotFoundException::new);
        ProductEntity product = productRepository.findById(productId).orElseThrow(RuntimeException::new);
        CartEntity cart = customer.getCart();
        CartProductEntity cartProduct = cartProductRepository.findById(new CartProductId(cart.getCartId(), productId))
                .orElseThrow(RuntimeException::new);
        if (cartProduct.getQuantity().equals(1L)) {
            cartProductRepository.delete(cartProduct);
        } else {
            cartProduct.setQuantity(cartProduct.getQuantity() - 1);
        }
        product.setAmount(product.getAmount() + 1);
    }

    @Transactional
    public void removeFromCart(String email, Long productId) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(CustomerNotFoundException::new);
        ProductEntity product = productRepository.findById(productId).orElseThrow(RuntimeException::new);
        CartEntity cart = customer.getCart();
        CartProductEntity cartProduct = cartProductRepository.findById(new CartProductId(cart.getCartId(), productId))
                .orElseThrow(RuntimeException::new);
        product.setAmount(product.getAmount() + cartProduct.getQuantity());
//        product.getCarts().remove(cartProduct);
//        cart.getProductsInCart().remove(cartProduct);

        cartProductRepository.delete(cartProduct);
    }

    @Transactional(readOnly = true)
    public Long isInCart(String email, Long productId) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(CustomerNotFoundException::new);
        CartEntity cart = customer.getCart();
        Optional<CartProductEntity> cartProduct = cartProductRepository
                .findById(new CartProductId(cart.getCartId(), productId));
        if (cartProduct.isPresent()) {
            return cartProduct.get().getQuantity();
        }
        return 0L;
    }

    @Transactional
    public List<CartProductDto> getCart(String email) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(CustomerNotFoundException::new);
        CartEntity cart = customer.getCart();
        return cart.getProductsInCart().stream()
                .map(entity -> {
                    ProductEntity product = entity.getProduct();
                    return new CartProductDto(
                            product.getProductId(),
                            product.getProductName(),
                            product.getPrice(),
                            entity.getQuantity(),
                            URI.create(pictureService.getLinkOnPicture(product.getProductName()))
                    );
                }).toList();
    }

    private CartProductEntity addNewProduct(CartEntity cart, ProductEntity product) {
        CartProductId id = new CartProductId(cart.getCartId(), product.getProductId());
        CartProductEntity cartProduct = new CartProductEntity(id);
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cart.getProductsInCart().add(cartProduct);
        product.getCarts().add(cartProduct);
        return cartProduct;
    }
}
