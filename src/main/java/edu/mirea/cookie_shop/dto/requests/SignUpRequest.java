package edu.mirea.cookie_shop.dto.requests;

public record SignUpRequest(String customerName, String customerSurname, String email, String password) {
}
