package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.utilites.Validation.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    /**
     * Вывод всех пользователей
     *
     * @return List с пользователями
     */
    public List<User> findAll(int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return repository.findAll(page).getContent();
    }

    /**
     * Вывод пользователя по ID
     *
     * @param userId ID пользователя
     * @return Пользователь
     */
    @Override
    public User findUserById(long userId) {
        Optional<User> user = repository.findById(userId);
        validationFindOwner(userId, user);
        return user.get();
    }

    /**
     * Создание пользователя
     *
     * @param user данные пользователя
     * @return созданный пользователь
     */
    @Override
    public User create(User user) {
        return repository.save(user);
    }

    /**
     * Обновление пользователя
     *
     * @param userId ID пользователя, которого надо обновить
     * @param user   данные пользователя на обновление
     * @return обновленный пользователь
     */
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

    /**
     * Удаление пользователя
     *
     * @param userId ID пользователя, которого надо удалить
     */
    @Override
    public void delete(long userId) {
        validationFindOwner(userId, repository.findById(userId));
        repository.deleteById(userId);
    }
}
