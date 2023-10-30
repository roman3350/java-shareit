package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    /**
     * Вывод продукта по ID
     *
     * @param itemId ID продукта
     * @return Продукт
     */
    ItemDto findItemByIdItem(long itemId, long userId);

    /**
     * Вывод продуктов выложенных пользователем
     *
     * @param userId ID пользователя
     * @return List продуктов
     */
    List<ItemDto> findItemByIdUser(long userId, int from, int size);

    /**
     * Поиск продукта по названию и описанию
     *
     * @param nameItem Название или описание продукта
     * @return Продукт
     */
    List<ItemDto> search(String nameItem, int from, int size);

    /**
     * Создание продукта
     *
     * @param userId       ID пользователя, добавляющего продукт
     * @param itemDtoShort Продукт
     * @return Созданный продукт
     */
    ItemDto create(long userId, ItemDtoShort itemDtoShort);

    /**
     * Обновление продукта
     *
     * @param userId ID пользователя, который добавил продукт
     * @param itemId ID продукта
     * @param item   Продукт
     * @return Обновленный продукт
     */
    ItemDto update(long userId, long itemId, Item item);

    /**
     * Создание комментария
     *
     * @param userId  ID пользователя создающего комментарий
     * @param itemId  ID комментируемого предмета
     * @param comment Комментарий
     * @return Комментарий
     */
    CommentDto createComment(long userId, long itemId, Comment comment);

    /**
     * Удаление продукта
     *
     * @param itemId ID продукта
     */
    void delete(long itemId);
}
