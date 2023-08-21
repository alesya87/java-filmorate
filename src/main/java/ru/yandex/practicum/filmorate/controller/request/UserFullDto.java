package ru.yandex.practicum.filmorate.controller.request;

import lombok.Data;

@Data
public class UpdateUserDto extends UserDto {
    private Integer id;
}
