package com.example.booking.repository;

import com.example.booking.entity.Reservation;
import com.example.booking.entity.ReservationStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Page<Reservation> findByUserId(
            Long userId,
            Pageable pageable
    );

    Page<Reservation> findByStatus(
            ReservationStatus status,
            Pageable pageable
    );

    Page<Reservation> findByPriceGreaterThanEqual(
            BigDecimal minPrice,
            Pageable pageable
    );

    Page<Reservation> findByPriceLessThanEqual(
            BigDecimal maxPrice,
            Pageable pageable
    );

    Page<Reservation> findByStatusAndPriceGreaterThanEqual(
            ReservationStatus status,
            BigDecimal minPrice,
            Pageable pageable
    );

    Page<Reservation> findByStatusAndPriceLessThanEqual(
            ReservationStatus status,
            BigDecimal maxPrice,
            Pageable pageable
    );

    Page<Reservation> findByPriceGreaterThanEqualAndPriceLessThanEqual(
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );

    Page<Reservation> findByStatusAndPriceGreaterThanEqualAndPriceLessThanEqual(
            ReservationStatus status,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );

    Page<Reservation> findByUserIdAndStatus(
            Long userId,
            ReservationStatus status,
            Pageable pageable
    );

    Page<Reservation> findByUserIdAndPriceGreaterThanEqual(
            Long userId,
            BigDecimal minPrice,
            Pageable pageable
    );

    Page<Reservation> findByUserIdAndPriceLessThanEqual(
            Long userId,
            BigDecimal maxPrice,
            Pageable pageable
    );

    Page<Reservation> findByUserIdAndStatusAndPriceGreaterThanEqual(
            Long userId,
            ReservationStatus status,
            BigDecimal minPrice,
            Pageable pageable
    );

    Page<Reservation> findByUserIdAndStatusAndPriceLessThanEqual(
            Long userId,
            ReservationStatus status,
            BigDecimal maxPrice,
            Pageable pageable
    );

    Page<Reservation> findByUserIdAndPriceGreaterThanEqualAndPriceLessThanEqual(
            Long userId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );

    Page<Reservation> findByUserIdAndStatusAndPriceGreaterThanEqualAndPriceLessThanEqual(
            Long userId,
            ReservationStatus status,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );

    // =========================================================
    // CHECK OVERLAPPING RESERVATION - CREATE
    // =========================================================
    boolean existsByResourceIdAndStartTimeLessThanAndEndTimeGreaterThan(
            Long resourceId,
            LocalDateTime endTime,
            LocalDateTime startTime
    );

    // =========================================================
    // CHECK OVERLAPPING RESERVATION - UPDATE
    // Excludes the reservation currently being updated
    // =========================================================
    boolean existsByResourceIdAndStartTimeLessThanAndEndTimeGreaterThanAndIdNot(
            Long resourceId,
            LocalDateTime endTime,
            LocalDateTime startTime,
            Long id
    );
}