package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

public interface MpaStorage {
    public MpaRating getMpaRatingById(Integer id);

    public List<MpaRating> getAllMpaRatings();

    public MpaRating getMpaRatingByFilmId(Integer id);
}
