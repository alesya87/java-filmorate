package ru.yandex.practicum.filmorate.exception.user;

public class NoSuchUserException extends RuntimeException {
    public NoSuchUserException(final String message) {
        super(message);
    }
}
