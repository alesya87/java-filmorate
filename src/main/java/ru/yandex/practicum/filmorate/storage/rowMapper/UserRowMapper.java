package ru.yandex.practicum.filmorate.storage.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        Set<Integer> friends = new HashSet<>();
        if (rs.getString("friends") != null) {
            friends = Arrays.stream(rs.getString("friends").split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());
        }
        return User.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .birthday((rs.getTimestamp("birthday")).toLocalDateTime().toLocalDate())
                .friends(friends)
                .build();
    }
}
