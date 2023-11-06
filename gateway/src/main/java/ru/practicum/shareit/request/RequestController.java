package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {

    private final RequestClient requestClient;

    /**
     * Запрос на создание запроса на вещь
     * @param userId ID пользователя
     * @param requestDto Запрос
     * @return Созданный запрос на вещь
     */
    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestBody @Valid RequestDto requestDto) {
        log.info("Creating request {}, userId={}", requestDto, userId);
        return requestClient.createRequest(userId, requestDto);
    }

    /**
     * Запрос на вывод запроса на вещь по ID пользователя
     * @param userId ID пользователя
     * @return Запросы на вещи пользователя
     */
    @GetMapping
    public ResponseEntity<Object> getRequestByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get request by user id {}", userId);
        return requestClient.getRequestByUserId(userId);
    }

    /**
     * Запрос на вывод всех запросов
     * @param userId ID пользователя
     * @param from С какого элемента выводить
     * @param size Количество элементов на странице
     * @return Запросы на вещи
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequestByUserId(@RequestHeader("X-Sharer-User-Id")
                                                        long userId,
                                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                        Integer from,
                                                        @Positive @RequestParam(name = "size", defaultValue = "10")
                                                        Integer size) {
        log.info("Get request all by user id {}, from {}, size {}", userId, from, size);
        return requestClient.getAllRequestByUserId(userId, from, size);
    }

    /**
     * Запрос на вывод запроса на вещь по ID запроса на вещь
     * @param userId ID пользователя
     * @param requestId ID запроса на вещь
     * @return Запрос на вещь
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestByRequestId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                        @PathVariable Long requestId) {
        log.info("Get booking {}, userId={}", requestId, userId);
        return requestClient.getRequestByRequestId(userId, requestId);
    }

}
