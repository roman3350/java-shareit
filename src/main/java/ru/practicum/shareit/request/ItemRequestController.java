package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

/**
 * Контроллер запросов вещей
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    /**
     * Создать запрос на вещь
     *
     * @param userId      ID пользователя
     * @param itemRequest Запрос на вещь
     * @return Запрос на вещь
     */
    @PostMapping
    public ItemRequest requestCreate(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @RequestBody ItemRequest itemRequest) {
        return itemRequestService.requestCreate(userId, itemRequest);
    }

    /**
     * Поиск запросов по ID пользователя
     * @param userId ID пользователя
     * @return List запросов пользователя
     */
    @GetMapping
    public List<ItemRequestDto> findRequestByIdUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.findRequestByIdUser(userId);
    }

    /**
     * Поиск всех запросов
     * @param userId ID пользователя
     * @param from с какого запроса осуществлять поиск
     * @param size Количество запросов на вывод
     * @return List запросов
     */
    @GetMapping("/all")
    public List<ItemRequestDto> findAllRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        return itemRequestService.findAllRequest(userId, from, size);
    }

    /**
     * Поиск запроса по ID запроса
     * @param userId ID пользователя
     * @param requestId ID запроса
     * @return запрос
     */
    @GetMapping("/{requestId}")
    private ItemRequestDto findById(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @PathVariable long requestId) {
        return itemRequestService.findById(userId, requestId);
    }
}
