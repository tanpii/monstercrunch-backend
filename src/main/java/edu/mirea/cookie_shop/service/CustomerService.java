package edu.mirea.cookie_shop.service;

import edu.mirea.cookie_shop.dao.entity.CustomerEntity;
import edu.mirea.cookie_shop.dao.repository.CustomerRepository;
import edu.mirea.cookie_shop.dto.CustomerData;
import edu.mirea.cookie_shop.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Transactional
    public UserDetailsService userDetailsService() {
        return username -> customerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public CustomerEntity save(CustomerEntity newUser) {
        return customerRepository.save(newUser);
    }

    @Transactional(readOnly = true)
    public CustomerData getCustomer(String email) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(CustomerNotFoundException::new);
        return new CustomerData(customer.getCustomerName(), customer.getCustomerSurname(), customer.getEmail());
    }
}
