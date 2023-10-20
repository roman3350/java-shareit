package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findUserById(long userId);

    User create(User user);

    User update(long userId, User user);

    void delete(long userId);
}
