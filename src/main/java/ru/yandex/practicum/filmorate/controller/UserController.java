package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.request.UserWithoutIdDto;
import ru.yandex.practicum.filmorate.controller.request.UserFullDto;
import ru.yandex.practicum.filmorate.controller.request.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
@Validated
public class UserController {
    @Autowired
    private final UserService userService;

    @PostMapping
    public UserFullDto add(@Valid @RequestBody UserWithoutIdDto userWithoutIdDto) {
        User user = userService.add(UserMapper.convert(userWithoutIdDto));
        return UserMapper.convert(user);
    }

    @PutMapping
    public UserFullDto update(@Valid @RequestBody UserFullDto userFullDto) {
        User user = userService.update(UserMapper.convert(userFullDto));
        return UserMapper.convert(user);
    }

    @GetMapping("/{id}")
    public UserFullDto getUserById(@PathVariable("id") Integer id) {
        return UserMapper.convert(userService.getUserById(id));
    }

    @GetMapping
    public List<UserFullDto> getAllUsers() {
        List<UserFullDto> userFullDtos = new ArrayList<>();
        for (User user : userService.getAllUsers()) {
            userFullDtos.add(UserMapper.convert(user));
        }
        return userFullDtos;
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
    public List<UserFullDto> getAllFriends(@PathVariable("id") Integer id) {
        List<UserFullDto> userFullDtos = new ArrayList<>();
        for (User user : userService.getAllFriends(id)) {
            userFullDtos.add(UserMapper.convert(user));
        }
        return userFullDtos;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<UserFullDto> getMutualFriends(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId) {
        List<UserFullDto> userFullDtos = new ArrayList<>();
        for (User user : userService.getMutualFriends(id, otherId)) {
            userFullDtos.add(UserMapper.convert(user));
        }
        return userFullDtos;
    }
}
