package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utilites.Validation.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    /**
     * Вывод продукта по ID
     *
     * @param itemId ID продукта
     * @return Продукт
     */
    @Override
    public ItemDto findItemByIdItem(long itemId, long userId) {
        Optional<Item> item = itemRepository.findById(itemId);
        validationFindItem(itemId, item);
        if (userId == item.get().getUser().getId()) {
            return ItemMapper.mapToItemDtoOwner(itemRepository.findById(itemId).get(),
                    bookingRepository.findFirst1ByItemIdAndStartBeforeAndStatusNotOrderByStartDesc(itemId,
                            LocalDateTime.now(),
                            BookingStatus.REJECTED),
                    bookingRepository.findFirst1ByItemIdAndStartAfterAndStatusNotOrderByStart(itemId,
                            LocalDateTime.now(),
                            BookingStatus.REJECTED),
                    CommentMapper.mapToCommentDto(commentRepository.findAllByItemId(itemId)));
        }
        return ItemMapper.mapToItemCommentDto(itemRepository.findById(itemId).get(),
                CommentMapper.mapToCommentDto(commentRepository.findAllByItemId(itemId)));
    }

    /**
     * Вывод продуктов выложенных пользователем
     *
     * @param userId ID пользователя
     * @return List продуктов
     */
    @Override
    public List<ItemDto> findItemByIdUser(long userId) {
        return itemRepository.findByUserId(userId).stream()
                .map(item -> ItemMapper.mapToItemDtoOwner(item,
                        bookingRepository.findFirst1ByItemIdAndStartBeforeAndStatusNotOrderByStartDesc(item.getId(),
                                LocalDateTime.now(),
                                BookingStatus.REJECTED),
                        bookingRepository.findFirst1ByItemIdAndStartAfterAndStatusNotOrderByStart(item.getId(),
                                LocalDateTime.now(),
                                BookingStatus.REJECTED),
                        CommentMapper.mapToCommentDto(commentRepository.findAllByItemId(item.getId()))))
                .collect(Collectors.toList());
    }

    /**
     * Поиск продукта по названию и описанию
     *
     * @param nameItem Название или описание продукта
     * @return Продукт
     */
    @Override
    public List<ItemDto> search(String nameItem) {
        if (nameItem.isEmpty()) {
            return List.of();
        }
        return ItemMapper.mapToItemDto(itemRepository.findByNameContainingIgnoreCase(nameItem));
    }

    /**
     * Создание продукта
     *
     * @param userId ID пользователя, добавляющего продукт
     * @param item   Продукт
     * @return Созданный продукт
     */
    @Override
    public ItemDto create(long userId, Item item) {
        Optional<User> user = userRepository.findById(userId);
        validationItem(item);
        validationFindOwner(userId, user);
        item.setUser(user.get());
        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    /**
     * Обновление продукта
     *
     * @param userId ID пользователя, который добавил продукт
     * @param itemId ID продукта
     * @param item   Продукт
     * @return Обновленный продукт
     */
    @Override
    public ItemDto update(long userId, long itemId, Item item) {
        Item itemUpdate = itemRepository.findById(itemId).get();
        validationIncorrectOwner(itemUpdate, userRepository.findById(userId).get());
        if (item.getName() != null) {
            itemUpdate.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemUpdate.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemUpdate.setAvailable(item.getAvailable());
        }
        return ItemMapper.mapToItemDto(itemRepository.save(itemUpdate));
    }

    /**
     * Создание комментария
     *
     * @param userId  ID пользователя создающего комментарий
     * @param itemId  ID комментируемого предмета
     * @param comment Комментарий
     * @return Комментарий
     */
    @Override
    public CommentDto createComment(long userId, long itemId, Comment comment) {
        validationComment(comment.getText());
        Optional<Booking> booking = bookingRepository.findFirst1ByBookerIdAndItemId(userId, itemId);
        Optional<User> user = userRepository.findById(userId);
        validationBooking(userId, itemId, booking);
        validationFindOwner(userId, user);
        Optional<Item> item = itemRepository.findById(itemId);
        validationFindItem(itemId, item);
        comment.setItem(item.get());
        comment.setAuthor(user.get());
        comment.setCreate(LocalDateTime.now());
        return CommentMapper.mapToCommentDto(commentRepository.save(comment));
    }

    /**
     * Удаление продукта
     *
     * @param itemId ID продукта
     */
    @Override
    public void delete(long itemId) {
        itemRepository.deleteById(itemId);
    }
}
