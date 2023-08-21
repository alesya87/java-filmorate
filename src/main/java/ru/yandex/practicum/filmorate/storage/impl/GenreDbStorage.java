package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.impl.GenreStorage;
import ru.yandex.practicum.filmorate.storage.rowMapper.GenreRowMapper;

import java.util.*;

@Repository
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(Integer id) {
        String sqlGetGenreById = "select id, name from genres where id = ?";
        try {
            Genre genre = jdbcTemplate.queryForObject(sqlGetGenreById, new GenreRowMapper(), id);
            return genre;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlGetAllGenres = "select id, name from genres";
        return jdbcTemplate.query(sqlGetAllGenres, new GenreRowMapper());
    }

    public Set<Genre> getGenresByFilmId(Integer id) {
        String sqlGetGenresByFilmId = "select id, name from genres g " +
                "inner join film_genres fg on g.id = fg.genre_id " +
                "where fg.film_id = ?";
        try {
            return new HashSet<>(jdbcTemplate.query(sqlGetGenresByFilmId, new GenreRowMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
