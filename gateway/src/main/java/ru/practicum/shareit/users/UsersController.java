package ru.practicum.shareit.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.users.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UsersController {

    private final UsersClient usersClient;

    /**
     * Запрос на вывод всех пользователей
     * @param from С какого элемента выводить
     * @param size Количество элементов на странице
     * @return Пользователи
     */
    @GetMapping
    public ResponseEntity<Object> findAll(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                          Integer from,
                                          @Positive @RequestParam(name = "size", defaultValue = "10")
                                          Integer size) {
        log.info("Get user all, from={}, size={}", from, size);
        return usersClient.getAllUsers(from, size);
    }

    /**
     * Запрос на вывод пользователя по ID пользователя
     * @param userId ID пользователя
     * @return Пользователь
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserByUserId(@PathVariable long userId) {
        log.info("Get user by user id {}", userId);
        return usersClient.getUserByUserId(userId);
    }

    /**
     * Запрос на создание пользователя
     * @param userDto Пользователь
     * @return Созданный пользователь
     */
    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDto userDto) {
        return usersClient.createUser(userDto);
    }

    /**
     * Запрос на обновление пользователя
     * @param id ID пользователя
     * @param userDto Данные на обновление пользователя
     * @return Обновленный пользователь
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable long id, @RequestBody UserDto userDto) {
        return usersClient.updateUser(id, userDto);
    }

    /**
     * Запрос на удаление пользователя
     * @param id ID пользователя
     * @return Статус удаление
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        return usersClient.deleteUser(id);
    }
}
