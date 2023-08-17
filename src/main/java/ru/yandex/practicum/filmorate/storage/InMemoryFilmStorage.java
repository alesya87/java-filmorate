package ru.yandex.practicum.filmorate.storage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "film.storage.type", havingValue = "inMemory")
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
    public List<Film> getFilmsByLikesCount(Integer count) {
        List<Film> films = getAllFilms().stream()
                .sorted(Comparator.comparingInt(f -> f.getLikes().size()))
                .limit(count).collect(Collectors.toList());
        Collections.reverse(films);
        return films;
    }

    /*@Override
    public boolean addLike(Integer id, Integer userId) {
        Film film = getFilmById(id);
        film.getLikes().add(userId);
        return true;
    }

    @Override
    public boolean deleteLike(Integer id, Integer userId) {
        Film film = getFilmById(id);
        film.getLikes().remove(userId);
        return true;
    }*/
}
