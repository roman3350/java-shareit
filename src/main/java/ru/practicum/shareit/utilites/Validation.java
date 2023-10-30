package ru.practicum.shareit.utilites;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exception.IncorrectedIdBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.exception.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
public class Validation {
    public static void validationUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("электронная почта пустая или не содержит символ @");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
    }

    public static void validationDuplicateEmail(String email, User users) {
        if (users != null) {
            log.warn("Пользователь с почтой {} уже зарегистрирован", email);
            throw new DuplicateEmailException("Пользователь с такой почтой уже зарегистрирован");
        }
    }

    public static void validationIncorrectOwner(Item item, User user) {
        if (!item.getUser().equals(user)) {
            log.warn("У пользователя {} нет Item с ID {}", user.getId(), item.getId());
            throw new IncorrectOwner("Продукт не найден");
        }
    }

    public static void validationFindOwner(long userID, Optional<User> user) {
        if (user.isEmpty()) {
            log.warn("Пользователя с ID {} не существует", userID);
            throw new IncorrectOwner("Пользователь не найден");
        }
    }

    public static void validationItem(ItemDtoShort item) {
        if (item.getName().isEmpty() || item.getAvailable() == null || item.getDescription() == null) {
            log.warn("Некорректный элемент");
            throw new ValidationItemException("Некорректный продукт");
        }
    }

    public static void validationFindItem(long itemId, Optional<Item> item) {
        if (item.isEmpty()) {
            log.warn("Item с ID {} не существует", itemId);
            throw new IncorrectIdItem("Элемент не найден");
        }
    }

    public static void validationFindBooking(long bookingId, Optional<Booking> booking) {
        if (booking.isEmpty()) {
            log.warn("Booking c ID {} не существует", bookingId);
            throw new IncorrectedIdBooking("Бронирование не найдено");
        }
    }

    public static void validationOwnerOrAuthorBooking(long userId, Booking booking) {
        if (booking.getBooker().getId() != userId && booking.getItem().getUser().getId() != userId) {
            log.warn("Пользователь с ID {} не является автором или владельцем", userId);
            throw new IncorrectOwner("Пользователь не является автором или владельцем");
        }
    }

    public static void validationItemAvailable(Item item) {
        if (!item.getAvailable()) {
            log.warn("Элемент забронирован");
            throw new ValidationItemException("Элемент забронирован");
        }
    }

    public static void validationTimeRequest(BookingDto bookingDto) {
        if (bookingDto.getStart() == null
                || bookingDto.getEnd() == null
                || bookingDto.getStart().isAfter(bookingDto.getEnd())
                || bookingDto.getStart().equals(bookingDto.getEnd())
                || bookingDto.getStart().isBefore(LocalDateTime.now())) {
            log.warn("Некорректное время");
            throw new ValidationItemException("Некорректное время");
        }
    }

    public static void validationBookingStatus(Booking booking) {
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            log.warn("Элемент забронирован");
            throw new ValidationItemException("Элемент забронирован");
        }
    }

    public static void validationOwner(long userId, Item item) {
        if (userId == item.getUser().getId()) {
            log.warn("ID ({}) бронирующего совпадает с ID ({}) владельца", userId, item.getUser().getId());
            throw new IncorrectOwner("ID бронирующего совпадает с ID владельца");
        }
    }

    public static void validationBooking(long userId, long itemId, Optional<Booking> booking) {
        if (booking.isEmpty()) {
            log.warn("Пользователь c ID {} не брал в аренду элемент с ID {}", userId, itemId);
            throw new ValidationItemException("Вы не брали в аренду данный предмет");
        } else if (booking.get().getStatus().equals(BookingStatus.REJECTED)) {
            log.warn("Пользователь c ID {} не брал в аренду элемент с ID {}", userId, itemId);
            throw new ValidationItemException("Вы не брали в аренду данный предмет");
        } else if (booking.get().getStart().isAfter(LocalDateTime.now())) {
            log.warn("Пользователь c ID {} не брал в аренду элемент с ID {}", userId, itemId);
            throw new ValidationItemException("Вы не брали в аренду данный предмет");
        }
    }

    public static void validationComment(String comment) {
        if (comment.isEmpty() || comment.isBlank()) {
            log.warn("Пустой комментарий");
            throw new ValidationItemException("Пустой комментарий");
        }
    }

    public static void validationRequest(ItemRequest itemRequest) {
        if (itemRequest.getDescription() == null) {
            log.warn("Некорректный запрос");
            throw new ValidationItemException("Некорректный запрос");
        }
    }

    public static void validationPage(int from, int size) {
        if (from < 0 || size <= 0) {
            log.warn("Некорректный размер страницы");
            throw new ValidationItemException("Некорректный размер страницы");
        }
    }

    public static void validationRequestId(long requestId, Optional<ItemRequest> itemRequest) {
        if (itemRequest.isEmpty()) {
            log.warn("Запроса с ID {} нет", requestId);
            throw new IncorrectIdItem("Запроса с таким ID нет");
        }
    }
}
