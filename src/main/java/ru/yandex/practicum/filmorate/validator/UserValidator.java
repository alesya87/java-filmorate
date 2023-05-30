package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.user.NotValidBirthdayException;
import ru.yandex.practicum.filmorate.exception.user.NotValidEmailException;
import ru.yandex.practicum.filmorate.exception.user.NotValidLoginException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidator {

    public static void startValidate(User user) {
        checkEmail(user);
        checkLogin(user);
        checkName(user);
        checkBirthday(user);
    }
    private static void checkEmail(User user) {
        String email  = user.getEmail();
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new NotValidEmailException("Email не может быть пустым и должен сожержать символ @");
        }
    }

    private static void checkLogin(User user) {
        String login  = user.getLogin();
        if (login == null || login.isBlank() || login.contains(" ")) {
            throw new NotValidLoginException("Логин не может быть пустым и не должен сожержать пробелы");
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
            throw new NotValidBirthdayException("Дата рождения должен быть не позднее текущей даты");
        }
    }
}
