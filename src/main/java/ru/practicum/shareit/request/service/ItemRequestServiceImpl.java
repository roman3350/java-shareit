package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.utilites.Validation.*;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    /**
     * Создать запрос на вещь
     *
     * @param userId      ID пользователя
     * @param itemRequest Запрос на вещь
     * @return Запрос на вещь
     */
    @Override
    public ItemRequest requestCreate(long userId, ItemRequest itemRequest) {
        Optional<User> user = userRepository.findById(userId);
        validationFindOwner(userId, user);
        validationRequest(itemRequest);
        itemRequest.setRequestor(user.get());
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequestRepository.save(itemRequest);
    }

    /**
     * Поиск запросов по ID пользователя
     *
     * @param userId ID пользователя
     * @return List запросов пользователя
     */
    @Override
    public List<ItemRequestDto> findRequestByIdUser(long userId) {
        validationFindOwner(userId, userRepository.findById(userId));
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorIdOrderByCreated(userId);
        return itemRequestDtosCreateList(itemRequests);
    }

    /**
     * Поиск всех запросов
     *
     * @param userId ID пользователя
     * @param from   с какого запроса осуществлять поиск
     * @param size   Количество запросов на вывод
     * @return List запросов
     */
    @Override
    public List<ItemRequestDto> findAllRequest(long userId, int from, int size) {
        validationPage(from, size);
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestorIdNot(userId, page).getContent();
        return itemRequestDtosCreateList(itemRequests);
    }

    /**
     * Поиск запроса по ID запроса
     *
     * @param userId    ID пользователя
     * @param requestId ID запроса
     * @return запрос
     */
    @Override
    public ItemRequestDto findById(long userId, long requestId) {
        Optional<ItemRequest> itemRequest = itemRequestRepository.findById(requestId);
        validationRequestId(requestId, itemRequest);
        validationFindOwner(userId, userRepository.findById(userId));
        return ItemRequestMapper.mapToItemDto(itemRequest.get(),
                ItemMapper.mapToItemDto(itemRepository.findByRequestId(itemRequest.get().getId())));
    }

    /**
     * Добавляет к запросам вещи, которые добавили по запросу
     *
     * @param itemRequests Запрос
     * @return Запрос с вещью
     */
    private List<ItemRequestDto> itemRequestDtosCreateList(List<ItemRequest> itemRequests) {
        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            itemRequestDtos.add(ItemRequestMapper.mapToItemDto(itemRequest,
                    ItemMapper.mapToItemDto(itemRepository.findByRequestId(itemRequest.getId()))));
        }
        return itemRequestDtos;
    }
}
