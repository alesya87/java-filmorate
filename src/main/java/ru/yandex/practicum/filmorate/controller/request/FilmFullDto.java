package ru.yandex.practicum.filmorate.controller.request;

import lombok.Data;

@Data
public class UpdateFilmRequest extends FilmRequest {
    private Integer id;
}
