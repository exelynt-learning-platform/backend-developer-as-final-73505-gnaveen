package com.example.booking.service;

import com.example.booking.dto.ReservationRequest;
import com.example.booking.dto.ReservationResponse;
import com.example.booking.entity.Reservation;
import com.example.booking.entity.ReservationStatus;
import com.example.booking.entity.Resource;
import com.example.booking.entity.User;

import com.example.booking.exception.ReservationNotFoundException;
import com.example.booking.exception.ResourceNotFoundException;
import com.example.booking.exception.UserNotFoundException;

import com.example.booking.repository.ReservationRepository;
import com.example.booking.repository.ResourceRepository;
import com.example.booking.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ResourceRepository resourceRepository,
            UserRepository userRepository) {

        this.reservationRepository = reservationRepository;
        this.resourceRepository = resourceRepository;
        this.userRepository = userRepository;
    }

    // =========================================================
    // GET ALL RESERVATIONS
    // =========================================================
    public Page<ReservationResponse> getAllReservations(
            ReservationStatus status,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable) {

        Page<Reservation> reservations;

        if (status != null && minPrice != null && maxPrice != null) {

            reservations =
                    reservationRepository
                            .findByStatusAndPriceGreaterThanEqualAndPriceLessThanEqual(
                                    status,
                                    minPrice,
                                    maxPrice,
                                    pageable
                            );

        } else if (status != null && minPrice != null) {

            reservations =
                    reservationRepository
                            .findByStatusAndPriceGreaterThanEqual(
                                    status,
                                    minPrice,
                                    pageable
                            );

        } else if (status != null && maxPrice != null) {

            reservations =
                    reservationRepository
                            .findByStatusAndPriceLessThanEqual(
                                    status,
                                    maxPrice,
                                    pageable
                            );

        } else if (minPrice != null && maxPrice != null) {

            reservations =
                    reservationRepository
                            .findByPriceGreaterThanEqualAndPriceLessThanEqual(
                                    minPrice,
                                    maxPrice,
                                    pageable
                            );

        } else if (status != null) {

            reservations =
                    reservationRepository.findByStatus(
                            status,
                            pageable
                    );

        } else if (minPrice != null) {

            reservations =
                    reservationRepository.findByPriceGreaterThanEqual(
                            minPrice,
                            pageable
                    );

        } else if (maxPrice != null) {

            reservations =
                    reservationRepository.findByPriceLessThanEqual(
                            maxPrice,
                            pageable
                    );

        } else {

            reservations =
                    reservationRepository.findAll(pageable);
        }

        return reservations.map(this::mapToResponse);
    }

    // =========================================================
    // GET RESERVATION BY ID
    // =========================================================
    public ReservationResponse getReservationById(
            Long id,
            String username,
            boolean isAdmin) {

        Reservation reservation =
                reservationRepository.findById(id)
                        .orElseThrow(() ->
                                new ReservationNotFoundException(
                                        "Reservation not found with id: " + id
                                )
                        );

        if (!isOwnerOrAdmin(
                reservation,
                username,
                isAdmin)) {

            throw new AccessDeniedException(
                    "You are not allowed to access this reservation"
            );
        }

        return mapToResponse(reservation);
    }

    // =========================================================
    // CREATE RESERVATION
    // =========================================================
    public ReservationResponse createReservation(
            ReservationRequest request,
            String username,
            BigDecimal price) {

        // Validate time
        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new IllegalArgumentException(
                    "Start time must be before end time"
            );
        }

        User user =
                userRepository.findByUsername(username)
                        .orElseThrow(() ->
                                new UserNotFoundException(
                                        "User not found: " + username
                                )
                        );

        Resource resource =
                resourceRepository.findById(request.getResourceId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Resource not found with id: "
                                                + request.getResourceId()
                                )
                        );

        // Check overlapping reservation
        boolean conflict =
                reservationRepository
                        .existsByResourceIdAndStartTimeLessThanAndEndTimeGreaterThan(
                                request.getResourceId(),
                                request.getEndTime(),
                                request.getStartTime()
                        );

        if (conflict) {
            throw new IllegalArgumentException(
                    "Resource is already reserved for the selected time"
            );
        }

        Reservation reservation = new Reservation();

        reservation.setUser(user);
        reservation.setResource(resource);
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        reservation.setPrice(price);
        reservation.setStatus(ReservationStatus.PENDING);

        Reservation savedReservation =
                reservationRepository.save(reservation);

        return mapToResponse(savedReservation);
    }

    // =========================================================
    // UPDATE RESERVATION
    // =========================================================
    public ReservationResponse updateReservation(
            Long id,
            ReservationRequest request,
            BigDecimal price,
            ReservationStatus status,
            String username,
            boolean isAdmin) {

        Reservation existingReservation =
                reservationRepository.findById(id)
                        .orElseThrow(() ->
                                new ReservationNotFoundException(
                                        "Reservation not found with id: " + id
                                )
                        );

        // Check ownership
        if (!isOwnerOrAdmin(
                existingReservation,
                username,
                isAdmin)) {

            throw new AccessDeniedException(
                    "You are not allowed to update this reservation"
            );
        }

        // Validate time
        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new IllegalArgumentException(
                    "Start time must be before end time"
            );
        }

        Resource resource =
                resourceRepository.findById(request.getResourceId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Resource not found with id: "
                                                + request.getResourceId()
                                )
                        );

        // Check overlapping reservation
        // Exclude the current reservation being updated
        boolean conflict =
                reservationRepository
                        .existsByResourceIdAndStartTimeLessThanAndEndTimeGreaterThanAndIdNot(
                                request.getResourceId(),
                                request.getEndTime(),
                                request.getStartTime(),
                                id
                        );

        if (conflict) {
            throw new IllegalArgumentException(
                    "Resource is already reserved for the selected time"
            );
        }

        existingReservation.setResource(resource);
        existingReservation.setStartTime(request.getStartTime());
        existingReservation.setEndTime(request.getEndTime());
        existingReservation.setPrice(price);
        existingReservation.setStatus(status);

        Reservation updatedReservation =
                reservationRepository.save(existingReservation);

        return mapToResponse(updatedReservation);
    }

    // =========================================================
    // DELETE RESERVATION
    // =========================================================
    public void deleteReservation(
            Long id,
            String username,
            boolean isAdmin) {

        Reservation reservation =
                reservationRepository.findById(id)
                        .orElseThrow(() ->
                                new ReservationNotFoundException(
                                        "Reservation not found with id: " + id
                                )
                        );

        // Check ownership
        if (!isOwnerOrAdmin(
                reservation,
                username,
                isAdmin)) {

            throw new AccessDeniedException(
                    "You are not allowed to delete this reservation"
            );
        }

        reservationRepository.delete(reservation);
    }

    // =========================================================
    // CHECK OWNERSHIP OR ADMIN
    // =========================================================
    private boolean isOwnerOrAdmin(
            Reservation reservation,
            String username,
            boolean isAdmin) {

        return isAdmin
                || reservation.getUser()
                        .getUsername()
                        .equals(username);
    }

    // =========================================================
    // MAP ENTITY -> RESPONSE DTO
    // =========================================================
    private ReservationResponse mapToResponse(
            Reservation reservation) {

        return new ReservationResponse(
                reservation.getId(),
                reservation.getUser().getId(),
                reservation.getResource().getId(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getPrice(),
                reservation.getStatus()
        );
    }
}