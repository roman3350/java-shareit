package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    /**
     * Вывод всех пользователей
     *
     * @return List с пользователями
     */
    List<User> findAll();

    /**
     * Вывод пользователя по ID
     *
     * @param userId ID пользователя
     * @return Пользователь
     */
    User findUserById(long userId);

    /**
     * Создание пользователя
     *
     * @param user данные пользователя
     * @return созданный пользователь
     */
    User create(User user);

    /**
     * Обновление пользователя
     *
     * @param userId ID пользователя, которого надо обновить
     * @param user   данные пользователя на обновление
     * @return обновленный пользователь
     */
    User update(long userId, User user);

    /**
     * Удаление пользователя
     *
     * @param userId ID пользователя, которого надо удалить
     */
    void delete(long userId);
}
