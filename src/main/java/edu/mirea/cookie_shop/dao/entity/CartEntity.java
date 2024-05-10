package edu.mirea.cookie_shop.dao.entity;

import edu.mirea.cookie_shop.dao.entity.link_tables.CartProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cart")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartEntity {
    @Id
    @Column(name = "cart_id")
    private Long cartId;

    @Column(name = "customer_id")
    private Long customerId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "cart_id")
    private CustomerEntity customer;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "cart_id")
    private Set<CartProductEntity> productsInCart = new HashSet<>();

    public CartEntity(Long cartId, Long customerId, CustomerEntity customer) {
        this.cartId = cartId;
        this.customerId = customerId;
        this.customer = customer;
    }
}
