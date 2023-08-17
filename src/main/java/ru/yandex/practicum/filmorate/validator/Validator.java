package ru.yandex.practicum.filmorate.validator;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.controller.request.FilmRequest;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;

import java.time.LocalDate;

@UtilityClass
public class Validator {
    private static final LocalDate MIN_REALISE_DATE = LocalDate.of(1895, 12, 18);

    public static void checkIdIsPositive(int id) {
        if (id <= 0) {
            throw new EntityNotFoundException("Ресурс с id " + id + " не найден");
        }
    }

    public static void checkReleaseDate(FilmRequest filmRequest) {
        LocalDate realiseDate = filmRequest.getReleaseDate();
        if (realiseDate == null || realiseDate.isBefore(MIN_REALISE_DATE)) {
            throw new ValidateException("Дата релиза фильма должна быть не ранее 18 декабря 1985 года");
        }
    }
}
