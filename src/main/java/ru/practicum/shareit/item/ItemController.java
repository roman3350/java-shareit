package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
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
    public Item findItemByIdItem(@PathVariable long itemId) {
        return itemService.findItemByIdItem(itemId);
    }

    /**
     * Вывод продуктов выложенных пользователем
     *
     * @param userId ID пользователя
     * @return List продуктов
     */
    @GetMapping
    public List<Item> findItemByIdUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.findItemByIdUser(userId);
    }

    /**
     * Поиск продукта по названию и описанию
     *
     * @param text Название или описание продукта
     * @return Продукт
     */
    @GetMapping("/search")
    public List<Item> search(@RequestParam String text) {
        return itemService.search(text);
    }

    /**
     * Создание продукта
     *
     * @param userId ID пользователя, добавляющего продукт
     * @param item   Продукт
     * @return Созданный продукт
     */
    @PostMapping
    public Item create(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody Item item) {
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
    public Item update(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId, @RequestBody Item item) {
        return itemService.update(userId, itemId, item);
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
