package ru.yandex.practicum.filmorate.validator;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.controller.request.FilmFullDto;
import ru.yandex.practicum.filmorate.controller.request.FilmWithoutIdDto;
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

    public static void checkReleaseDate(FilmFullDto filmFullDto) {
        LocalDate realiseDate = filmFullDto.getReleaseDate();
        if (realiseDate == null || realiseDate.isBefore(MIN_REALISE_DATE)) {
            throw new ValidateException("Дата релиза фильма должна быть не ранее 18 декабря 1985 года");
        }
    }

    public static void checkReleaseDate(FilmWithoutIdDto filmWithoutIdDto) {
        LocalDate realiseDate = filmWithoutIdDto.getReleaseDate();
        if (realiseDate == null || realiseDate.isBefore(MIN_REALISE_DATE)) {
            throw new ValidateException("Дата релиза фильма должна быть не ранее 18 декабря 1985 года");
        }
    }
}
