package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.utilites.Validation.validationTime;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    /**
     * Запрос на вывод бронирования по ID пользователя
     * @param userId ID пользователя
     * @param stateParam Статус бронирования
     * @param from С какого элемента выводить
     * @param size Количество элементов на странице
     * @return
     */
    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id")
                                              long userId,
                                              @RequestParam(name = "state", defaultValue = "all")
                                              String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                              Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10")
                                              Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

    /**
     * Запрос на создание бронирования
     * @param userId ID пользователя
     * @param requestDto Бронирование
     * @return Созданное бронирование
     */
    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody @Valid BookItemRequestDto requestDto) {
        validationTime(requestDto);
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    /**
     * Запрос на вывод бронирования по ID бронирования
     * @param userId ID пользователя
     * @param bookingId ID бронирования
     * @return Бронирование
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    /**
     * Запрос на подтверждение или отклонение бронирования
     * @param userId ID пользователя
     * @param bookingId ID бронирования
     * @param approved Подтверждение или отклонение бронирования
     * @return бронирование
     */
    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> responseToRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @PathVariable long bookingId,
                                                    @RequestParam boolean approved) {
        log.info("Response to request booking {}, userId={}, approved={}", bookingId, userId, approved);
        return bookingClient.responseToRequest(userId, bookingId, approved);
    }

    /**
     * Запрос на вывод бронирований вещи, осуществляемый владельцем вещи
     * @param userId ID пользователя
     * @param stateParam Статус бронирования
     * @param from С какого элемента выводить
     * @param size Количество элементов на странице
     * @return
     */
    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingOwner(@RequestHeader("X-Sharer-User-Id")
                                                  long userId,
                                                  @RequestParam(name = "state", defaultValue = "all")
                                                  String stateParam,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                  Integer from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "10")
                                                  Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Find booking owner {}, from={}, size={}", userId, from, size);
        return bookingClient.getBookingOwner(userId, state, from, size);
    }
}
