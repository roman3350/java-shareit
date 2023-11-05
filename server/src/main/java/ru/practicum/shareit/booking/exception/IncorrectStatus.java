package ru.practicum.shareit.booking.exception;

public class IncorrectStatus extends RuntimeException {
    public IncorrectStatus(String s) {
        super(s);
    }
}
