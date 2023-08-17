package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.List;

@Service
public class FilmService {
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

    public Film getFilmById(Integer id) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new EntityNotFoundException("Фильма с id " + id + " не существует");
        }

        MpaRating mpaRating = mpaStorage.getMpaRatingByFilmId(film.getId());
        if (mpaRating == null) {
            throw new EntityNotFoundException("MPA не найден");
        }
        film.setMpa(mpaRating);
        film.setGenres(genreStorage.getGenresByFilmId(film.getId()));
        film.setLikes(likeStorage.getLikesByFilmId(film.getId()));

        return film;
    }

    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.getAllFilms();
        for (Film film : films) {
            MpaRating mpaRating = mpaStorage.getMpaRatingByFilmId(film.getId());
            if (mpaRating == null) {
                throw new EntityNotFoundException("MPA не найден");
            }
            film.setMpa(mpaRating);
            film.setGenres(genreStorage.getGenresByFilmId(film.getId()));
            film.setLikes(likeStorage.getLikesByFilmId(film.getId()));
        }
        return films;
    }

    public void addLike(Integer id, Integer userId) {
        if (!likeStorage.addLike(id, userId)) {
            throw new EntityNotFoundException("Фильма с id " + id + " не найден");
        }
    }

    public void deleteLike(Integer id, Integer userId) {
        if (!likeStorage.deleteLike(id, userId)) {
            throw new EntityNotFoundException("Фильма с id " + id + " не найден");
        }
    }

    public List<Film> getFilmsByLikesCount(Integer count) {
        List<Film> films = filmStorage.getFilmsByLikesCount(count);
        for (Film film : films) {
            MpaRating mpaRating = mpaStorage.getMpaRatingByFilmId(film.getId());
            if (mpaRating == null) {
                throw new EntityNotFoundException("MPA не найден");
            }
            film.setMpa(mpaRating);
            film.setGenres(genreStorage.getGenresByFilmId(film.getId()));
            film.setLikes(likeStorage.getLikesByFilmId(film.getId()));
        }
        return films;
    }
}
