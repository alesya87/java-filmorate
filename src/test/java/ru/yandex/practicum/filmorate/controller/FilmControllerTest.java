package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    Film filmToCreateOk = new Film(null, "Alesya", "adipisicing",
            LocalDate.parse("1967-03-25", dateTimeFormatter), 100);

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    public void shouldTrowValidateExceptionIfFilmNameIsEmptyWhenCreateWhenCreate() {
        Film film = new Film(null, "", "adipisicing",
                LocalDate.parse("1967-03-25", dateTimeFormatter), 100);
        Exception exception = assertThrows(ValidateException.class,
                () -> filmController.add(film));
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    public void shouldTrowValidateExceptionIfDescriptionLengthMoreThan200WhenCreate() {
        Film film = new Film(null, "test", String.format("%-200s", ""),
                LocalDate.parse("1967-03-25", dateTimeFormatter), 100);
        filmController.add(film);
        assertEquals(1, filmController.getAllFilms().size());
        Film filmFail = new Film(null, "test", String.format("%-201s", ""),
                LocalDate.parse("1967-03-25", dateTimeFormatter), 100);
        Exception exception = assertThrows(ValidateException.class,
                () -> filmController.add(filmFail));
        assertEquals("Длина названия фильма не может быть больше 200 символов", exception.getMessage());
    }

    @Test
    public void shouldThrowValidateExceptionIfReleaseDateIsEarlierThan18951218WhenCreate() {
        Film film = new Film(null, "test", String.format("%-200s", ""),
                LocalDate.parse("1895-12-18", dateTimeFormatter), 100);
        filmController.add(film);
        assertEquals(1, filmController.getAllFilms().size());
        Film filmFail = new Film(null, "test", String.format("%-20s", ""),
                LocalDate.parse("1895-12-17", dateTimeFormatter), 100);
        Exception exception = assertThrows(ValidateException.class,
                () -> filmController.add(filmFail));
        assertEquals("Дата релиза фильма должна быть не ранее 18 декабря 1985 года", exception.getMessage());
    }

    @Test
    public void shouldThrowValidateExceptionIfDurationIsNotPositiveWhenCreate() {
        Film filmFail = new Film(null, "test", String.format("%-200s", ""),
                LocalDate.parse("1895-12-18", dateTimeFormatter), -100);
        Exception exception = assertThrows(ValidateException.class,
                () -> filmController.add(filmFail));
        assertEquals("Длительность фильма должна быть положительной", exception.getMessage());
    }

    @Test
    public void shouldTrowValidateExceptionIfFilmNameIsEmptyWhenUpdate() {
        filmController.add(filmToCreateOk);
        Film filmToUpdateFail = new Film(1, "", "adipisicing",
                LocalDate.parse("1967-03-25", dateTimeFormatter), 100);
        Exception exception = assertThrows(ValidateException.class,
                () -> filmController.update(filmToUpdateFail));
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    public void shouldTrowValidateExceptionIfDescriptionLengthMoreThan200WhenUpdate() {
        filmController.add(filmToCreateOk);
        assertEquals(1, filmController.getAllFilms().size());
        Film filmToUpdateFail = new Film(1, "test", String.format("%-202s", ""),
                LocalDate.parse("1967-03-25", dateTimeFormatter), 100);
        Exception exception = assertThrows(ValidateException.class,
                () -> filmController.update(filmToUpdateFail));
        assertEquals("Длина названия фильма не может быть больше 200 символов", exception.getMessage());
    }

    @Test
    public void shouldThrowValidateExceptionIfReleaseDateIsEarlierThan18951218WhenUpdate() {
        filmController.add(filmToCreateOk);
        Film filmToUpdateOk = new Film(1, "test", String.format("%-200s", ""),
                LocalDate.parse("1895-12-18", dateTimeFormatter), 100);
        filmController.update(filmToUpdateOk);
        assertEquals(1, filmController.getAllFilms().size());
        Film filmToUpdateFail = new Film(1, "test", String.format("%-20s", ""),
                LocalDate.parse("1895-12-17", dateTimeFormatter), 100);
        Exception exception = assertThrows(ValidateException.class,
                () -> filmController.update(filmToUpdateFail));
        assertEquals("Дата релиза фильма должна быть не ранее 18 декабря 1985 года", exception.getMessage());
    }

    @Test
    public void shouldThrowValidateExceptionIfDurationIsNotPositiveWhenUpdate() {
        filmController.add(filmToCreateOk);
        Film filmToUpdateFail = new Film(1, "test", String.format("%-200s", ""),
                LocalDate.parse("1895-12-18", dateTimeFormatter), -100);
        Exception exception = assertThrows(ValidateException.class,
                () -> filmController.update(filmToUpdateFail));
        assertEquals("Длительность фильма должна быть положительной", exception.getMessage());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionIfFilmEmptyWhenUpdate() {
        Film filmToUpdateFail = new Film(1, "test", String.format("%-2s", ""),
                LocalDate.parse("1895-12-18", dateTimeFormatter), 100);
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> filmController.update(filmToUpdateFail));
        assertEquals("Фильма с id 1 не существует", exception.getMessage());
    }
}