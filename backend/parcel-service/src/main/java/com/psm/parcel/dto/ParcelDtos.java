package com.psm.parcel.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ParcelDtos {
    public record BookingRequest(
            @NotBlank String customerId,
            @NotBlank String senderName,
            @NotBlank String senderAddress,
            @NotBlank String senderContact,
            @NotBlank String receiverName,
            @NotBlank String receiverAddress,
            @NotBlank @Pattern(regexp = "\\d{6}") String receiverPin,
            @NotBlank String receiverContact,
            @NotBlank String parcelSize,
            @NotNull @Positive Double parcelWeight,
            @NotBlank String contents,
            @NotBlank String deliverySpeed,
            @NotBlank String packaging,
            @NotNull LocalDateTime pickupTime,
            @NotNull LocalDateTime dropOffTime,
            @NotBlank String paymentMethod,
            boolean insurance,
            boolean trackingService
    ) {}

    public record PaymentRequest(
            @NotBlank @Pattern(regexp = "\\d{16}") String cardNo,
            @NotBlank String cardHolder,
            @NotBlank @Pattern(regexp = "(0[1-9]|1[0-2])/[0-9]{2}") String expiry,
            @NotBlank @Pattern(regexp = "\\d{3}") String cvv
    ) {}

    public record StatusUpdateRequest(@NotBlank String status) {}

    public record PickupUpdateRequest(@NotNull LocalDateTime pickupTime) {}

    public record BookingResponse(String bookingId, String customerId, String receiverName, String receiverAddress,
                                  String receiverPin, String receiverContact, Double parcelWeight, String contents,
                                  String deliverySpeed, String packaging, LocalDateTime pickupTime, LocalDateTime dropOffTime,
                                  BigDecimal cost, LocalDateTime paymentTime, String status) {}
}
