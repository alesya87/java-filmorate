package ru.yandex.practicum.filmorate.storage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "storage.type", havingValue = "inMemory")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id;

    @Override
    public User add(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(Integer id) {
        return users.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<User>(users.values());
    }

    private Integer generateId() {
        return ++id;
    }

    @Override
    public void checkUserExists(User user) {
        int id = user.getId();
        if (users.get(id) == null) {
            throw new EntityNotFoundException("Пользователя с id " + id + " не существует");
        }
    }

    @Override
    public void checkUserExistsById(Integer id) {
        if (id == null || users.get(id) == null) {
            throw new EntityNotFoundException("Пользователя с id " + id + " не существует");
        }
    }
}
