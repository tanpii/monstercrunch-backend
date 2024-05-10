package edu.mirea.cookie_shop.dao.entity;

import edu.mirea.cookie_shop.dao.entity.link_tables.CartProductEntity;
import edu.mirea.cookie_shop.dao.entity.link_tables.OrderProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int price;

    private long amount = 0;

    @Column(nullable = false)
    private boolean removed = false;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Set<CartProductEntity> carts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<OrderProductEntity> orders = new ArrayList<>();

    @ManyToMany(mappedBy = "favoriteProducts", fetch = FetchType.LAZY)
    private Set<CustomerEntity> customersFav = new HashSet<>();

    public ProductEntity(String productName, String description, int price, long amount) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }
}
