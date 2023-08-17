package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(path = "/mpa")
@AllArgsConstructor
@Validated
public class MpaController {
    @Autowired
    private final MpaService mpaService;

    @GetMapping("/{id}")
    public MpaRating getMpaRatingById(@PathVariable("id") @Valid @Positive Integer id) {
        return mpaService.getMpaRatingById(id);
    }

    @GetMapping
    public List<MpaRating> getAllMpaRatings() {
        return mpaService.getAllMpaRatings();
    }
}
