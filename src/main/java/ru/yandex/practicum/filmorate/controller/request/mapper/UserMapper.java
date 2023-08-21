package ru.yandex.practicum.filmorate.controller.request.mapper;

import ru.yandex.practicum.filmorate.controller.request.UserWithoutIdDto;
import ru.yandex.practicum.filmorate.controller.request.UserFullDto;
import ru.yandex.practicum.filmorate.model.User;

public class UserMapper {
    public static User convert(UserWithoutIdDto userWithoutIdDto) {
        return User.builder()
                .name(userWithoutIdDto.getName())
                .email(userWithoutIdDto.getEmail())
                .login(userWithoutIdDto.getLogin())
                .birthday(userWithoutIdDto.getBirthday())
                .build();
    }

    public static User convert(UserFullDto userFullDto) {
        return User.builder()
                .id(userFullDto.getId())
                .name(userFullDto.getName())
                .email(userFullDto.getEmail())
                .login(userFullDto.getLogin())
                .birthday(userFullDto.getBirthday())
                .build();
    }

    public static UserFullDto convert(User user) {
        return UserFullDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .login(user.getLogin())
                .birthday(user.getBirthday())
                .friends(user.getFriends())
                .build();
    }
}
