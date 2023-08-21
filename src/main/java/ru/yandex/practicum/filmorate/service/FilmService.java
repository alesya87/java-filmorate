package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film add(Film film);

    Film update(Film film);

    Film getFilmById(Integer id);

    List<Film> getAllFilms();

    void addLike(Integer id, Integer userId);

    void deleteLike(Integer id, Integer userId);

    List<Film> getFilmsByLikesCount(Integer count);

}
