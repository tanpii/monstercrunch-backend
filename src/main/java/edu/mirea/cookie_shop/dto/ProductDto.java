package edu.mirea.cookie_shop.dto;

import java.net.URI;

public record ProductDto(Long productId, String productName, String description, int price, long amount, URI image) {
}
