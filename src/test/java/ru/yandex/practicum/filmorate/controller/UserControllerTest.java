package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.film.*;
import ru.yandex.practicum.filmorate.exception.user.NoSuchUserException;
import ru.yandex.practicum.filmorate.exception.user.NotValidBirthdayException;
import ru.yandex.practicum.filmorate.exception.user.NotValidEmailException;
import ru.yandex.practicum.filmorate.exception.user.NotValidLoginException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class UsewrControllerTest {
    private UserController userController;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
    }

    @Test
    public void shouldTrowNotValidLoginExceptionIfItIs() {
        User user = new User(null, "a@ru151.ru", "alesya 2", "Alesya",
                LocalDate.parse("1967-03-25", dateTimeFormatter));
        Exception exception = assertThrows(NotValidLoginException.class,
                () -> userController.add(user));
        assertEquals("Логин не может быть пустым и не должен сожержать пробелы", exception.getMessage());
    }

    @Test
    public void shouldTrowNotValidEmailExceptionIfItIs() {
        User user = new User(null, "aru151.ru", "alesya", "Alesya",
                LocalDate.parse("1967-03-25", dateTimeFormatter));
        Exception exception = assertThrows(NotValidEmailException.class,
                () -> userController.add(user));
        assertEquals("Email не может быть пустым и должен сожержать символ @", exception.getMessage());
    }

    @Test
    public void shouldTrowNotValidBirthdayExceptionIfItIs() {
        User user = new User(null, "a@ru151.ru", "alesya", "Alesya",
                LocalDate.now().plusDays(1));
        Exception exception = assertThrows(NotValidBirthdayException.class,
                () -> userController.add(user));
        assertEquals("Дата рождения должен быть не позднее текущей даты", exception.getMessage());
    }

    @Test
    public void shouldThrowNoSuchUserExceptionItIsEmpty() {
        User user = new User(1, "a@ru151.ru", "alesya", "Alesya",
                LocalDate.now());
        Exception exception = assertThrows(NoSuchUserException.class,
                () -> userController.update(user));
        System.out.println(exception.getMessage());
        assertEquals("Пользователя с id 1 не существует", exception.getMessage());

    }

    @Test
    public void shouldSetNameLoginIfNameIsEmpty() {
        User user = new User(1, "a@ru151.ru", "alesya", "",
                LocalDate.now());
        userController.add(user);
        assertEquals("alesya", userController.getUsers().get(1).getName());
    }
}