package ru.yandex.practicum.filmorate.storage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.request.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.impl.FilmStorage;
import ru.yandex.practicum.filmorate.storage.rowMapper.FilmRowMapper;

import java.util.*;

@Repository
@ConditionalOnProperty(value = "film.storage.type", havingValue = "DB")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film add(Film film) {
        Map<String, Object> keys = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingColumns("name", "description", "duration", "release_date", "rate", "mpa_id")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKeyHolder(FilmMapper.filmToMap(film))
                .getKeys();
        film.setId((Integer) keys.get("id"));

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                if (!linkFilmWithGenre(film.getId(), genre.getId())) {
                    throw new EntityNotFoundException("Фильм с id " + film.getId() + "или жанр м id " + genre.getId() + "не существует");
                }
            }
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlUpdateFilmById = "update films set name = ?, description = ?, duration = ?, " +
                "release_date = ?, mpa_id = ? where id = ?";
        jdbcTemplate.update(sqlUpdateFilmById, film.getName(), film.getDescription(), film.getDuration(),
                film.getReleaseDate(), film.getMpa().getId(), film.getId());

        unlinkFilmWithGenre(film.getId());
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                if (!linkFilmWithGenre(film.getId(), genre.getId())) {
                    throw new EntityNotFoundException("Фильм с id " + film.getId() + "или жанр м id " + genre.getId() + "не существует");
                }
            }
        }
        return film;
    }

    @Override
    public Film getFilmById(Integer id) {
        String sqlGetFilmById = "select id, name, description, duration, release_date, rate from films where id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlGetFilmById, new FilmRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlGetAllFilms = "select id, name, description, duration, release_date, rate from films order by id";
        return jdbcTemplate.query(sqlGetAllFilms, new FilmRowMapper());
    }

    @Override
    public List<Film> getFilmsByLikesCount(Integer count) {
        String sqlGetAllFilmsByLikesCount = "select id, name, description, duration, release_date, rate from films " +
                "order by rate DESC limit ?";
        return jdbcTemplate.query(sqlGetAllFilmsByLikesCount, new FilmRowMapper(), count);
    }

    private boolean linkFilmWithGenre(Integer filmId, Integer genreId) {
        String sqlLinkFilmWithGenre = "insert into film_genres (film_id, genre_id) values (?, ?)";
        try {
            jdbcTemplate.update(sqlLinkFilmWithGenre, filmId, genreId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private void unlinkFilmWithGenre(Integer filmId) {
        String sqlUnlinkFilmWithGenre = "delete from film_genres where film_id = ?";
        jdbcTemplate.update(sqlUnlinkFilmWithGenre, filmId);
    }
}
