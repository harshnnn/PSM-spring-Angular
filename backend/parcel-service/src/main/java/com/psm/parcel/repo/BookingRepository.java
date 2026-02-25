package com.psm.parcel.repo;

import com.psm.parcel.entity.Booking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingId(String bookingId);
    List<Booking> findByCustomerIdOrderByCreatedAtDesc(String customerId, Pageable pageable);
    List<Booking> findByCustomerIdAndCreatedAtBetweenOrderByCreatedAtDesc(String customerId, LocalDateTime from, LocalDateTime to, Pageable pageable);
}
