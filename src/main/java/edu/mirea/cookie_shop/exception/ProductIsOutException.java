package edu.mirea.cookie_shop.exception;

public class ProductIsOutException extends RuntimeException {
    public ProductIsOutException(String message) {
        super(message);
    }
}
