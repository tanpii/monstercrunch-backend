package edu.mirea.cookie_shop.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException() {
        super("Customer was not found");
    }
}
