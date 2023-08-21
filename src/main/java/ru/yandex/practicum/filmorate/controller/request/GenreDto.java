package ru.yandex.practicum.filmorate.controller.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenreDto {
    private Integer id;
    private String name;
}
