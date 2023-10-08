package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utilites.Validation.*;

@Component
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final UserRepository userRepository;
    private final List<Item> items = new ArrayList<>();
    private long id;

    @Override
    public Item findItemByIdItem(long itemId) {
        return items.stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst().get();
    }

    @Override
    public List<Item> findItemByIdUser(long userId) {
        return items.stream()
                .filter(item -> item.getOwner().equals(userRepository.findUserById(userId)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> search(String nameItem) {
        if (nameItem.isEmpty()) {
            return List.of();
        }
        return items.stream()
                .filter(item -> item.getName().toLowerCase().contains(nameItem.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(nameItem.toLowerCase()) &&
                                item.getAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public Item create(long userId, Item item) {
        validationItem(item);
        item.setOwner(userRepository.findUserById(userId));
        item.setId(++id);
        items.add(item);
        return item;
    }

    @Override
    public Item update(long userId, long itemId, Item item) {
        items.stream()
                .filter(item1 -> item1.getId() == itemId)
                .findFirst()
                .map(item1 -> {
                    validationIncorrectOwner(item1, userRepository.findUserById(userId));
                    if (item.getName() != null) {
                        item1.setName(item.getName());
                    }
                    if (item.getDescription() != null) {
                        item1.setDescription(item.getDescription());
                    }
                    if (item.getAvailable() != null) {
                        item1.setAvailable(item.getAvailable());
                    }
                    return items;
                });
        return findItemByIdItem(itemId);
    }

    @Override
    public void delete(long itemId) {
        items.removeIf(item -> item.getId().equals(itemId));
    }
}
