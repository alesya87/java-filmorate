package ru.yandex.practicum.filmorate.exception.film;

public class NoSuchFilmException extends RuntimeException {
    public NoSuchFilmException(final String message) {
        super(message);
    }
}
