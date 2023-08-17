package ru.yandex.practicum.filmorate.controller.request.converter;

import ru.yandex.practicum.filmorate.controller.request.CreateFilmRequest;
import ru.yandex.practicum.filmorate.controller.request.FilmRequest;
import ru.yandex.practicum.filmorate.controller.request.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmConverter {
    private static Film convertInternal(FilmRequest filmRequest) {
        return Film.builder()
                .name(filmRequest.getName())
                .description(filmRequest.getDescription())
                .releaseDate(filmRequest.getReleaseDate())
                .duration(filmRequest.getDuration())
                .rate(filmRequest.getRate())
                .mpa(filmRequest.getMpa())
                .genres(filmRequest.getGenres())
                .build();
    }

    public static Film convert(CreateFilmRequest createFilmRequest) {
        return convertInternal(createFilmRequest);
    }

    public static Film convert(UpdateFilmRequest updateFilmRequest) {
        Film film = convertInternal(updateFilmRequest);
        film.setId(updateFilmRequest.getId());
        return film;
    }
}
