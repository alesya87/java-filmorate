package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id;

    @Override
    public Film add(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilmById(Integer id) {
        return films.get(id);
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<Film>(films.values());
    }

    private Integer generateId() {
        return ++id;
    }

    @Override
    public void checkFilmExists(Film film) {
        int id = film.getId();
        if (films.get(id) == null) {
            throw new EntityNotFoundException("Фильма с id " + id + " не существует");
        }
    }

    @Override
    public void checkFilmExistsById(Integer id) {
        if (id == null || films.get(id) == null) {
            throw new EntityNotFoundException("Фильма с id " + id + " не существует");
        }
    }

    @Override
    public List<Film> getFilmsByLikesCount(Integer count) {
        return getAllFilms().stream()
                .sorted(Comparator.comparing(Film::getLikesSize).reversed())
                .limit(count).collect(Collectors.toList());
    }
}
