package ru.yandex.practicum.filmorate.exception.user;

public class NotValidBirthdayException extends RuntimeException {
    public NotValidBirthdayException(final String message) {
        super(message);
    }
}
