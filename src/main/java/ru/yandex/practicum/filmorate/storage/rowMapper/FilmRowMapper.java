package ru.yandex.practicum.filmorate.storage.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class FilmRowMapper implements RowMapper<Film> {
    private HashMap<Integer, Genre> genres;

    public FilmRowMapper(HashMap<Integer, Genre> genres) {
        this.genres = genres;
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {

        Set<Integer> likes = new HashSet<>();
        if (rs.getString("likes") != null) {
            likes = Arrays.stream(rs.getString("likes").split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());
        }

        Set<Genre> genreSet = new HashSet<>();
        if (rs.getString("genre_ids") != null) {
            String[] genreIds = rs.getString("genre_ids").split(",");
            for (String id : genreIds) {
                genreSet.add(genres.get(Integer.parseInt(id)));
            }
        }

        return Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .duration(rs.getInt("duration"))
                .releaseDate((rs.getTimestamp("release_date")).toLocalDateTime().toLocalDate())
                .rate(rs.getInt("rate"))
                .mpa(MpaRating.builder()
                        .id(rs.getInt("mpa_id"))
                        .name(rs.getString("mpa_name"))
                        .build())
                .likes(likes)
                .genres(genreSet)
                .build();
    }
}
