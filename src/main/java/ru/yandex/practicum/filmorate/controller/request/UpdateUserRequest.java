package ru.yandex.practicum.filmorate.controller.request;

import lombok.Data;

@Data
public class UpdateUserRequest extends UserRequest {
    private Integer id;
}
