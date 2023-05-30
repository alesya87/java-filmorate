package ru.yandex.practicum.filmorate.exception.film;

public class DescriptionLengthMoreThanLimitException extends RuntimeException {
    public DescriptionLengthMoreThanLimitException(final String message) {
        super(message);
    }
}
