package ru.yandex.practicum.filmorate.exception.user;

public class NotValidLoginException extends RuntimeException {
    public NotValidLoginException(final String message) {
        super(message);
    }
}
