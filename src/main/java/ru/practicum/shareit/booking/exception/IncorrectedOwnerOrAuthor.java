package ru.practicum.shareit.booking.exception;

public class IncorrectedOwnerOrAuthor extends RuntimeException {
    public IncorrectedOwnerOrAuthor(String s) {
        super(s);
    }
}
