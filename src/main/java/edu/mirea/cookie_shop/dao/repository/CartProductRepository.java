package edu.mirea.cookie_shop.dao.repository;

import edu.mirea.cookie_shop.dao.entity.link_tables.CartProductEntity;
import edu.mirea.cookie_shop.dao.entity.link_tables.CartProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartProductRepository extends JpaRepository<CartProductEntity, CartProductId> {
}
