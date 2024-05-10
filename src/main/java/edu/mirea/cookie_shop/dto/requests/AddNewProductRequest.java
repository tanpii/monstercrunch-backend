package edu.mirea.cookie_shop.dto.requests;

import org.springframework.web.multipart.MultipartFile;

public record AddNewProductRequest(String productName, String description, int price, long amount, MultipartFile image) {
}
