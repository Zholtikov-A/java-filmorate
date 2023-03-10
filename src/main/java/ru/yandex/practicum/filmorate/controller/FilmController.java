package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final InMemoryFilmStorage filmStorage;

    @PostMapping()
    public Film create(@RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping()
    public Film update(@RequestBody Film film) {
        return filmStorage.update(film);
    }

    @GetMapping()
    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable("id") Long filmId) {
        return filmStorage.findFilmById(filmId);
    }

    @PutMapping("{id}/like/{userId}")
    public Film addLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.addLike(id, userId);

    }

    @DeleteMapping("{id}/like/{userId}")
    public Film removeLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.removeLike(id, userId);

    }

    @GetMapping("popular")
    public List<Film> findPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.findPopularFilms(count);
    }

}

