package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto findItemByIdItem(long itemId, long userId);

    List<ItemDto> findItemByIdUser(long userId);

    List<ItemDto> search(String nameItem);

    ItemDto create(long userId, Item item);

    ItemDto update(long userId, long itemId, Item item);

    CommentDto createComment(long userId, long itemId, Comment comment);

    void delete(long itemId);
}
