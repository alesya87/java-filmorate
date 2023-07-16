package ru.yandex.practicum.filmorate.storage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

@Repository
@ConditionalOnProperty(value = "storage.type", havingValue = "DB")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User add(User user) {
        Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("users")
                .usingColumns("name", "login", "email", "birthday")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKeyHolder(Map.of(
                        "name", user.getName(),
                        "login", user.getLogin(),
                        "email", user.getEmail(),
                        "birthday", java.sql.Date.valueOf(user.getBirthday())))
                .getKeys();
        user.setId((Integer) keys.get("id"));
        return user;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public User getUserById(Integer id) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public void checkUserExistsById(Integer id) {

    }

    @Override
    public void checkUserExists(User user) {

    }
}