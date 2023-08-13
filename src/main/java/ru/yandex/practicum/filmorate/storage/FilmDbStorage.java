package ru.yandex.practicum.filmorate.storage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.*;

@Repository("filmDBStorage") // TODO: почему не работает через film.storage,type?
@ConditionalOnProperty(value = "film.storage.type", havingValue = "DB")
public class FilmDbStorage implements FilmStorage {
    private JdbcTemplate jdbcTemplate;
    private String sqlCreateFilm = "insert into films (name, description, duration, release_date, mpa_id) " +
            "values (?, ?, ?, ?, ?)";
    private String sqlGetMpaRatingById = "select id, name from mpa_ratings where id = ?"; // TODO: нормально ли доставать id по id?
    private String sqlGetGenreById = "select id, name from genres where id = ?"; // TODO: нормально ли доставать id по id?
    private String sqlGetGenresByFilmId = "select id, name from genres g " +
            "inner join film_genres fg on g.id = fg.genre_id " +
            "where fg.film_id = ?";

    private String sqlLinkFilmWithGenre = "insert into film_genres (film_id, genre_id) values (?, ?)";

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film add(Film film) {
        Map<String, Object> keys = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingColumns("name", "description", "duration", "release_date", "mpa_id")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKeyHolder(filmToMap(film))
                .getKeys();
        film.setId((Integer) keys.get("id"));
        return film;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public Film getFilmById(Integer id) {
        return null;
    }

    @Override
    public List<Film> getAllFilms() {
        return null;
    }

    @Override
    public void checkFilmExists(Film film) {

    }

    @Override
    public void checkFilmExistsById(Integer id) {

    }

    @Override
    public List<Film> getFilmsByLikesCount(Integer count) {
        return null;
    }

    public Set<Genre> getGenresByFilmId(Integer id) {
        try {
            return new HashSet<>(jdbcTemplate.query(sqlGetGenresByFilmId, new GenreMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public MpaRating getMpaRatingById(Integer id) {
        try {
            return jdbcTemplate.queryForObject(sqlGetMpaRatingById, new MpaRatingMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Genre getGenreById(Integer id) {
        try {
            return jdbcTemplate.queryForObject(sqlGetGenreById, new GenreMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean linkFilmWithGenre(Integer filmId, Integer genreId) {
        try {
            jdbcTemplate.update(sqlLinkFilmWithGenre, filmId, genreId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private Map<String, Object> filmToMap(Film film) {
        Map<String, Object> filmMap = new HashMap<>();
        filmMap.put("name", film.getName());
        filmMap.put("description", film.getDescription());
        filmMap.put("duration", film.getDuration());
        filmMap.put("release_date", film.getReleaseDate());
        filmMap.put("rate", film.getRate());
        filmMap.put("mpa_id", film.getMpa().getId());
        return filmMap;
    }
}
