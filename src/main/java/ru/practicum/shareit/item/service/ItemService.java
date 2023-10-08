package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item findItemByIdItem(long itemId);

    List<Item> findItemByIdUser(long userId);

    List<Item> search(String nameItem);

    Item create(long userId, Item item);

    Item update(long userId, long itemId, Item item);

    void delete(long itemId);
}
