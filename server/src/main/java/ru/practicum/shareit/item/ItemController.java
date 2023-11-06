package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * Контроллер предметов
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    /**
     * Вывод продукта по ID
     *
     * @param itemId ID продукта
     * @return Продукт
     */
    @GetMapping("/{itemId}")
    public ItemDto findItemByIdItem(@PathVariable long itemId,
                                    @RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return itemService.findItemByIdItem(itemId, userId);
    }

    /**
     * Вывод продуктов выложенных пользователем
     *
     * @param userId ID пользователя
     * @return List продуктов
     */
    @GetMapping
    public List<ItemDto> findItemByIdUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size) {
        return itemService.findItemByIdUser(userId, from, size);
    }

    /**
     * Поиск продукта по названию и описанию
     *
     * @param text Название или описание продукта
     * @return Продукт
     */
    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text,
                                @RequestParam(defaultValue = "0") int from,
                                @RequestParam(defaultValue = "10") int size) {
        return itemService.search(text, from, size);
    }

    /**
     * Создание продукта
     *
     * @param userId ID пользователя, добавляющего продукт
     * @param item   Продукт
     * @return Созданный продукт
     */
    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemDtoShort item) {
        return itemService.create(userId, item);
    }

    /**
     * Обновление продукта
     *
     * @param userId ID пользователя, который добавил продукт
     * @param itemId ID продукта
     * @param item   Продукт
     * @return Обновленный продукт
     */
    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable long itemId,
                          @RequestBody Item item) {
        return itemService.update(userId, itemId, item);
    }

    /**
     * Создание комментария
     *
     * @param userId  ID пользователя создающего комментарий
     * @param itemId  ID комментируемого предмета
     * @param comment Комментарий
     * @return Комментарий
     */
    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @PathVariable long itemId,
                                    @RequestBody Comment comment) {
        return itemService.createComment(userId, itemId, comment);
    }

    /**
     * Удаление продукта
     *
     * @param itemId ID продукта
     */
    @DeleteMapping
    public void delete(@PathVariable long itemId) {
        itemService.delete(itemId);
    }
}
