package com.example.booking.dto;

import java.time.LocalDate;

public class BookingResponse {

    private Long id;
    private String customerName;
    private String email;
    private String eventName;
    private LocalDate bookingDate;
    private Integer numberOfSeats;

    public BookingResponse() {
    }

    public BookingResponse(
            Long id,
            String customerName,
            String email,
            String eventName,
            LocalDate bookingDate,
            Integer numberOfSeats) {

        this.id = id;
        this.customerName = customerName;
        this.email = email;
        this.eventName = eventName;
        this.bookingDate = bookingDate;
        this.numberOfSeats = numberOfSeats;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getEmail() {
        return email;
    }

    public String getEventName() {
        return eventName;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }
}