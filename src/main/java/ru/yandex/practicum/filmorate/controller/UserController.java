package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.request.CreateUserRequest;
import ru.yandex.practicum.filmorate.controller.request.UpdateUserRequest;
import ru.yandex.practicum.filmorate.controller.request.converter.UserConverter;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
@Validated
public class UserController {
    @Autowired
    private final UserService userService;

    @PostMapping
    public User add(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return userService.add(UserConverter.convert(createUserRequest));
    }

    @PutMapping
    public User update(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        return userService.update(UserConverter.convert(updateUserRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable("id") Integer id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId) {
        return userService.getMutualFriends(id, otherId);
    }
}
