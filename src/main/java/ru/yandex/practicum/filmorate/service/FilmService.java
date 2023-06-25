package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.List;

@Service
@AllArgsConstructor
public class FilmService {
    @Autowired
    private final FilmStorage filmStorage;

    public Film add(Film film) {
        Validator.startValidate(film);
        filmStorage.add(film);
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
