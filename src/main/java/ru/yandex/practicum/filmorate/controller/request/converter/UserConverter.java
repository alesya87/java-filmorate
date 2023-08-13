package ru.yandex.practicum.filmorate.controller.request.converter;

import ru.yandex.practicum.filmorate.controller.request.CreateUserRequest;
import ru.yandex.practicum.filmorate.controller.request.UpdateUserRequest;
import ru.yandex.practicum.filmorate.model.User;

public class UserConverter {
    public static User convert(CreateUserRequest createUserRequest) {
        return User.builder()
                .name(createUserRequest.getName())
                .email(createUserRequest.getEmail())
                .login(createUserRequest.getLogin())
                .birthday(createUserRequest.getBirthday())
                .build(); // TODO: сделать общий клас
    }

    public static User convert(UpdateUserRequest updateUserRequest) {
        User user = convert((CreateUserRequest) updateUserRequest);
        user.setId(updateUserRequest.getId());
        return user;
    }
}
