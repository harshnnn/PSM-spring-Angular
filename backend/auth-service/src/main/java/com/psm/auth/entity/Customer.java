package com.psm.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String customerCode;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String countryCode;

    @Column(length = 10, nullable = false)
    private String mobile;

    @Column(length = 250, nullable = false)
    private String address;

    @Column(unique = true, nullable = false, length = 20)
    private String userId;

    @Column(nullable = false, length = 30)
    private String password;

    @Column(length = 150)
    private String preferences;
}
