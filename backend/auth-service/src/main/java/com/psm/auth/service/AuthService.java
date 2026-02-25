package com.psm.auth.service;

import com.psm.auth.dto.AuthDtos.*;
import com.psm.auth.entity.Customer;
import com.psm.auth.repo.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final String OFFICER_USER = "officer01";
    private static final String OFFICER_PASS = "Officer@123";
    private final CustomerRepository customerRepository;

    public AuthResponse register(RegisterRequest req) {
        if (!req.password().equals(req.confirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password and confirm password must match");
        }
        if (customerRepository.existsByUserId(req.userId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID already exists");
        }
        if (customerRepository.existsByEmail(req.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }
        String code = "CUST-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Customer saved = customerRepository.save(Customer.builder()
                .customerCode(code)
                .name(req.name())
                .email(req.email())
                .countryCode(req.countryCode())
                .mobile(req.mobile())
                .address(req.address())
                .userId(req.userId())
                .password(req.password())
                .preferences(req.preferences())
                .build());
        return new AuthResponse("Registration successful", "CUSTOMER", saved.getUserId(), saved.getName(), saved.getEmail(), saved.getCustomerCode());
    }

    public AuthResponse login(LoginRequest req) {
        if ("OFFICER".equalsIgnoreCase(req.role())) {
            if (OFFICER_USER.equals(req.userId()) && OFFICER_PASS.equals(req.password())) {
                return new AuthResponse("Login successful", "OFFICER", req.userId(), "Parcel Officer", "officer@psm.local", null);
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid officer credentials");
        }
        Customer customer = customerRepository.findByUserId(req.userId())
                .filter(c -> c.getPassword().equals(req.password()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid customer credentials"));
        return new AuthResponse("Login successful", "CUSTOMER", customer.getUserId(), customer.getName(), customer.getEmail(), customer.getCustomerCode());
    }

    public Customer findCustomer(String userId) {
        return customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }
}
