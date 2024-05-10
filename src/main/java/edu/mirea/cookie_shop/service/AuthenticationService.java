package edu.mirea.cookie_shop.service;

import edu.mirea.cookie_shop.dao.entity.CartEntity;
import edu.mirea.cookie_shop.dao.entity.CustomerEntity;
import edu.mirea.cookie_shop.dao.repository.CustomerRepository;
import edu.mirea.cookie_shop.dto.Role;
import edu.mirea.cookie_shop.dto.requests.SignInRequest;
import edu.mirea.cookie_shop.dto.requests.SignUpRequest;
import edu.mirea.cookie_shop.dto.response.JwtAuthenticationResponse;
import edu.mirea.cookie_shop.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        CustomerEntity customer = CustomerEntity.builder()
                .customerName(request.customerName())
                .customerSurname(request.customerSurname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ROLE_USER)
                .build();

        customer = customerService.save(customer);
        CartEntity cart = new CartEntity(customer.getCustomerId(), customer.getCustomerId(), customer);
        customer.setCart(cart);
        var jwt = jwtService.generateToken(customer);
        return new JwtAuthenticationResponse(jwt);
    }

    @Transactional
    public JwtAuthenticationResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        var user = customerRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    @Transactional(readOnly = true)
    public boolean isAuthorized(String token) {
        return !jwtService.isTokenExpired(token);
    }

    @Transactional(readOnly = true)
    public boolean isAdmin(String token) {
        String email = jwtService.extractUserName(token);
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(CustomerNotFoundException::new);
        return customer.getRole().equals(Role.ROLE_ADMIN);
    }
}
