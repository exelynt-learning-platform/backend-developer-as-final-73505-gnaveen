package com.example.booking.controller;

import com.example.booking.dto.ReservationRequest;
import com.example.booking.dto.ReservationResponse;
import com.example.booking.entity.ReservationStatus;
import com.example.booking.service.ReservationService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // =========================================================
    // GET ALL RESERVATIONS
    // =========================================================
    @GetMapping
    public Page<ReservationResponse> getAllReservations(
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {

        String[] sortParts = sort.split(",");

        Sort.Direction direction =
                sortParts.length > 1
                        && sortParts[1].equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direction, sortParts[0])
        );

        return reservationService.getAllReservations(
                status,
                minPrice,
                maxPrice,
                pageable
        );
    }

    // =========================================================
    // GET RESERVATION BY ID
    // =========================================================
    @GetMapping("/{id}")
    public ReservationResponse getReservationById(
            @PathVariable Long id,
            Authentication authentication) {

        String username = authentication.getName();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority()
                                        .equals("ROLE_ADMIN")
                        );

        return reservationService.getReservationById(
                id,
                username,
                isAdmin
        );
    }

    // =========================================================
    // CREATE RESERVATION
    // =========================================================
    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody ReservationRequest request,
            Authentication authentication) {

        String username = authentication.getName();

        BigDecimal price = BigDecimal.ZERO;

        ReservationResponse response =
                reservationService.createReservation(
                        request,
                        username,
                        price
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================================
    // UPDATE RESERVATION
    // =========================================================
    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponse> updateReservation(
            @PathVariable Long id,
            @Valid @RequestBody ReservationRequest request,
            Authentication authentication) {

        String username = authentication.getName();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority()
                                        .equals("ROLE_ADMIN")
                        );

        BigDecimal price = BigDecimal.ZERO;

        ReservationResponse response =
                reservationService.updateReservation(
                        id,
                        request,
                        price,
                        ReservationStatus.PENDING,
                        username,
                        isAdmin
                );

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // DELETE RESERVATION
    // =========================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable Long id,
            Authentication authentication) {

        String username = authentication.getName();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority()
                                        .equals("ROLE_ADMIN")
                        );

        reservationService.deleteReservation(
                id,
                username,
                isAdmin
        );

        return ResponseEntity.noContent().build();
    }
}