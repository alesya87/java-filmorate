package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.film.DurationIsNotPositiveException;
import ru.yandex.practicum.filmorate.exception.film.EmptyFilmNameException;
import ru.yandex.practicum.filmorate.exception.film.DescriptionLengthMoreThanLimitException;
import ru.yandex.practicum.filmorate.exception.film.NotValidRealiseDateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidator {
    private final static int MAX_FILM_NAME_LENGTH = 200;
    private final static LocalDate MIN_REALISE_DATE = LocalDate.of(1895, 12, 18);

    public static void startValidate(Film film) {
        checkIfNameIsEmpty(film);
        checkDescriptionLength(film);
        checkReleaseDate(film);
        checkDuration(film);
    }

    private static void checkIfNameIsEmpty(Film film) {
        String name = film.getName();
        if (name == null || name.isBlank()) {
            throw new EmptyFilmNameException("Название фильма не может быть пустым");
        }
    }

    private static void checkDescriptionLength(Film film) {
        if (film.getDescription().length() > MAX_FILM_NAME_LENGTH) {
            throw new DescriptionLengthMoreThanLimitException("Длина названия фильма не может быть больше 200 символов");
        }
    }

    private static void checkReleaseDate(Film film) {
        LocalDate realiseDate = film.getReleaseDate();
        if (realiseDate == null || realiseDate.isBefore(MIN_REALISE_DATE)) {
            throw new NotValidRealiseDateException("Дата релиза фильма должна быть не ранее 18 декабря 1985 года");
        }
    }

    private static void checkDuration(Film film) {
        int duration = film.getDuration();
        if (duration < 0) {
            throw new DurationIsNotPositiveException("Длительность фильма должна быть положительной");
        }
    }
}
