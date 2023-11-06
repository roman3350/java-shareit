package ru.practicum.shareit.booking.dto;

import lombok.Data;

@Data
public class LastBooking {
    private Long id;
    private Long bookerId;

    public LastBooking(Long id, Long bookerId) {
        this.id = id;
        this.bookerId = bookerId;
    }
}
