package ru.yandex.practicum.filmorate.exception.film;

public class NotValidRealiseDateException extends RuntimeException {
    public NotValidRealiseDateException(final String message) {
        super(message);
    }
}
