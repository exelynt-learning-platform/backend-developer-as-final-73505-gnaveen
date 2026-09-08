package com.example.booking.service;

import com.example.booking.dto.BookingRequest;
import com.example.booking.dto.BookingResponse;
import com.example.booking.dto.BookingUpdateRequest;
import com.example.booking.entity.Booking;
import com.example.booking.exception.BookingNotFoundException;
import com.example.booking.repository.BookingRepository;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
    
    public List<BookingResponse> getAllBookings() {

        return bookingRepository.findAll()
                .stream()
                .map(booking -> new BookingResponse(
                        booking.getId(),
                        booking.getCustomerName(),
                        booking.getEmail(),
                        booking.getEventName(),
                        booking.getBookingDate(),
                        booking.getNumberOfSeats()
                ))
                .toList();
    }
    public BookingResponse getBookingById(Long id) {

    Booking booking = bookingRepository.findById(id)
            .orElseThrow(() ->
                    new BookingNotFoundException(
                            "Booking not found with id: " + id
                    )
            );

    return new BookingResponse(
            booking.getId(),
            booking.getCustomerName(),
            booking.getEmail(),
            booking.getEventName(),
            booking.getBookingDate(),
            booking.getNumberOfSeats()
        );
    }
    public BookingResponse createBooking(BookingRequest request) {

    Booking booking = new Booking();

    booking.setCustomerName(request.getCustomerName());
    booking.setEmail(request.getEmail());
    booking.setEventName(request.getEventName());
    booking.setBookingDate(request.getBookingDate());
    booking.setNumberOfSeats(request.getNumberOfSeats());

    Booking savedBooking = bookingRepository.save(booking);

    return new BookingResponse(
            savedBooking.getId(),
            savedBooking.getCustomerName(),
            savedBooking.getEmail(),
            savedBooking.getEventName(),
            savedBooking.getBookingDate(),
            savedBooking.getNumberOfSeats()
        );
    }
   public BookingResponse updateBooking(Long id, BookingUpdateRequest request) {

    Booking existingBooking = bookingRepository.findById(id)
            .orElseThrow(() ->
                    new BookingNotFoundException("Booking not found with id: " + id));

    existingBooking.setBookingDate(request.getBookingDate());
    existingBooking.setCustomerName(request.getCustomerName());
    existingBooking.setEmail(request.getEmail());
    existingBooking.setEventName(request.getEventName());
    existingBooking.setNumberOfSeats(request.getNumberOfSeats());

    Booking updatedBooking = bookingRepository.save(existingBooking);

    return new BookingResponse(
            updatedBooking.getId(),
            updatedBooking.getCustomerName(),
            updatedBooking.getEmail(),
            updatedBooking.getEventName(),
            updatedBooking.getBookingDate(),
            updatedBooking.getNumberOfSeats()
        );
   }
   public void deleteBooking(Long id) {

    if (!bookingRepository.existsById(id)) {
        throw new BookingNotFoundException("Booking not found with id: " + id);
    }

    bookingRepository.deleteById(id);
   } 
}