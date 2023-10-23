package ru.practicum.shareit.booking.dto;

import lombok.Data;

@Data
public class NextBooking {
    Long id;
    Long bookerId;

    public NextBooking(Long id, Long bookerId) {
        this.id = id;
        this.bookerId = bookerId;
    }
}
