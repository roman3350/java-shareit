package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.exception.IncorrectStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.utilites.Validation.*;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    /**
     * Запрос на бронирования
     *
     * @param userId     ID бронирующего пользователя
     * @param bookingDto Параметры бронирования
     * @return Бронирование
     */
    @Override
    public Booking create(long userId, BookingDto bookingDto) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Item> item = itemRepository.findById(bookingDto.getItemId());
        validationFindOwner(userId, user);
        validationFindItem(bookingDto.getItemId(), item);
        Booking booking = BookingMapper.mapToBooking(bookingDto, item.get(), user.get());
        validationOwner(userId, item.get());
        validationItemAvailable(item.get());
        return bookingRepository.save(booking);
    }

    /**
     * Подтверждение или отклонение бронирования
     *
     * @param userId    ID пользователя подтверждающего или отклоняющего бронирование
     * @param bookingId ID бронирование
     * @param approved  Подтверждение или отклонение бронирования
     * @return Бронирование
     */
    @Override
    public Booking responseToRequest(long userId, long bookingId, boolean approved) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        Optional<User> user = userRepository.findById(userId);
        validationFindBooking(bookingId, booking);
        validationIncorrectOwner(booking.get().getItem(), user.get());
        Booking bookings = booking.get();
        if (approved) {
            validationBookingStatus(bookings);
            bookings.setStatus(BookingStatus.APPROVED);
            return bookingRepository.save(bookings);
        } else {
            bookings.setStatus(BookingStatus.REJECTED);
            return bookingRepository.save(bookings);
        }
    }

    /**
     * Поиск бронирования по ID
     *
     * @param userId    ID пользователя осуществляющего поиск
     * @param bookingId ID бронирования
     * @return Бронирование
     */
    @Override
    public Booking findBookingById(long userId, long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        validationFindBooking(bookingId, booking);
        validationOwnerOrAuthorBooking(userId, booking.get());
        return booking.get();
    }

    /**
     * Поиск бронирования по автору бронирования
     *
     * @param userId ID пользователя
     * @param state  Статус бронирования
     * @return Бронирование
     */
    @Override
    public List<Booking> findBookingAuthor(long userId, String state, int from, int size) {
        try {
            PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
            BookingStatus status = BookingStatus.valueOf(state);
            validationFindOwner(userId, userRepository.findById(userId));
            switch (status) {
                case ALL:
                    return bookingRepository.findAllByBookerId(userId, page);
                case PAST:
                    return bookingRepository.findAllByBookerIdAndEndBeforeOrderByEndDesc(userId,
                            LocalDateTime.now(),
                            page);
                case FUTURE:
                    return bookingRepository.findAllByBookerIdAndStatusInAndStartAfterOrderByStartDesc(userId,
                            List.of(BookingStatus.APPROVED,
                                    BookingStatus.WAITING),
                            LocalDateTime.now(),
                            page);
                case CURRENT:
                    return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(userId,
                            LocalDateTime.now(),
                            LocalDateTime.now(),
                            page);
                case WAITING:
                    return bookingRepository.findAllByBookerIdAndStatus(userId, BookingStatus.WAITING, page);
                case APPROVED:
                    return bookingRepository.findAllByBookerIdAndStatus(userId, BookingStatus.APPROVED, page);
                case CANCELED:
                    return bookingRepository.findAllByBookerIdAndStatus(userId, BookingStatus.CANCELED, page);
                case REJECTED:
                    return bookingRepository.findAllByBookerIdAndStatus(userId, BookingStatus.REJECTED, page);
                default:
                    throw new IncorrectStatus("Unknown state: UNSUPPORTED_STATUS");
            }
        } catch (IllegalArgumentException e) {
            throw new IncorrectStatus("Unknown state: UNSUPPORTED_STATUS");
        }

    }

    /**
     * Поиск бронирования владельцем вещей
     *
     * @param userId ID Пользователя
     * @param state  Статус бронирования
     * @return Бронирование
     */
    @Override
    public List<Booking> findBookingOwner(long userId, String state, int from, int size) {
        try {
            PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
            BookingStatus status = BookingStatus.valueOf(state);
            validationFindOwner(userId, userRepository.findById(userId));
            switch (status) {
                case ALL:
                    return bookingRepository.findAllByOwnerId(userId, page);
                case PAST:
                    return bookingRepository.findAllByOwnerIdAndEndBefore(userId,
                            LocalDateTime.now(),
                            page);
                case FUTURE:
                    return bookingRepository.findAllByOwnerIdAndStatusInAndStartBeforeOrderByStartDesc(userId,
                            BookingStatus.APPROVED,
                            BookingStatus.WAITING,
                            LocalDateTime.now(),
                            page);
                case CURRENT:
                    return bookingRepository.findAllByOwnerIdAndStartBeforeAndEndAfter(userId,
                            LocalDateTime.now(),
                            page);
                case WAITING:
                    return bookingRepository.findAllByOwnerIdAndStatus(userId, BookingStatus.WAITING, page);
                case APPROVED:
                    return bookingRepository.findAllByOwnerIdAndStatus(userId, BookingStatus.APPROVED, page);
                case CANCELED:
                    return bookingRepository.findAllByOwnerIdAndStatus(userId, BookingStatus.CANCELED, page);
                case REJECTED:
                    return bookingRepository.findAllByOwnerIdAndStatus(userId, BookingStatus.REJECTED, page);
                default:
                    throw new IncorrectStatus("Unknown state: UNSUPPORTED_STATUS");
            }
        } catch (IllegalArgumentException e) {
            throw new IncorrectStatus("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
