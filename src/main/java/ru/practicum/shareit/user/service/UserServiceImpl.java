package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User findUserById(long userId) {
        return repository.findUserById(userId);
    }

    @Override
    public User create(User user) {
        return repository.create(user);
    }

    @Override
    public User update(long userId, User user) {
        return repository.update(userId, user);
    }

    @Override
    public void delete(long userId) {
        repository.delete(userId);
    }
}
