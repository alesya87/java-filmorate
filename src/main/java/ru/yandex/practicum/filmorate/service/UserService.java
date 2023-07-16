package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {
        Validator.startValidate(user);
        userStorage.add(user);
        return user;
    }

    public User update(User user) {
        userStorage.checkUserExists(user);
        Validator.startValidate(user);
        userStorage.update(user);
        return user;
    }

    public User getUserById(Integer id) {
        userStorage.checkUserExistsById(id);
        return userStorage.getUserById(id);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(Integer id, Integer friendId) {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
    }

    public void deleteFriend(Integer id, Integer friendId) {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
    }

    public List<User> getAllFriends(Integer id) {
        User user = getUserById(id);
        return user.getFriends().stream().map(friendId -> getUserById(friendId))
                .collect(Collectors.toList());
    }

    public List<User> getMutualFriends(Integer id, Integer otherId) {
        User user = getUserById(id);
        User otherUser = getUserById(otherId);
        return user.getFriends().stream()
                .filter(friendId -> otherUser.getFriends().contains(friendId))
                .map(friendId -> getUserById(friendId))
                .collect(Collectors.toList());
    }
}
