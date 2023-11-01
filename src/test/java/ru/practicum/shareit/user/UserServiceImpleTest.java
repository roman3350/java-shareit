package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@Rollback(value = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"db.name=test"})
@DataJpaTest
@ComponentScan("ru.practicum.shareit.user.service")
public class UserServiceImpleTest {
    private final EntityManager em;
    private final UserService service;

    @Test
    void saveUser() {
        User user = makeUser("Петр Иванович", "some@email.com");

        service.create(user);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User userDB = query.setParameter("email", user.getEmail())
                .getSingleResult();

        assertThat(userDB.getId(), notNullValue());
        assertThat(userDB.getName(), equalTo(user.getName()));
        assertThat(userDB.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void findAll() {
        User user = makeUser("Петр Иванович", "some@email.com");

        service.create(user);
        List<User> users = service.findAll(0, 10);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User userDB = query.setParameter("email", user.getEmail())
                .getSingleResult();

        assertThat(userDB.getId(), equalTo(users.get(0).getId()));
        assertThat(userDB.getName(), equalTo(users.get(0).getName()));
        assertThat(userDB.getEmail(), equalTo(users.get(0).getEmail()));
    }

    @Test
    void findUserById() {
        User user = makeUser("Петр Иванович", "some@email.com");

        long id = service.create(user).getId();
        User users = service.findUserById(id);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        User userDB = query.setParameter("id", id)
                .getSingleResult();

        assertThat(userDB.getId(), equalTo(users.getId()));
        assertThat(userDB.getName(), equalTo(users.getName()));
        assertThat(userDB.getEmail(), equalTo(users.getEmail()));
    }

    @Test
    void updateUser() {
        User user = makeUser("Петр Иванович", "some@email.com");
        User userUpdate = makeUser("Иван Петрович", "some@gmail.com");

        long id = service.create(user).getId();
        service.update(id, userUpdate);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User userDB = query.setParameter("email", userUpdate.getEmail())
                .getSingleResult();

        assertThat(userDB.getId(), notNullValue());
        assertThat(userDB.getName(), equalTo(userUpdate.getName()));
        assertThat(userDB.getEmail(), equalTo(userUpdate.getEmail()));
    }

    @Test
    void delete() {
        User user = makeUser("Петр Иванович", "some@email.com");

        long id = service.create(user).getId();
        service.delete(id);
        List<User> users = service.findAll(0, 10);
        assertThat(users, equalTo(List.of()));
    }

    private User makeUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return user;
    }
}
