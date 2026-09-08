package com.example.booking.controller;

import com.example.booking.dto.ReservationResponse;
import com.example.booking.entity.ReservationStatus;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationControllerTest {

    @Test
    void reservationResponseShouldContainCorrectData() {

        ReservationResponse response =
                new ReservationResponse(
                        7L,
                        2L,
                        1L,
                        LocalDateTime.of(2026, 10, 11, 10, 0),
                        LocalDateTime.of(2026, 10, 11, 12, 0),
                        BigDecimal.ZERO,
                        ReservationStatus.PENDING
                );

        assertEquals(7L, response.getId());
        assertEquals(2L, response.getUserId());
        assertEquals(1L, response.getResourceId());
        assertEquals(BigDecimal.ZERO, response.getPrice());
        assertEquals(ReservationStatus.PENDING, response.getStatus());
    }
}