package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemsController {

    private final ItemsClient itemsClient;

    /**
     * Запрос на вывод вещи по ID вещи
     * @param userId ID пользователя
     * @param itemId ID вещи
     * @return Вещь
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable Long itemId) {
        log.info("Get item {}, userId={}", itemId, userId);
        return itemsClient.getItem(userId, itemId);
    }

    /**
     * Запрос на вывод вещей по ID пользователя
     * @param userId ID пользователя
     * @param from С какого элемента выводить
     * @param size Количество элементов на странице
     * @return Вещи
     */
    @GetMapping
    public ResponseEntity<Object> getItemsByUserId(@RequestHeader("X-Sharer-User-Id")
                                                   long userId,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                   Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "10")
                                                   Integer size) {
        log.info("Get item by user id {}, from={}, size={}", userId, from, size);
        return itemsClient.getItemsByUserId(userId, from, size);
    }

    /**
     * Запрос на поиск вещи
     * @param userId ID пользователя
     * @param text Критерий поиска
     * @param from С какого элемента выводить
     * @param size Количество элементов на странице
     * @return
     */
    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam String text,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                         Integer from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10")
                                         Integer size) {
        log.info("Get search item {}, userId {}, from {}, size {}", text, userId, from, size);
        return itemsClient.search(userId, text, from, size);
    }

    /**
     * Запрос на создание вещи
     * @param userId ID пользователя
     * @param itemDto Вещь
     * @return Созданная вещь
     */
    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody @Valid ItemDto itemDto) {
        log.info("Creating item {}, userId={}", itemDto, userId);
        return itemsClient.createItem(userId, itemDto);
    }

    /**
     * Запрос на обновление вещи
     * @param userId ID пользователя
     * @param itemId ID вещи
     * @param itemDto Вещь
     * @return Обновленная вещь
     */
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId,
                                             @RequestBody ItemDto itemDto) {
        log.info("Update item {}, itemId {}, userId {}", itemDto, itemId, userId);
        return itemsClient.updateItem(userId, itemId, itemDto);
    }

    /**
     * Запрос на создание комментария
     * @param userId ID пользователя
     * @param itemId ID вещи
     * @param commentDto Комментарий
     * @return Созданный комментарий
     */
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @PathVariable long itemId,
                                                @RequestBody @Valid CommentDto commentDto) {
        log.info("Create comment {}, userId {}, itemId {}", commentDto, userId, itemId);
        return itemsClient.createComment(userId, itemId, commentDto);
    }

    /**
     * Запрос на удаление вещи
     * @param itemId ID вещи
     */
    @DeleteMapping
    public void delete(@PathVariable long itemId) {
        log.info("Delete item {}", itemId);
        itemsClient.deleteItem(itemId);
    }
}
