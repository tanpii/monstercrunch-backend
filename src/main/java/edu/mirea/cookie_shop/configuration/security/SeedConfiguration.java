package edu.mirea.cookie_shop.configuration.security;

import edu.mirea.cookie_shop.dao.entity.CustomerEntity;
import edu.mirea.cookie_shop.dao.repository.CustomerRepository;
import edu.mirea.cookie_shop.dto.Role;
import edu.mirea.cookie_shop.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class SeedConfiguration implements CommandLineRunner {
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (customerRepository.count() == 0) {
            CustomerEntity admin = CustomerEntity
                    .builder()
                    .customerName("admin")
                    .customerSurname("admin")
                    .email("admin@admin.com")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.ROLE_ADMIN)
                    .build();
            customerService.save(admin);
            log.debug("created ADMIN user - {}", admin);
        }
    }
}
