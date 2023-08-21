package ru.yandex.practicum.filmorate.storage.impl;


import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {
    public Genre getGenreById(Integer id);

    public List<Genre> getAllGenres();

    public Set<Genre> getGenresByFilmId(Integer id);
}
