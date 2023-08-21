package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

public interface MpaService {
    MpaRating getMpaRatingById(Integer id);

    List<MpaRating> getAllMpaRatings();
}
