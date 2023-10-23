package ru.practicum.shareit.booking.dto;

import lombok.Data;

@Data
public class LastBooking {
    Long id;
    Long bookerId;

    public LastBooking(Long id, Long bookerId) {
        this.id = id;
        this.bookerId = bookerId;
    }
}
