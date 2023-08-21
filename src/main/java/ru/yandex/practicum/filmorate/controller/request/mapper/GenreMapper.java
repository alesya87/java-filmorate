package ru.yandex.practicum.filmorate.controller.request.mapper;

import ru.yandex.practicum.filmorate.controller.request.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

public class GenreMapper {
    public static GenreDto convert(Genre genre) {
        return GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }
}
