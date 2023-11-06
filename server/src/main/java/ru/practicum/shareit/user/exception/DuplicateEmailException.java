package ru.practicum.shareit.user.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String s) {
        super(s);
    }
}
