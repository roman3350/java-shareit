package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.*;

public interface UserRepository {
    List<User> findAll();

    User findUserById(long userId);

    User create(User user);

    User update(long userId, User user);

    void delete(long userId);
}
