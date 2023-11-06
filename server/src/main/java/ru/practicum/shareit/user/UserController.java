package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * Контроллер пользователей
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Вывод всех пользователей
     *
     * @return List с пользователями
     */
    @GetMapping
    public List<User> findAll(@RequestParam(defaultValue = "0") int from,
                              @RequestParam(defaultValue = "10") int size) {
        return userService.findAll(from, size);
    }

    /**
     * Вывод пользователя по ID
     *
     * @param id ID пользователя
     * @return Пользователь
     */
    @GetMapping("/{id}")
    public User findUserById(@PathVariable long id) {
        return userService.findUserById(id);
    }

    /**
     * Создание пользователя
     *
     * @param user данные пользователя
     * @return созданный пользователь
     */
    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    /**
     * Обновление пользователя
     *
     * @param id   ID пользователя, которого надо обновить
     * @param user данные пользователя на обновление
     * @return обновленный пользователь
     */
    @PatchMapping("/{id}")
    public User update(@PathVariable long id, @RequestBody User user) {
        return userService.update(id, user);
    }

    /**
     * Удаление пользователя
     *
     * @param id ID пользователя, которого надо удалить
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        userService.delete(id);
    }
}
