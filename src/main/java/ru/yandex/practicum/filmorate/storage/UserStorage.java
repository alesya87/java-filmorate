package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public User add(User user);

    public User update(User user);

    public User getUserById(Integer id);

    public List<User> getAllUsers();

    public void checkUserExistsById(Integer id);

    public void checkUserExists(User user);
}
