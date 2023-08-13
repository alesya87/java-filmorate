package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    public Film add(Film film);

    public Film update(Film film);

    public Film getFilmById(Integer id);

    public List<Film> getAllFilms();

    public void checkFilmExists(Film film);

    public void checkFilmExistsById(Integer id);

    public List<Film> getFilmsByLikesCount(Integer count);

    public MpaRating getMpaRatingById(Integer id);

    public Set<Genre> getGenresByFilmId(Integer id);

    public boolean linkFilmWithGenre(Integer filmId, Integer genreId);
}
