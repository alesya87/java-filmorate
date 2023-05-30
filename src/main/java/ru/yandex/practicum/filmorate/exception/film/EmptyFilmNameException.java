package ru.yandex.practicum.filmorate.exception.film;

public class EmptyFilmNameException extends RuntimeException {
    public EmptyFilmNameException(final String message) {
        super(message);
    }
}
