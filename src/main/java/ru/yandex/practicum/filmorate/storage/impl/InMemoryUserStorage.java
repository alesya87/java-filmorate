package ru.yandex.practicum.filmorate.storage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public void addFriend(Integer id, Integer friendId) {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
    }

    @Override
    public void deleteFriend(Integer id, Integer friendId) {
        User user = getUserById(id);
        User friend = getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
    }

    @Override
    public List<User> getAllFriends(Integer id) {
        User user = users.get(id);
        return user.getFriends().stream().map(friendId -> getUserById(friendId))
        .collect(Collectors.toList());
    }

    @Override
    public List<User> getMutualFriends(Integer id, Integer otherId) {
        User user = getUserById(id);
        User otherUser = getUserById(otherId);
        return user.getFriends().stream()
                .filter(friendId -> otherUser.getFriends().contains(friendId))
                .map(friendId -> getUserById(friendId))
                .collect(Collectors.toList());
    }
}
