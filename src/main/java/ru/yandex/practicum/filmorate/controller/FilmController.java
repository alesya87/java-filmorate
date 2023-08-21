package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.request.FilmWithoutIdDto;
import ru.yandex.practicum.filmorate.controller.request.FilmFullDto;
import ru.yandex.practicum.filmorate.controller.request.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.Validator;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/films")
@AllArgsConstructor
@Validated
public class FilmController {
    @Autowired
    private final FilmService filmService;

    @PostMapping
    public FilmFullDto add(@Valid @RequestBody FilmWithoutIdDto filmWithoutIdDto) {
        Validator.checkReleaseDate(filmWithoutIdDto);
        Film film = filmService.add(FilmMapper.convert(filmWithoutIdDto));
        return FilmMapper.convert(film);
    }

    @PutMapping
    public FilmFullDto update(@Valid @RequestBody FilmFullDto filmFullDto) {
        Validator.checkReleaseDate(filmFullDto);
        Film film = filmService.update(FilmMapper.convert(filmFullDto));
        return FilmMapper.convert(film);
    }

    @GetMapping("/{id}")
    public FilmFullDto getFilmById(@PathVariable("id") @Valid @Positive Integer id) {
        return FilmMapper.convert(filmService.getFilmById(id));
    }

    @GetMapping
    public List<FilmFullDto> getAllFilms() {
        List<FilmFullDto> filmFullDtos = new ArrayList<>();
        for (Film film : filmService.getAllFilms()) {
            filmFullDtos.add(FilmMapper.convert(film));
        }
        return filmFullDtos;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") @Valid @Positive Integer id, @PathVariable("userId") @Valid @Positive Integer userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") @Valid @Positive Integer id, @PathVariable("userId") @Valid @Positive Integer userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<FilmFullDto> getFilmsByLikesCount(@RequestParam(defaultValue = "10", required = false) Integer count) {
        List<FilmFullDto> filmFullDtos = new ArrayList<>();
        for (Film film : filmService.getFilmsByLikesCount(count)) {
            filmFullDtos.add(FilmMapper.convert(film));
        }
        return filmFullDtos;
    }
}
