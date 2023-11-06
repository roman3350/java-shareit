package ru.practicum.shareit.utilites;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.utilites.exception.ValidationTime;

import java.time.LocalDateTime;

@Slf4j
public class Validation {
    public static void validationTime(BookItemRequestDto bookItemRequestDto) {
        if (bookItemRequestDto.getStart() == null
                || bookItemRequestDto.getEnd() == null
                || bookItemRequestDto.getStart().isAfter(bookItemRequestDto.getEnd())
                || bookItemRequestDto.getStart().equals(bookItemRequestDto.getEnd())
                || bookItemRequestDto.getStart().isBefore(LocalDateTime.now())) {
            log.warn("Некорректное время");
            throw new ValidationTime("Некорректное время");
        }
    }
}
