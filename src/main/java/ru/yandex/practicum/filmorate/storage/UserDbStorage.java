package ru.yandex.practicum.filmorate.storage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

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
        String sqlGetUserById = "select id, name, login, email, birthday from users where id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sqlGetUserById, new UserMapper(), id);
            user.setFriends(getFriendsIdByUserId(user.getId()));
            return user;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        String sqlGetAllUsers = "select id, name, login, email, birthday from users";
        List<User> users = new ArrayList<>();
        for (User user : jdbcTemplate.query(sqlGetAllUsers, new UserMapper())) {
            user.setFriends(getFriendsIdByUserId(user.getId()));
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
        String sqlGetFriendsByUserId = "select id, name, login, email, birthday " +
                "from users u " +
                "inner join friends f on u.id = f.friend_id " +
                "where f.user_id = ?";
        List<User> users = new ArrayList<>();
        for (User user : jdbcTemplate.query(sqlGetFriendsByUserId, new UserMapper(), id)) {
            user.setFriends(getFriendsIdByUserId(user.getId()));
            users.add(user);
        }
        return users;
    }

    @Override
    public List<User> getMutualFriends(Integer id, Integer otherId) {
        String sqlGetMutualFriends = "select id, name, login, email, birthday from users where id in (select f.friend_id " +
                "from friends as f " +
                "inner join friends as f2 on f.friend_id = f2.friend_id " +
                "where f.user_id = ? and f2.user_id = ?)";
        List<User> mutualFriends = new ArrayList<>();
        for (User user : jdbcTemplate.query(sqlGetMutualFriends, new UserMapper(), id, otherId)) {
            user.setFriends(getFriendsIdByUserId(user.getId()));
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

    private Set<Integer> getFriendsIdByUserId(Integer userId) {
        String sqlGetFriendsIdByUserId = "select friend_id from friends where user_id = ?";
        Set<Integer> friends = new HashSet<>();
        SqlRowSet rsFriends = jdbcTemplate.queryForRowSet(sqlGetFriendsIdByUserId, userId);
        while (rsFriends.next()) {
            friends.add(rsFriends.getInt("friend_id"));
        }
        return friends;
    }
}