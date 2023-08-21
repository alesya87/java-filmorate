package ru.yandex.practicum.filmorate.controller.request.mapper;

import ru.yandex.practicum.filmorate.controller.request.MpaRatingDto;
import ru.yandex.practicum.filmorate.model.MpaRating;

public class MpaRatingMapper {
    public static MpaRatingDto convert(MpaRating mpaRating) {
        return MpaRatingDto.builder()
                .id(mpaRating.getId())
                .name(mpaRating.getName())
                .build();
    }
}
