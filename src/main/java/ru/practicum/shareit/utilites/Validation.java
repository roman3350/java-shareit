package ru.practicum.shareit.utilites;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.exception.*;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Slf4j
public class Validation {
    public static void validationUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("электронная почта пустая или не содержит символ @");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
    }

    public static void validationDuplicateEmail(User user, List<User> users) {
        if (users.stream()
                .anyMatch(user1 -> {
                    if (user1.getId().equals(user.getId()) && user1.getEmail().equals(user.getEmail())) {
                        return false;
                    }
                    return user1.getEmail().equals(user.getEmail());
                })) {
            log.warn("Пользователь с почта {} уже зарегистрирован", user.getEmail());
            throw new duplicateEmailException("Пользователь с такой почтой уже зарегистрирован");
        }
    }

    public static void validationIncorrectOwner(Item item, User user) {
        if (!item.getOwner().equals(user)) {
            log.warn("У пользователя {} нет Item с ID {}", user.getId(), item.getId());
            throw new IncorrectOwner("Продукт не найден");
        }
    }

    public static void validationFindOwner(long userID, Optional<User> user) {
        if (user.isEmpty()) {
            log.warn("Пользователя с ID {} не существует", userID);
            throw new IncorrectOwner("Пользователь не найден");
        }
    }

    public static void validationItem(Item item) {
        if (item.getName().isEmpty() || item.getAvailable() == null || item.getDescription() == null) {
            log.warn("Некорректный элемент");
            throw new ValidationItemException("Некорректный продукт");
        }
    }
}
