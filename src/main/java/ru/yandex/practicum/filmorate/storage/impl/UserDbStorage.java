package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.rowMapper.UserRowMapper;

import java.util.*;

@Repository
@ConditionalOnProperty(value = "user.storage.type", havingValue = "DB")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User add(User user) {
        Map<String, Object> keys = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingColumns("name", "login", "email", "birthday")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKeyHolder(userToMap(user))
                .getKeys();
        user.setId((Integer) keys.get("id"));
        return user;
    }

    @Override
    public User update(User user) {
        String sqlUpdateUserById = "update users set name = ?, login = ?, email = ?, birthday = ? where id = ?";
        jdbcTemplate.update(sqlUpdateUserById, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public User getUserById(Integer id) {
        String sqlGetUserById = "select u.id, u.name, u.login, u.email, u.birthday, string_agg(f.friend_id::varchar, ',') as friends\n" +
                "from users u\n" +
                "left join friends f on u.id = f.user_id\n" +
                "where u.id = ?\n" +
                "group by u.id, u.name, u.login, u.email, u.birthday\n";
        try {
            return jdbcTemplate.queryForObject(sqlGetUserById, new UserRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        String sqlGetAllUsers = "select u.id, u.name, u.login, u.email, u.birthday, string_agg(f.friend_id::varchar, ',') as friends\n" +
                "from users u\n" +
                "left join friends f on u.id = f.user_id\n" +
                "group by u.id, u.name, u.login, u.email, u.birthday\n" +
                "order by u.id";
        List<User> users = new ArrayList<>();
        for (User user : jdbcTemplate.query(sqlGetAllUsers, new UserRowMapper())) {
            users.add(user);
        }
        return users;
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        String sqlSelectFriendStatus = "select COUNT(status) > 0 " +
                "from friends " +
                "where user_id = ? and friend_id = ?";
        String sqlAddFriend = "insert into friends(user_id, friend_id, status) values (?, ?, ?)";
        if (Boolean.TRUE.equals(jdbcTemplate.queryForObject(sqlSelectFriendStatus, Boolean.class, friendId, id))) {
            jdbcTemplate.update(sqlAddFriend, id, friendId, true);
            jdbcTemplate.update(sqlAddFriend, friendId, id, true);
        } else {
            jdbcTemplate.update(sqlAddFriend, id, friendId, false);
        }
    }

    @Override
    public void deleteFriend(Integer id, Integer friendId) {
        String sqlDeleteFriend = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlDeleteFriend, id, friendId);
    }

    @Override
    public List<User> getAllFriends(Integer id) {
        String sqlGetFriendsByUserId = "select u.id, u.name, u.login, u.email, u.birthday, string_agg(fr.friend_id::varchar, ',') as friends\n" +
                "from users u\n" +
                "inner join friends f on u.id = f.friend_id\n" +
                "left join friends fr on fr.user_id = f.friend_id\n" +
                "where f.user_id = ?\n" +
                "group by u.id, u.name, u.login, u.email, u.birthday\n" +
                "order by u.id";
        List<User> users = new ArrayList<>();
        for (User user : jdbcTemplate.query(sqlGetFriendsByUserId, new UserRowMapper(), id)) {
            users.add(user);
        }
        return users;
    }

    @Override
    public List<User> getMutualFriends(Integer id, Integer otherId) {
        String sqlGetMutualFriends = "select t1.id, t1.name, t1.login, t1.email, t1.birthday, string_agg(fr.friend_id::varchar, ',') as friends from\n" +
                "(\n" +
                "select id, name, login, email, birthday from users where id in (select f.friend_id\n" +
                "from friends as f\n" +
                "inner join friends as f2 on f.friend_id = f2.friend_id\n" +
                "where f.user_id = ? and f2.user_id = ?)\n" +
                ") t1\n" +
                "left join friends fr on fr.user_id = t1.id\n" +
                "group by t1.id, t1.name, t1.login, t1.email, t1.birthday\n";;
        List<User> mutualFriends = new ArrayList<>();
        for (User user : jdbcTemplate.query(sqlGetMutualFriends, new UserRowMapper(), id, otherId)) {
            mutualFriends.add(user);
        }
        return mutualFriends;
    }

    private Map<String, Object> userToMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", user.getName());
        values.put("login", user.getLogin());
        values.put("email", user.getEmail());
        values.put("birthday", user.getBirthday());
        return values;
    }
}