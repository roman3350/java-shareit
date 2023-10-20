package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.*;

import static ru.practicum.shareit.utilites.Validation.*;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final List<User> users = new ArrayList<>();
    private long id;

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public User findUserById(long userId) {
        Optional<User> user = users.stream()
                .filter(users -> users.getId() == userId)
                .findFirst();
        validationFindOwner(userId, user);
        return user.get();
    }

    @Override
    public User create(User user) {
        validationUser(user);
        validationDuplicateEmail(user, users);
        user.setId(++id);
        users.add(user);
        return user;
    }

    @Override
    public User update(long userId, User user) {
        users.stream()
                .filter(user1 -> user1.getId() == userId)
                .findFirst()
                .map(user1 -> {
                    if (user.getName() != null) {
                        user1.setName(user.getName());
                    }
                    if (user.getEmail() != null) {
                        user.setId(userId);
                        validationDuplicateEmail(user, users);
                        user1.setEmail(user.getEmail());
                    }
                    return users;
                });
        return findUserById(userId);
    }

    @Override
    public void delete(long userId) {
        users.removeIf(user -> user.getId().equals(userId));
    }
}
