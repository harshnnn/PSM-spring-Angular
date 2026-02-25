package com.psm.parcel.service;

import com.psm.parcel.dto.ParcelDtos.*;
import com.psm.parcel.entity.Booking;
import com.psm.parcel.repo.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;

    public Booking create(BookingRequest req) {
        BigDecimal cost = calculateCost(req.parcelSize(), req.deliverySpeed(), req.parcelWeight());
        Booking booking = Booking.builder()
                .bookingId(generateBookingId())
                .customerId(req.customerId())
                .senderName(req.senderName())
                .senderAddress(req.senderAddress())
                .senderContact(req.senderContact())
                .receiverName(req.receiverName())
                .receiverAddress(req.receiverAddress())
                .receiverPin(req.receiverPin())
                .receiverContact(req.receiverContact())
                .parcelSize(req.parcelSize())
                .parcelWeight(req.parcelWeight())
                .contents(req.contents())
                .deliverySpeed(req.deliverySpeed())
                .packaging(req.packaging())
                .pickupTime(req.pickupTime())
                .dropOffTime(req.dropOffTime())
                .paymentMethod(req.paymentMethod())
                .insurance(req.insurance())
                .trackingService(req.trackingService())
                .cost(cost)
                .status("Booked")
                .createdAt(LocalDateTime.now())
                .build();
        return bookingRepository.save(booking);
    }

    public Booking pay(String bookingId, PaymentRequest paymentRequest) {
        Booking b = getByBookingId(bookingId);
        b.setPaymentTime(LocalDateTime.now());
        b.setStatus("Payment Successful");
        return bookingRepository.save(b);
    }

    public Booking updateStatus(String bookingId, StatusUpdateRequest req) {
        Booking b = getByBookingId(bookingId);
        b.setStatus(req.status());
        return bookingRepository.save(b);
    }

    public Booking updatePickup(String bookingId, PickupUpdateRequest req) {
        Booking b = getByBookingId(bookingId);
        b.setPickupTime(req.pickupTime());
        return bookingRepository.save(b);
    }

    public Booking getByBookingId(String bookingId) {
        return bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
    }

    public List<Booking> history(String customerId, int page, int size) {
        return bookingRepository.findByCustomerIdOrderByCreatedAtDesc(customerId, PageRequest.of(page, size));
    }

    public List<Booking> historyByRange(String customerId, LocalDate from, LocalDate to, int page, int size) {
        return bookingRepository.findByCustomerIdAndCreatedAtBetweenOrderByCreatedAtDesc(
                customerId, from.atStartOfDay(), to.plusDays(1).atStartOfDay(), PageRequest.of(page, size));
    }

    public List<Booking> allLatest() {
        return bookingRepository.findAll(PageRequest.of(0, 100)).getContent().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt())).toList();
    }

    private BigDecimal calculateCost(String size, String speed, Double weight) {
        BigDecimal base = switch (size.toUpperCase()) {
            case "SMALL" -> BigDecimal.valueOf(80);
            case "MEDIUM" -> BigDecimal.valueOf(140);
            default -> BigDecimal.valueOf(220);
        };
        BigDecimal speedMultiplier = "EXPRESS".equalsIgnoreCase(speed) ? BigDecimal.valueOf(1.8) : BigDecimal.ONE;
        return base.multiply(speedMultiplier).add(BigDecimal.valueOf(weight * 10));
    }

    private String generateBookingId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

    public BookingResponse toResponse(Booking b) {
        return new BookingResponse(b.getBookingId(), b.getCustomerId(), b.getReceiverName(), b.getReceiverAddress(),
                b.getReceiverPin(), b.getReceiverContact(), b.getParcelWeight(), b.getContents(), b.getDeliverySpeed(),
                b.getPackaging(), b.getPickupTime(), b.getDropOffTime(), b.getCost(), b.getPaymentTime(), b.getStatus());
    }
}
