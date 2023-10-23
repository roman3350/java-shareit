package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select it " +
            "from Item as it " +
            "where (lower(it.name) like lower(concat('%', ?1,'%')) " +
            "or lower(it.description)like lower(concat('%', ?1,'%'))) " +
            "and it.available = true")
    List<Item> findByNameContainingIgnoreCase(String nameSearch);

    List<Item> findByUserId(long userId);
}
