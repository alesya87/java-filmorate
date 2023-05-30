package ru.yandex.practicum.filmorate.exception.user;

public class NotValidEmailException extends RuntimeException {
    public NotValidEmailException(final String message) {
        super(message);
    }
}
