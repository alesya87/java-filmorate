package ru.yandex.practicum.filmorate.controller.request.converter;

import ru.yandex.practicum.filmorate.controller.request.CreateUserRequest;
import ru.yandex.practicum.filmorate.controller.request.UpdateUserRequest;
import ru.yandex.practicum.filmorate.controller.request.UserRequest;
import ru.yandex.practicum.filmorate.model.User;

public class UserConverter {
    private static User convertInternal(UserRequest userRequest) {
        return User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .login(userRequest.getLogin())
                .birthday(userRequest.getBirthday())
                .build();
    }

    public static User convert(CreateUserRequest createUserRequest) {
        return convertInternal(createUserRequest);
    }

    public static User convert(UpdateUserRequest updateUserRequest) {
        User user = convertInternal(updateUserRequest);
        user.setId(updateUserRequest.getId());
        return user;
    }
}
