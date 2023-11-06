package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.LastBooking;
import ru.practicum.shareit.booking.dto.NextBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    public static ItemDto mapToItemDto(Item item) {
        if (item.getRequest() == null) {
            return new ItemDto(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable()
            );
        } else {
            return new ItemDto(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    item.getRequest().getId()
            );
        }
    }

    public static ItemDto mapToItemCommentDto(Item item, List<CommentDto> comments) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                comments);
    }

    public static List<ItemDto> mapToItemDto(Iterable<Item> items) {
        List<ItemDto> dtos = new ArrayList<>();
        for (Item item : items) {
            dtos.add(mapToItemDto(item));
        }
        return dtos;
    }

    public static ItemDto mapToItemDtoOwner(Item item,
                                            Booking bookingLast,
                                            Booking bookingNext,
                                            List<CommentDto> comment) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                bookingLast == null ? null : new LastBooking(bookingLast.getId(), bookingLast.getBooker().getId()),
                bookingNext == null ? null : new NextBooking(bookingNext.getId(), bookingNext.getBooker().getId()),
                comment,
                item.getRequest() == null ? null : item.getRequest().getId()
        );
    }
}
