package com.psm.parcel.controller;

import com.psm.parcel.dto.ParcelDtos.*;
import com.psm.parcel.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/parcel")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/booking")
    public BookingResponse create(@Valid @RequestBody BookingRequest request) {
        return bookingService.toResponse(bookingService.create(request));
    }

    @PostMapping("/booking/{bookingId}/pay")
    public BookingResponse pay(@PathVariable String bookingId, @Valid @RequestBody PaymentRequest request) {
        return bookingService.toResponse(bookingService.pay(bookingId, request));
    }

    @GetMapping("/tracking/{bookingId}")
    public BookingResponse tracking(@PathVariable String bookingId) {
        return bookingService.toResponse(bookingService.getByBookingId(bookingId));
    }

    @GetMapping("/history/customer/{customerId}")
    public List<BookingResponse> customerHistory(@PathVariable String customerId,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        return bookingService.history(customerId, page, size).stream().map(bookingService::toResponse).toList();
    }

    @GetMapping("/history/officer")
    public List<BookingResponse> officerHistory(@RequestParam String customerId,
                                                @RequestParam String from,
                                                @RequestParam String to,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return bookingService.historyByRange(customerId, LocalDate.parse(from), LocalDate.parse(to), page, size)
                .stream().map(bookingService::toResponse).toList();
    }

    @GetMapping("/officer/shipments")
    public List<BookingResponse> shipments() {
        return bookingService.allLatest().stream().map(bookingService::toResponse).toList();
    }

    @PutMapping("/officer/{bookingId}/pickup")
    public BookingResponse updatePickup(@PathVariable String bookingId, @Valid @RequestBody PickupUpdateRequest request) {
        return bookingService.toResponse(bookingService.updatePickup(bookingId, request));
    }

    @PutMapping("/officer/{bookingId}/status")
    public BookingResponse updateStatus(@PathVariable String bookingId, @Valid @RequestBody StatusUpdateRequest request) {
        return bookingService.toResponse(bookingService.updateStatus(bookingId, request));
    }
}
