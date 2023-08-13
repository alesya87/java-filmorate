package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(@Qualifier(value = "filmDBStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }
    // TODO: почему-то не работает через film.storage,type? Qualifier

    public Film add(Film film) {
        Integer mpaId = film.getMpa().getId();
        MpaRating mpaRating = filmStorage.getMpaRatingById(mpaId);
        if (mpaRating == null) {
            throw new EntityNotFoundException("MPA с id " + mpaId + "не существует");
        }
        film.setMpa(mpaRating); // TODO: как правильно добавить объект MPA?

        filmStorage.add(film); // TODO: жанры и мпа добавить в таблицу заранее?

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                if (!filmStorage.linkFilmWithGenre(film.getId(), genre.getId())) {
                    throw new EntityNotFoundException("Фильм с id " + film.getId() + "или жанр м id " + genre.getId() + "не существует");
                }
            }
            film.setGenres(filmStorage.getGenresByFilmId(film.getId())); // TODO: как правильно добавить объект Genre?
        }

        return film;
    }

    public Film update(Film film) {
        filmStorage.checkFilmExists(film);
        Validator.startValidate(film);
        filmStorage.update(film);
        return film;
    }

    public Film getFilmById(Integer id) {
        filmStorage.checkFilmExistsById(id);
        return filmStorage.getFilmById(id);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void addLike(Integer id, Integer userId) {
        Validator.checkIdIsPositive(userId);
        Film film = getFilmById(id);
        film.getLikes().add(userId);
    }

    public void deleteLike(Integer id, Integer userId) {
        Validator.checkIdIsPositive(userId);
        Film film = getFilmById(id);
        film.getLikes().remove(userId);
    }

    public List<Film> getFilmsByLikesCount(Integer count) {
        return filmStorage.getFilmsByLikesCount(count);
    }
}
