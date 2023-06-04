package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private User userToCreateOk = new User(null, "a@ru151.ru", "alesya2", "Alesya",
            LocalDate.parse("1967-03-25", dateTimeFormatter));

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
    }

    @Test
    public void shouldTrowValidateExceptionIfLoginIsEmptyOrContainsSpaceWhenCreate() {
        User user = new User(null, "a@ru151.ru", "alesya 2", "Alesya",
                LocalDate.parse("1967-03-25", dateTimeFormatter));
        Exception exception = assertThrows(ValidateException.class,
                () -> userController.add(user));
        assertEquals("Логин не может быть пустым и не должен сожержать пробелы", exception.getMessage());
    }

    @Test
    public void shouldTrowNotValidateExceptionIfEmailIsNotValidWhenCreate() {
        User user = new User(null, "aru151.ru", "alesya", "Alesya",
                LocalDate.parse("1967-03-25", dateTimeFormatter));
        Exception exception = assertThrows(ValidateException.class,
                () -> userController.add(user));
        assertEquals("Email не может быть пустым и должен сожержать символ @", exception.getMessage());
    }

    @Test
    public void shouldTrowValidateExceptionIfBirthdayIsAfterCurrentDateWhenCreate() {
        User user = new User(null, "a@ru151.ru", "alesya", "Alesya",
                LocalDate.now().plusDays(1));
        Exception exception = assertThrows(ValidateException.class,
                () -> userController.add(user));
        assertEquals("Дата рождения должен быть не позднее текущей даты", exception.getMessage());
    }

    @Test
    public void shouldSetNameLoginIfNameIsEmptyWhenCreate() {
        User user = new User(null, "a@ru151.ru", "alesya", "",
                LocalDate.now());
        userController.add(user);
        assertEquals("alesya", userController.getAllUsers().get(0).getName());
    }

    @Test
    public void shouldTrowValidateExceptionIfLoginIsEmptyOrContainsSpaceWhenUpdate() {
        userController.add(userToCreateOk);
        User userToUpdateFail = new User(1, "a@ru151.ru", "alesya 2", "Alesya",
                LocalDate.parse("1967-03-25", dateTimeFormatter));
        Exception exception = assertThrows(ValidateException.class,
                () -> userController.update(userToUpdateFail));
        assertEquals("Логин не может быть пустым и не должен сожержать пробелы", exception.getMessage());
    }

    @Test
    public void shouldTrowNotValidateExceptionIfEmailIsNotValidWhenUpdate() {
        userController.add(userToCreateOk);
        User userToUpdateFail = new User(1, "aru151.ru", "alesya", "Alesya",
                LocalDate.parse("1967-03-25", dateTimeFormatter));
        Exception exception = assertThrows(ValidateException.class,
                () -> userController.update(userToUpdateFail));
        assertEquals("Email не может быть пустым и должен сожержать символ @", exception.getMessage());
    }

    @Test
    public void shouldTrowValidateExceptionIfBirthdayIsAfterCurrentDateWhenUpdate() {
        userController.add(userToCreateOk);
        User userToUpdateFail = new User(1, "a@ru151.ru", "alesya", "Alesya",
                LocalDate.now().plusDays(1));
        Exception exception = assertThrows(ValidateException.class,
                () -> userController.update(userToUpdateFail));
        assertEquals("Дата рождения должен быть не позднее текущей даты", exception.getMessage());
    }

    @Test
    public void shouldThrowNEntityNotFoundExceptionItIsEmptyWhenUpdate() {
        User user = new User(1, "a@ru151.ru", "alesya", "Alesya",
                LocalDate.now());
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> userController.update(user));
        System.out.println(exception.getMessage());
        assertEquals("Пользователя с id 1 не существует", exception.getMessage());

    }
}