package ru.yandex.practicum.filmorate.storage.impl;

import java.util.Set;

public interface LikeStorage {
    public int getLikesCountByFilmId(Integer id);

    public Set<Integer> getLikesByFilmId(Integer id);

    public boolean addLike(Integer id, Integer userId);

    public boolean deleteLike(Integer id, Integer userId);
}
