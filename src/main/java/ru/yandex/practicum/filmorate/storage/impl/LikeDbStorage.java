package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.HashSet;
import java.util.Set;

@Repository
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int getLikesCountByFilmId(Integer id) {
        String sqlGetLikesCountByFilmId = "select count(user_id) from likes where film_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlGetLikesCountByFilmId, Integer.class, id);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    @Override
    public Set<Integer> getLikesByFilmId(Integer id) {
        String sqlGetLikesByFilmId = "select user_id from likes where film_id = ?";
        try {
            return new HashSet<>(jdbcTemplate.queryForList(sqlGetLikesByFilmId, Integer.class, id));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public boolean addLike(Integer id, Integer userId) {
        String sqlAddLike = "insert into likes (film_id, user_id) values (?, ?)";
        String sqlIncreaseFilmRate = "update films set rate = (select COUNT(user_id) as rate " +
                "from likes where film_id = ?) where id = ?";
        try {
            jdbcTemplate.update(sqlAddLike, id, userId);
            jdbcTemplate.update(sqlIncreaseFilmRate, id, id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean deleteLike(Integer id, Integer userId) {
        String sqlDeleteLike = "delete from likes where film_id =? and user_id = ?";
        String sqlDecreaseFilmRate = "update films set rate = (select COUNT(user_id) as rate " +
                "from likes where film_id = ?) where id = ?";
        try {
            jdbcTemplate.update(sqlDeleteLike, id, userId);
            jdbcTemplate.update(sqlDecreaseFilmRate, id, id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
