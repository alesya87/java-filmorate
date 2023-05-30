package ru.yandex.practicum.filmorate.exception.film;

public class DurationIsNotPositiveException extends RuntimeException {
    public DurationIsNotPositiveException(final String message) {
        super(message);
    }
}
