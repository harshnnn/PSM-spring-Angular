package com.psm.auth.dto;

import jakarta.validation.constraints.*;

public class AuthDtos {
    public record LoginRequest(
            @NotBlank @Size(min = 5, max = 20) String userId,
            @NotBlank @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[^A-Za-z0-9]).{8,30}$",
                    message = "Password must contain uppercase, lowercase and special character") String password,
            @NotBlank String role
    ) {}

    public record RegisterRequest(
            @NotBlank @Size(max = 50) String name,
            @NotBlank @Email String email,
            @NotBlank String countryCode,
            @NotBlank @Pattern(regexp = "\\d{10}") String mobile,
            @NotBlank @Size(max = 250) String address,
            @NotBlank @Size(min = 5, max = 20) String userId,
            @NotBlank @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[^A-Za-z0-9]).{8,30}$") String password,
            @NotBlank @Size(max = 30) String confirmPassword,
            String preferences
    ) {}

    public record AuthResponse(String message, String role, String userId, String name, String email, String customerCode) {}
}
