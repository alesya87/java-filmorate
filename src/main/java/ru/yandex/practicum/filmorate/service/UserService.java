package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {
        setUserNameIfEmpty(user);
        userStorage.add(user);
        return user;
    }

    public User update(User user) {
        int id = user.getId();
        if (userStorage.getUserById(id) == null) {
            throw new EntityNotFoundException("Пользователя с id " + id + " не существует");
        }
        setUserNameIfEmpty(user);
        userStorage.update(user);
        return user;
    }

    public User getUserById(Integer id) {
        if (userStorage.getUserById(id) == null) {
            throw new EntityNotFoundException("Пользователя с id " + id + " не существует");
        }
        return userStorage.getUserById(id);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(Integer id, Integer friendId) {
        if (getUserById(id) == null) {
            throw new EntityNotFoundException("Пользователя с id " + id + " не существует");
        }
        if (getUserById(friendId) == null) {
            throw new EntityNotFoundException("Пользователя с id " + friendId + " не существует");
        }
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(Integer id, Integer friendId) {
        userStorage.deleteFriend(id, friendId);
    }

    public List<User> getAllFriends(Integer id) {
        if (getUserById(id) == null) {
            throw new EntityNotFoundException("Пользователя с id " + id + " не существует");
        }
        return userStorage.getAllFriends(id);
    }

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
