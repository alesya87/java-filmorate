package ru.yandex.practicum.filmorate.validator;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@UtilityClass
public class Validator {
    private static final int MAX_FILM_NAME_LENGTH = 200;
    private static final LocalDate MIN_REALISE_DATE = LocalDate.of(1895, 12, 18);

    public static void startValidate(Film film) {
        checkIfNameIsEmpty(film);
        checkDescriptionLength(film);
        checkReleaseDate(film);
        checkDuration(film);
    }

    public static void startValidate(User user) {
        checkEmail(user);
        checkLogin(user);
        checkName(user);
        checkBirthday(user);
    }

    public static void checkIdIsPositive(int id) {
        if (id <= 0) {
            throw new EntityNotFoundException("Ресурс с id " + id + " не найден");
        }
    }

    private static void checkIfNameIsEmpty(Film film) {
        String name = film.getName();
        if (name == null || name.isBlank()) {
            throw new ValidateException("Название фильма не может быть пустым");
        }
    }

    private static void checkDescriptionLength(Film film) {
        if (film.getDescription().length() > MAX_FILM_NAME_LENGTH) {
            throw new ValidateException("Длина названия фильма не может быть больше 200 символов");
        }
    }

    private static void checkReleaseDate(Film film) {
        LocalDate realiseDate = film.getReleaseDate();
        if (realiseDate == null || realiseDate.isBefore(MIN_REALISE_DATE)) {
            throw new ValidateException("Дата релиза фильма должна быть не ранее 18 декабря 1985 года");
        }
    }

    private static void checkDuration(Film film) {
        int duration = film.getDuration();
        if (duration < 0) {
            throw new ValidateException("Длительность фильма должна быть положительной");
        }
    }

    private static void checkEmail(User user) {
        String email = user.getEmail();
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new ValidateException("Email не может быть пустым и должен сожержать символ @");
        }
    }

    private static void checkLogin(User user) {
        String login = user.getLogin();
        if (login == null || login.isBlank() || login.contains(" ")) {
            throw new ValidateException("Логин не может быть пустым и не должен сожержать пробелы");
        }
    }

    private static void checkName(User user) {
        String name = user.getName();
        if (name == null || name.isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private static void checkBirthday(User user) {
        LocalDate birthday = user.getBirthday();
        if (birthday == null || birthday.isAfter(LocalDate.now())) {
            throw new ValidateException("Дата рождения должен быть не позднее текущей даты");
        }
    }
}
