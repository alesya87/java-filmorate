package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MpaRating {
    private Integer id;
    private String name; // TODO: нужно ли это поле?
}

