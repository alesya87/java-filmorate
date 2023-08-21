package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.request.GenreDto;
import ru.yandex.practicum.filmorate.controller.request.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/genres")
@AllArgsConstructor
@Validated
public class GenreController {
    @Autowired
    private final GenreService genreService;

    @GetMapping("/{id}")
    public GenreDto getGenreById(@PathVariable("id") @Valid @Positive Integer id) {
        return GenreMapper.convert(genreService.getGenreById(id));
    }

    @GetMapping
    public List<GenreDto> getAllGenres() {
        List<GenreDto> genreDtos = new ArrayList<>();
        for (Genre genre : genreService.getAllGenres()) {
            genreDtos.add(GenreMapper.convert(genre));
        }
        return genreDtos;
    }
}
