package ru.yandex.practicum.filmorate.exception;

public class NoSuchUserException extends RuntimeException {
    public NoSuchUserException(final String message) {
        super(message);
    }
}
