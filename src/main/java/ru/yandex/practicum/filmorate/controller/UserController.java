package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.user.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    @Getter
    private final Map<Integer, User> users = new HashMap<>();
    private static int increment;
    private int id;

    @PostMapping
    public User add(@RequestBody User user) {
        UserValidator.startValidate(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        checkUserExists(user);
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<User>(users.values());
    }

    private Integer generateId() {
        id = ++increment;
        return id;
    }

    private void checkUserExists(User user) {
        int id = user.getId();
        if (!users.containsKey(id)) {
            throw new NoSuchUserException("Пользователя с id " + id + " не существует");
        }
    }
}
