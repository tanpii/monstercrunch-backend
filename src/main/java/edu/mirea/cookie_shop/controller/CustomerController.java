package edu.mirea.cookie_shop.controller;

import edu.mirea.cookie_shop.dto.CustomerData;
import edu.mirea.cookie_shop.service.CustomerService;
import edu.mirea.cookie_shop.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final JwtService jwtService;

    private static final String TOKEN = "Token";

    @GetMapping
    @CrossOrigin
    public CustomerData getCustomerData(@RequestHeader(TOKEN) String token) {
        String email = jwtService.extractUserName(token);
        return customerService.getCustomer(email);
    }
}
