package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
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
    private final ItemRequestRepository itemRequestRepository;

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
    public List<ItemDto> findItemByIdUser(long userId, int from, int size) {
        validationPage(from, size);
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return itemRepository.findAllByUserId(userId, page).stream()
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
    public List<ItemDto> search(String nameItem, int from, int size) {
        validationPage(from, size);
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        if (nameItem.isEmpty()) {
            return List.of();
        }
        return ItemMapper.mapToItemDto(itemRepository.findAllByNameContainingIgnoreCase(nameItem, page));
    }

    /**
     * Создание продукта
     *
     * @param userId       ID пользователя, добавляющего продукт
     * @param itemDtoShort Продукт
     * @return Созданный продукт
     */
    @Override
    public ItemDto create(long userId, ItemDtoShort itemDtoShort) {
        Optional<User> user = userRepository.findById(userId);
        validationItem(itemDtoShort);
        validationFindOwner(userId, user);
        Item item = Item.builder()
                .name(itemDtoShort.getName())
                .description(itemDtoShort.getDescription())
                .available(itemDtoShort.getAvailable())
                .request(itemDtoShort.getRequestId() == null ? null :
                        itemRequestRepository.findById(itemDtoShort.getRequestId()).get())
                .build();
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
