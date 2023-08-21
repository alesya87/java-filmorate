package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User add(User user);

    User update(User user);

    User getUserById(Integer id);

    List<User> getAllUsers();

    void addFriend(Integer id, Integer friendId);

    void deleteFriend(Integer id, Integer friendId);

    List<User> getAllFriends(Integer id);

    List<User> getMutualFriends(Integer id, Integer otherId);
}
