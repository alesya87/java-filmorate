package ru.yandex.practicum.filmorate.storage.impl;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    public Film add(Film film);

    public Film update(Film film);

    public Film getFilmById(Integer id);

    public List<Film> getAllFilms();

    public List<Film> getFilmsByLikesCount(Integer count);
}
