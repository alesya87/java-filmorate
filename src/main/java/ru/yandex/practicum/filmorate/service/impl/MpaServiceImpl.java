package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
public class MpaService implements ru.yandex.practicum.filmorate.service.MpaService {
    private final MpaStorage mpaStorage;

    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @Override
    public MpaRating getMpaRatingById(Integer id) {
        MpaRating mpaRating = mpaStorage.getMpaRatingById(id);
        if (mpaRating == null) {
            throw new EntityNotFoundException("MPA с id " + id + " не существует");
        }
        return mpaRating;
    }

    @Override
    public List<MpaRating> getAllMpaRatings() {
        return mpaStorage.getAllMpaRatings();
    }
}
