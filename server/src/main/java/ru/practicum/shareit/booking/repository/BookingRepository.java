package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b " +
            "from Booking as b " +
            "join b.booker as u " +
            "where u.id = ?1 and b.status = ?2 " +
            "order by b.end desc ")
    List<Booking> findAllByBookerIdAndStatus(long userId, BookingStatus state, PageRequest page);

    @Query("select b " +
            "from Booking as b " +
            "join b.booker as u " +
            "where u.id = ?1 " +
            "order by b.end desc ")
    List<Booking> findAllByBookerId(long userId, PageRequest page);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.user as u " +
            "where u.id = ?1 " +
            "order by b.end desc ")
    List<Booking> findAllByOwnerId(long userId, PageRequest page);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.user as u " +
            "where u.id = ?1 and b.end < ?2 " +
            "order by b.end desc ")
    List<Booking> findAllByOwnerIdAndEndBefore(long bookerId, LocalDateTime time, PageRequest page);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.user as u " +
            "where u.id = ?1 and b.status = ?2 " +
            "order by b.end desc ")
    List<Booking> findAllByOwnerIdAndStatus(long userId, BookingStatus state, PageRequest page);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.user as u " +
            "where u.id = ?1 and b.start < ?2 and b.end > ?2 " +
            "order by b.end desc ")
    List<Booking> findAllByOwnerIdAndStartBeforeAndEndAfter(long bookerId, LocalDateTime time, PageRequest page);

    List<Booking> findAllByBookerIdAndStatusInAndStartAfterOrderByStartDesc(long userId,
                                                                            List<BookingStatus> status,
                                                                            LocalDateTime time,
                                                                            PageRequest page);

    @Query("select b " +
            "from Booking as b " +
            "join b.item as i " +
            "join i.user as u " +
            "where u.id = ?1 and b.status in (?2, ?3) and b.start > ?4 " +
            "order by b.start desc ")
    List<Booking> findAllByOwnerIdAndStatusInAndStartBeforeOrderByStartDesc(long userId,
                                                                            BookingStatus status1,
                                                                            BookingStatus status2,
                                                                            LocalDateTime time,
                                                                            PageRequest page);

    Booking findFirst1ByItemIdAndStartBeforeAndStatusNotOrderByStartDesc(long id,
                                                                         LocalDateTime time,
                                                                         BookingStatus status);

    Booking findFirst1ByItemIdAndStartAfterAndStatusNotOrderByStart(long id,
                                                                    LocalDateTime time,
                                                                    BookingStatus status);

    Optional<Booking> findFirst1ByBookerIdAndItemId(long bookerId, long itemId);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(long bookerId,
                                                             LocalDateTime time,
                                                             LocalDateTime time1,
                                                             PageRequest page);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByEndDesc(long bookerId, LocalDateTime time, PageRequest page);
}
