package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    /**
     * Запрос на бронирования
     *
     * @param userId     ID бронирующего пользователя
     * @param bookingDto Параметры бронирования
     * @return Бронирование
     */
    Booking create(long userId, BookingDto bookingDto);

    /**
     * Подтверждение или отклонение бронирования
     *
     * @param userId    ID пользователя подтверждающего или отклоняющего бронирование
     * @param bookingId ID бронирование
     * @param approved  Подтверждение или отклонение бронирования
     * @return Бронирование
     */
    Booking responseToRequest(long userId, long bookingId, boolean approved);

    /**
     * Поиск бронирования по ID
     *
     * @param userId    ID пользователя осуществляющего поиск
     * @param bookingId ID бронирования
     * @return Бронирование
     */
    Booking findBookingById(long userId, long bookingId);

    /**
     * Поиск бронирования по автору бронирования
     *
     * @param userId ID пользователя
     * @param state  Статус бронирования
     * @return Бронирование
     */
    public List<Booking> findBookingAuthor(long userId, String state);

    /**
     * Поиск бронирования владельцем вещей
     *
     * @param userId ID Пользователя
     * @param state  Статус бронирования
     * @return Бронирование
     */
    List<Booking> findBookingOwner(long userId, String state);

}
