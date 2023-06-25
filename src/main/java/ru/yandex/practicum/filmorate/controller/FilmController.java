package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping(path = "/films")
@AllArgsConstructor
public class FilmController {
    @Autowired
    private final FilmService filmService;

    @PostMapping
    public Film add(@RequestBody Film film) {
        return filmService.add(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Integer id) {
        return filmService.getFilmById(id);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getFilmsByLikesCount(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.getFilmsByLikesCount(count);
    }
}
