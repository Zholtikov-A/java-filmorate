package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @PostMapping()
    public Film create(@RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping()
    public Film update(@RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping()
    public List<Film> findAll() {
        return filmService.findAll();
    }

}

