package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

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
        List<Film> films = getAllFilms().stream()
                .sorted(Comparator.comparingInt(f -> f.getLikes().size()))
                .limit(count).collect(Collectors.toList());
        Collections.reverse(films);
        return films;
    }

    @Override
    public MpaRating getMpaRatingById(Integer id) {
        return null;
    }

    @Override
    public Set<Genre> getGenresByFilmId(Integer id) {
        return null;
    }

    @Override
    public boolean linkFilmWithGenre(Integer filmId, Integer genreId) {
        return false;
    }
}
