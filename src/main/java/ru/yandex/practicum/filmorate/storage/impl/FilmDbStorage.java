package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.request.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.rowMapper.FilmRowMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
@ConditionalOnProperty(value = "film.storage.type", havingValue = "DB")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        genreStorage = new GenreDbStorage(jdbcTemplate);
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
        linkFilmWithGenre(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlUpdateFilmById = "update films set name = ?, description = ?, duration = ?, " +
                "release_date = ?, mpa_id = ? where id = ?";
        jdbcTemplate.update(sqlUpdateFilmById, film.getName(), film.getDescription(), film.getDuration(),
                film.getReleaseDate(), film.getMpa().getId(), film.getId());
        unlinkFilmWithGenre(film.getId());
        linkFilmWithGenre(film);
        return film;
    }

    @Override
    public Film getFilmById(Integer id) {
        String sqlGetFilmById = "select table1.id, table1.name, table1.description,\n" +
                "table1.duration, table1.release_date, table1.rate, table1.mpa_id, " +
                "table1.mpa_name, table1.likes,\n" +
                "string_agg(g.id::varchar, ',' order by g.id) as genre_ids\n" +
                "from\n" +
                "(\n" +
                "select f.id, f.name, f.description, f.duration, f.release_date, count(l.user_id) as rate,\n" +
                "mr.id as mpa_id, mr.name as mpa_name, string_agg(l.user_id::varchar, ',') as likes\n" +
                "from films f\n" +
                "left join mpa_ratings mr on f.mpa_id = mr.id\n" +
                "left join likes l on f.id = l.film_id\n" +
                "where f.id = ?\n" +
                "group by f.id, f.name, f.description, f.release_date, mr.id, mr.name\n" +
                ") table1\n" +
                "left join film_genres fg on table1.id = fg.film_id\n" +
                "left join genres g on g.id = fg.genre_id\n" +
                "group by table1.id, table1.name, table1.description,\n" +
                "table1.duration, table1.release_date, table1.rate, table1.mpa_id, " +
                "table1.mpa_name, table1.likes";
        try {
            return jdbcTemplate.queryForObject(sqlGetFilmById, new FilmRowMapper(getGenresMap()), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlGetAllFilms = "select table1.id, table1.name, table1.description,\n" +
                "table1.duration, table1.release_date, table1.rate, table1.mpa_id, " +
                "table1.mpa_name, table1.likes,\n" +
                "string_agg(g.id::varchar, ',' order by g.id) as genre_ids\n" +
                "from\n" +
                "(\n" +
                "select f.id, f.name, f.description, f.duration, f.release_date, count(l.user_id) as rate,\n" +
                "mr.id as mpa_id, mr.name as mpa_name, string_agg(l.user_id::varchar, ',') as likes\n" +
                "from films f\n" +
                "left join mpa_ratings mr on f.mpa_id = mr.id\n" +
                "left join likes l on f.id = l.film_id\n" +
                "group by f.id, f.name, f.description, f.release_date, mr.id, mr.name\n" +
                ") table1\n" +
                "left join film_genres fg on table1.id = fg.film_id\n" +
                "left join genres g on g.id = fg.genre_id\n" +
                "group by table1.id, table1.name, table1.description,\n" +
                "table1.duration, table1.release_date, table1.rate, table1.mpa_id, " +
                "table1.mpa_name, table1.likes\n" +
                "order by table1.id ";
        return jdbcTemplate.query(sqlGetAllFilms, new FilmRowMapper(getGenresMap()));
    }

    @Override
    public List<Film> getFilmsByLikesCount(Integer count) {
        String sqlGetAllFilmsByLikesCount = "select table1.id, table1.name, table1.description,\n" +
                "table1.duration, table1.release_date, table1.rate, table1.mpa_id, " +
                "table1.mpa_name, table1.likes,\n" +
                "string_agg(g.id::varchar, ',' order by g.id) as genre_ids\n" +
                "from\n" +
                "(\n" +
                "select f.id, f.name, f.description, f.duration, f.release_date, count(l.user_id) as rate,\n" +
                "mr.id as mpa_id, mr.name as mpa_name, string_agg(l.user_id::varchar, ',') as likes\n" +
                "from films f\n" +
                "left join mpa_ratings mr on f.mpa_id = mr.id\n" +
                "left join likes l on f.id = l.film_id\n" +
                "group by f.id, f.name, f.description, f.release_date, mr.id, mr.name\n" +
                ") table1\n" +
                "left join film_genres fg on table1.id = fg.film_id\n" +
                "left join genres g on g.id = fg.genre_id\n" +
                "group by table1.id, table1.name, table1.description,\n" +
                "table1.duration, table1.release_date, table1.rate, table1.mpa_id, " +
                "table1.mpa_name, table1.likes\n" +
                "order by table1.rate DESC\n" +
                "limit ?";
        return jdbcTemplate.query(sqlGetAllFilmsByLikesCount, new FilmRowMapper(getGenresMap()), count);
    }

    private void linkFilmWithGenre(Film film) {
        if (film.getGenres() != null) {
            List<Genre> genreList = new ArrayList<>(film.getGenres());
            jdbcTemplate.batchUpdate(
                    "insert into film_genres (film_id, genre_id) values (?, ?)",
                    new BatchPreparedStatementSetter() {
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            Genre genre = genreList.get(i);
                            ps.setInt(1, film.getId());
                            ps.setInt(2, genre.getId());
                        }

                        public int getBatchSize() {
                            return genreList.size();
                        }
                    });
        }
    }

    private void unlinkFilmWithGenre(Integer filmId) {
        String sqlUnlinkFilmWithGenre = "delete from film_genres where film_id = ?";
        jdbcTemplate.update(sqlUnlinkFilmWithGenre, filmId);
    }

    private HashMap<Integer, Genre> getGenresMap() {
        HashMap<Integer, Genre> genres = new HashMap<>();
        for (Genre genre : genreStorage.getAllGenres()) {
            genres.put(genre.getId(), genre);
        }
        return genres;
    }
}
