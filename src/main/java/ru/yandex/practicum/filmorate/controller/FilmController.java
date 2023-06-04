package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int id;

    @PostMapping
    public Film add(@RequestBody Film film) {
        Validator.startValidate(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        checkFilmExists(film);
        Validator.startValidate(film);
        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<Film>(films.values());
    }

    private Integer generateId() {
        return ++id;
    }

    private void checkFilmExists(Film film) {
        int id = film.getId();
        if (films.get(id) == null) {
            throw new EntityNotFoundException("Фильма с id " + id + " не существует");
        }
    }
}
