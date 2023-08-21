package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.rowMapper.MpaRatingRowMapper;

import java.util.List;

@Repository
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MpaRating getMpaRatingById(Integer id) {
        try {
            String sqlGetMpaRatingById = "select id, name from mpa_ratings where id = ?";
            MpaRating mpaRating = jdbcTemplate.queryForObject(sqlGetMpaRatingById, new MpaRatingRowMapper(), id);
            return mpaRating;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<MpaRating> getAllMpaRatings() {
        String sqlGetAllMpaRatings = "select id, name from mpa_ratings";
        return jdbcTemplate.query(sqlGetAllMpaRatings, new MpaRatingRowMapper());
    }

    @Override
    public MpaRating getMpaRatingByFilmId(Integer id) {
        String sqlGetMpaRatingsByFilmId = "select mpa.id, mpa.name from mpa_ratings mpa " +
                "inner join films f on mpa.id = f.mpa_id " +
                "where f.id = ?";
        return jdbcTemplate.queryForObject(sqlGetMpaRatingsByFilmId, new MpaRatingRowMapper(), id);
    }
}
