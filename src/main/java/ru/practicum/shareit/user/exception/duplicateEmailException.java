package ru.practicum.shareit.user.exception;

public class duplicateEmailException extends RuntimeException{
    public duplicateEmailException(String s) {
        super(s);
    }
}
