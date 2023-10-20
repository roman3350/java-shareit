package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;

    @Override
    public Item findItemByIdItem(long itemId) {
        return repository.findItemByIdItem(itemId);
    }

    @Override
    public List<Item> findItemByIdUser(long userId) {
        return repository.findItemByIdUser(userId);
    }

    @Override
    public List<Item> search(String nameItem) {
        return repository.search(nameItem);
    }

    @Override
    public Item create(long userId, Item item) {
        return repository.create(userId, item);
    }

    @Override
    public Item update(long userId, long itemId, Item item) {
        return repository.update(userId, itemId, item);
    }

    @Override
    public void delete(long itemId) {
        repository.delete(itemId);
    }
}
