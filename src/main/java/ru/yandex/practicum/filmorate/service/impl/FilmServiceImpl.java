package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
public class FilmService implements ru.yandex.practicum.filmorate.service.FilmService {
    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final LikeStorage likeStorage;

    public FilmService(FilmStorage filmStorage, MpaStorage mpaStorage, GenreStorage genreStorage, LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
        this.likeStorage = likeStorage;
    }

    @Override
    public Film add(Film film) {
        filmStorage.add(film);
        MpaRating mpaRating = mpaStorage.getMpaRatingByFilmId(film.getId());
        if (mpaRating == null) {
            throw new EntityNotFoundException("MPA не найден");
        }
        film.setMpa(mpaRating);
        film.setGenres(genreStorage.getGenresByFilmId(film.getId()));
        return film;
    }

    @Override
    public Film update(Film film) {
        int id = film.getId();
        if (filmStorage.getFilmById(id) == null) {
            throw new EntityNotFoundException("Фильма с id " + id + " не существует");
        }
        filmStorage.update(film);

        MpaRating mpaRating = mpaStorage.getMpaRatingByFilmId(film.getId());
        if (mpaRating == null) {
            throw new EntityNotFoundException("MPA не найден");
        }
        film.setMpa(mpaRating);
        film.setGenres(genreStorage.getGenresByFilmId(film.getId()));
        film.setLikes(likeStorage.getLikesByFilmId(film.getId()));
        film.setRate(likeStorage.getLikesCountByFilmId(film.getId()));
        return film;
    }

    @Override
    public Film getFilmById(Integer id) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new EntityNotFoundException("Фильм с id " + id + " не найден");
        }
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public void addLike(Integer id, Integer userId) {
        if (!likeStorage.addLike(id, userId)) {
            throw new EntityNotFoundException("Фильм с id " + id + " не найден");
        }
    }

    @Override
    public void deleteLike(Integer id, Integer userId) {
        if (!likeStorage.deleteLike(id, userId)) {
            throw new EntityNotFoundException("Фильма с id " + id + " не найден");
        }
    }

    @Override
    public List<Film> getFilmsByLikesCount(Integer count) {
        return filmStorage.getFilmsByLikesCount(count);
    }
}
