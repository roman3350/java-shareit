package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequestDto {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
    private List<ItemDto> items = new ArrayList<>();

    public ItemRequestDto(Long id,
                          String description,
                          User requestor,
                          LocalDateTime created,
                          List<ItemDto> items) {
        this.id = id;
        this.description = description;
        this.requestor = requestor;
        this.created = created;
        this.items = items;
    }
}
