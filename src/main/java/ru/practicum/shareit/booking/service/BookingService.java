package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking create(long userId, BookingDto bookingDto);

    Booking responseToRequest(long userId, long bookingId, boolean approved);

    Booking findBookingById(long userId, long bookingId);

    public List<Booking> findBookingAuthor(long userId, String state);

    List<Booking> findBookingOwner(long userId, String state);

}
