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
    private final String sqlUpdateUserById = "update users set name = ?, login = ?, email = ?, birthday = ? where id = ?";
    private final String sqlGetUserById = "select id, name, login, email, birthday from users where id = ?";
    private final String sqlGetFriendsIdByUserId = "select friend_id from friends where user_id = ?";
    private final String sqlGetAllUsers = "select id, name, login, email, birthday from users";
    private final String sqlAddFriend = "insert into friends(user_id, friend_id, status) values (?, ?, ?)";
    private final String sqlDeleteFriend = "delete from friends where user_id = ? and friend_id = ?";
    private final String sqlGetFriendsByUserId = "select id, name, login, email, birthday " +
            "from users u " +
            "inner join friends f on u.id = f.friend_id " +
            "where f.user_id = ?";
    private final String sqlGetMutualFriends = "select * from users where id in (select f.friend_id " +
            "from friends as f " +
            "inner join friends as f2 on f.friend_id = f2.friend_id " +
            "where f.user_id = ? and f2.user_id = ?)"; // TODO: можно ли переписать без вложенного селекта?
    private final String sqlSelectForFriendStatus = "select status from friends where user_id = ? and friend_id = ?";

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    } // TODO: где он создается? засчет аннотации репозиторий?

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
        jdbcTemplate.update(sqlUpdateUserById, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public User getUserById(Integer id) {
        try {
            User user = jdbcTemplate.queryForObject(sqlGetUserById, new UserMapper(), id);
            user.setFriends(getFriendsIdByUserId(user.getId())); // TODO: как лучше заполнить массив friends?
            return user;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        for (User user : jdbcTemplate.query(sqlGetAllUsers, new UserMapper())) {
            user.setFriends(getFriendsIdByUserId(user.getId()));
            users.add(user);
        }
        return users;
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        try {
            jdbcTemplate.queryForObject(sqlSelectForFriendStatus, Boolean.class, friendId, id);
            // TODO: как лучше присвоить статус, если в базе нет обратной записи?
            // TODO: кка лучше сделать апдейт статуса обеих записей
            jdbcTemplate.update(sqlAddFriend, id, friendId, true);
            jdbcTemplate.update(sqlAddFriend, friendId, id, true);
        } catch (EmptyResultDataAccessException e) {
            jdbcTemplate.update(sqlAddFriend, id, friendId, false);
        }
    }

    @Override
    public void deleteFriend(Integer id, Integer friendId) {
        jdbcTemplate.update(sqlDeleteFriend, id, friendId);
    }

    @Override
    public List<User> getAllFriends(Integer id) {
        List<User> users = new ArrayList<>();
        for (User user : jdbcTemplate.query(sqlGetFriendsByUserId, new UserMapper(), id)) {
            user.setFriends(getFriendsIdByUserId(user.getId()));
            users.add(user);
        }
        return users;
    }

    @Override
    public List<User> getMutualFriends(Integer id, Integer otherId) {
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
        Set<Integer> friends = new HashSet<>();
        SqlRowSet rsFriends = jdbcTemplate.queryForRowSet(sqlGetFriendsIdByUserId, userId);
        while (rsFriends.next()) {
            friends.add(rsFriends.getInt("friend_id"));
        }
        return friends;
    }
}