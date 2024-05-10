package edu.mirea.cookie_shop.dao.entity.link_tables;

import edu.mirea.cookie_shop.dao.entity.CartEntity;
import edu.mirea.cookie_shop.dao.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartProductEntity {
    @EmbeddedId
    private CartProductId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cartId")
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(name = "quantity", nullable = false)
    private Long quantity = 0L;

    public CartProductEntity(CartProductId id) {
        this.id = id;
    }
}