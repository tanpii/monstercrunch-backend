package edu.mirea.cookie_shop.dto;

import java.net.URI;

public record OrderProductDto(String name, long price, long amount, URI image) {
}
