package com.psm.parcel.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String bookingId;
    private String customerId;
    private String senderName;
    private String senderAddress;
    private String senderContact;
    private String receiverName;
    private String receiverAddress;
    private String receiverPin;
    private String receiverContact;
    private String parcelSize;
    private Double parcelWeight;
    private String contents;
    private String deliverySpeed;
    private String packaging;
    private LocalDateTime pickupTime;
    private LocalDateTime dropOffTime;
    private BigDecimal cost;
    private String paymentMethod;
    private Boolean insurance;
    private Boolean trackingService;
    private LocalDateTime paymentTime;
    private String status;
    private LocalDateTime createdAt;
}
