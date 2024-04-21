package ru.yandex.practicum.filmorate.controller.request.mapper;

import ru.yandex.practicum.filmorate.controller.request.FilmWithoutIdDto;
import ru.yandex.practicum.filmorate.controller.request.FilmFullDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

public class FilmMapper {
    public static Film convert(FilmWithoutIdDto filmWithoutIdDto) {
        return Film.builder()
                .name(filmWithoutIdDto.getName())
                .description(filmWithoutIdDto.getDescription())
                .releaseDate(filmWithoutIdDto.getReleaseDate())
                .duration(filmWithoutIdDto.getDuration())
                .rate(filmWithoutIdDto.getRate())
                .mpa(filmWithoutIdDto.getMpa())
                .genres(filmWithoutIdDto.getGenres())
                .build();
    }

    public static Film convert(FilmFullDto filmFullDto) {
        return Film.builder()
                .id(filmFullDto.getId())
                .name(filmFullDto.getName())
                .description(filmFullDto.getDescription())
                .releaseDate(filmFullDto.getReleaseDate())
                .duration(filmFullDto.getDuration())
                .rate(filmFullDto.getRate())
                .mpa(filmFullDto.getMpa())
                .genres(filmFullDto.getGenres())
                .build();
    }

    public static FilmFullDto convert(Film film) {
        return FilmFullDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .rate(film.getRate())
                .mpa(film.getMpa())
                .genres(film.getGenres())
                .likes(film.getLikes())
                .build();
    }

    public static Map<String, Object> filmToMap(Film film) {
        Map<String, Object> filmMap = new HashMap<>();
        filmMap.put("name", film.getName());
        filmMap.put("description", film.getDescription());
        filmMap.put("duration", film.getDuration());
        filmMap.put("release_date", film.getReleaseDate());
        filmMap.put("rate", film.getRate());
        filmMap.put("mpa_id", film.getMpa().getId());
        return filmMap;
    }
}
