package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserService implements ru.yandex.practicum.filmorate.service.UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User add(User user) {
        setUserNameIfEmpty(user);
        userStorage.add(user);
        return user;
    }

    @Override
    public User update(User user) {
        int id = user.getId();
        if (userStorage.getUserById(id) == null) {
            throw new EntityNotFoundException("Пользователя с id " + id + " не существует");
        }
        setUserNameIfEmpty(user);
        userStorage.update(user);
        return user;
    }

    @Override
    public User getUserById(Integer id) {
        if (userStorage.getUserById(id) == null) {
            throw new EntityNotFoundException("Пользователя с id " + id + " не существует");
        }
        return userStorage.getUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        if (getUserById(id) == null) {
            throw new EntityNotFoundException("Пользователя с id " + id + " не существует");
        }
        if (getUserById(friendId) == null) {
            throw new EntityNotFoundException("Пользователя с id " + friendId + " не существует");
        }
        userStorage.addFriend(id, friendId);
    }

    @Override
    public void deleteFriend(Integer id, Integer friendId) {
        userStorage.deleteFriend(id, friendId);
    }

    @Override
    public List<User> getAllFriends(Integer id) {
        if (getUserById(id) == null) {
            throw new EntityNotFoundException("Пользователя с id " + id + " не существует");
        }
        return userStorage.getAllFriends(id);
    }

    @Override
    public List<User> getMutualFriends(Integer id, Integer otherId) {
        if (getUserById(id) == null) {
            throw new EntityNotFoundException("Пользователя с id " + id + " не существует");
        }
        if (getUserById(otherId) == null) {
            throw new EntityNotFoundException("Пользователя с id " + otherId + " не существует");
        }
        return userStorage.getMutualFriends(id, otherId);
    }

    private void setUserNameIfEmpty(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
