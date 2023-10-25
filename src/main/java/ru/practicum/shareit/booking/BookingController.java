package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingServiceImpl;

import java.util.List;

/**
 * Контроллер бронирования
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingServiceImpl bookingService;

    /**
     * Запрос на бронирования
     *
     * @param userId     ID бронирующего пользователя
     * @param bookingDto Параметры бронирования
     * @return Бронирование
     */
    @PostMapping
    public Booking create(@RequestHeader("X-Sharer-User-Id") long userId,
                          @RequestBody BookingDto bookingDto) {
        return bookingService.create(userId, bookingDto);
    }

    /**
     * Подтверждение или отклонение бронирования
     *
     * @param userId    ID пользователя подтверждающего или отклоняющего бронирование
     * @param bookingId ID бронирование
     * @param approved  Подтверждение или отклонение бронирования
     * @return Бронирование
     */
    @PatchMapping("/{bookingId}")
    public Booking responseToRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @PathVariable long bookingId,
                                     @RequestParam boolean approved) {
        return bookingService.responseToRequest(userId, bookingId, approved);
    }

    /**
     * Поиск бронирования по ID
     *
     * @param userId    ID пользователя осуществляющего поиск
     * @param bookingId ID бронирования
     * @return Бронирование
     */
    @GetMapping("/{bookingId}")
    public Booking findBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @PathVariable long bookingId) {
        return bookingService.findBookingById(userId, bookingId);
    }

    /**
     * Поиск бронирования по автору бронирования
     *
     * @param userId ID пользователя
     * @param state  Статус бронирования
     * @return Бронирование
     */
    @GetMapping
    public List<Booking> findBookingAuthor(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findBookingAuthor(userId, state);
    }

    /**
     * Поиск бронирования владельцем вещей
     *
     * @param userId ID Пользователя
     * @param state  Статус бронирования
     * @return Бронирование
     */
    @GetMapping("/owner")
    public List<Booking> findBookingOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findBookingOwner(userId, state);
    }
}
