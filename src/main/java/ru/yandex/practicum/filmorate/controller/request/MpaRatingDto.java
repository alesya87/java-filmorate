package ru.yandex.practicum.filmorate.controller.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MpaRatingDto {
    private Integer id;
    private String name;
}
