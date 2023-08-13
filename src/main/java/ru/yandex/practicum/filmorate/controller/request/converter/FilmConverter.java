package ru.yandex.practicum.filmorate.controller.request.converter;

import ru.yandex.practicum.filmorate.controller.request.CreateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmConverter {
    public static Film convert(CreateFilmRequest createFilmRequest) {
        return Film.builder()
                .name(createFilmRequest.getName())
                .description(createFilmRequest.getDescription())
                .releaseDate(createFilmRequest.getReleaseDate())
                .duration(createFilmRequest.getDuration())
                .rate(createFilmRequest.getRate())
                .mpa(createFilmRequest.getMpa())
                .genres(createFilmRequest.getGenres())
                .build();
    }
}
