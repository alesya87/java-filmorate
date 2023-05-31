package ru.yandex.practicum.filmorate.exception;

public class NoSuchFilmException extends RuntimeException {
    public NoSuchFilmException(final String message) {
        super(message);
    }
}
