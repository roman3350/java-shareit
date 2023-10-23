package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.utilites.Validation.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    public List<User> findAll() {
        List<User> users = repository.findAll();
        return users;
    }

    @Override
    public User findUserById(long userId) {
        Optional<User> user = repository.findById(userId);
        validationFindOwner(userId, user);
        return user.get();
    }

    @Override
    public User create(User user) {
        validationUser(user);
        return repository.save(user);
    }

    @Override
    public User update(long userId, User user) {
        User userUpdate = repository.findById(userId).get();
        if (user.getName() != null) {
            userUpdate.setName(user.getName());
        }
        if (user.getEmail() != null) {
            userUpdate.setEmail(user.getEmail());
        }
        return repository.save(userUpdate);
    }

    @Override
    public void delete(long userId) {
        repository.deleteById(userId);
    }
}
