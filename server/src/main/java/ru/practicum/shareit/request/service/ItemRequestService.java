package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    /**
     * Создать запрос на вещь
     *
     * @param userId      ID пользователя
     * @param itemRequest Запрос на вещь
     * @return Запрос на вещь
     */
    ItemRequest requestCreate(long userId, ItemRequest itemRequest);

    /**
     * Поиск запросов по ID пользователя
     *
     * @param userId ID пользователя
     * @return List запросов пользователя
     */
    List<ItemRequestDto> findRequestByIdUser(long userId);

    /**
     * Поиск всех запросов
     *
     * @param userId ID пользователя
     * @param from   с какого запроса осуществлять поиск
     * @param size   Количество запросов на вывод
     * @return List запросов
     */
    List<ItemRequestDto> findAllRequest(long userId, int from, int size);

    /**
     * Поиск запроса по ID запроса
     *
     * @param userId    ID пользователя
     * @param requestId ID запроса
     * @return запрос
     */
    ItemRequestDto findById(long userId, long requestId);
}
