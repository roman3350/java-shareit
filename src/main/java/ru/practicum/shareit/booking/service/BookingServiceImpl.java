package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
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

    @Override
    public Booking create(long userId, BookingDto bookingDto) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Item> item = itemRepository.findById(bookingDto.getItemId());
        validationFindOwner(userId, user);
        validationFindItem(bookingDto.getItemId(), item);
        validationTimeRequest(bookingDto);
        Booking booking = BookingMapper.mapToBooking(bookingDto, item.get(), user.get());
        validationOwner(userId, item.get());
        validationItemAvailable(item.get());
        return bookingRepository.save(booking);
    }

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

    @Override
    public Booking findBookingById(long userId, long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        validationFindBooking(bookingId, booking);
        validationOwnerOrAuthorBooking(userId, booking.get());
        return booking.get();
    }

    @Override
    public List<Booking> findBookingAuthor(long userId, String state) {
        try {
            BookingStatus status = BookingStatus.valueOf(state);
            validationFindOwner(userId, userRepository.findById(userId));
            switch (status) {
                case ALL:
                    return bookingRepository.findByBookerId(userId);
                case PAST:
                    return bookingRepository.findByBookerIdAndEndBeforeOrderByEndDesc(userId,
                            LocalDateTime.now());
                case FUTURE:
                    return bookingRepository.findByBookerIdAndStatusInAndStartAfterOrderByStartDesc(userId,
                            List.of(BookingStatus.APPROVED, BookingStatus.WAITING),
                            LocalDateTime.now());
                case CURRENT:
                    return bookingRepository.findByBookerIdAndStartBeforeAndEndAfter(userId,
                            LocalDateTime.now(),
                            LocalDateTime.now());
                case WAITING:
                    return bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.WAITING);
                case APPROVED:
                    return bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.APPROVED);
                case CANCELED:
                    return bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.CANCELED);
                case REJECTED:
                    return bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED);
                default:
                    throw new IncorrectStatus("Unknown state: UNSUPPORTED_STATUS");
            }
        } catch (IllegalArgumentException e) {
            throw new IncorrectStatus("Unknown state: UNSUPPORTED_STATUS");
        }

    }

    @Override
    public List<Booking> findBookingOwner(long userId, String state) {
        try {
            BookingStatus status = BookingStatus.valueOf(state);
            validationFindOwner(userId, userRepository.findById(userId));
            switch (status) {
                case ALL:
                    return bookingRepository.findByOwnerId(userId);
                case PAST:
                    return bookingRepository.findByOwnerIdAndEndBefore(userId,
                            LocalDateTime.now());
                case FUTURE:
                    return bookingRepository.findByOwnerIdAndStatusInAndStartBeforeOrderByStartDesc(userId,
                            BookingStatus.APPROVED,
                            BookingStatus.WAITING,
                            LocalDateTime.now());
                case CURRENT:
                    return bookingRepository.findByOwnerIdAndStartBeforeAndEndAfter(userId,
                            LocalDateTime.now());
                case WAITING:
                    return bookingRepository.findByOwnerIdAndStatus(userId, BookingStatus.WAITING);
                case APPROVED:
                    return bookingRepository.findByOwnerIdAndStatus(userId, BookingStatus.APPROVED);
                case CANCELED:
                    return bookingRepository.findByOwnerIdAndStatus(userId, BookingStatus.CANCELED);
                case REJECTED:
                    return bookingRepository.findByOwnerIdAndStatus(userId, BookingStatus.REJECTED);
                default:
                    throw new IncorrectStatus("Unknown state: UNSUPPORTED_STATUS");
            }
        } catch (IllegalArgumentException e) {
            throw new IncorrectStatus("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
