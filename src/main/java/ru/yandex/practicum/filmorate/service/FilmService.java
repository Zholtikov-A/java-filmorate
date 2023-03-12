package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findFilmById(Long filmId) {
        return filmStorage.findFilmById(filmId);
    }

    public Film addLike(Long filmId, Long userId) {
        Film film = filmStorage.findFilmById(filmId);
        userStorage.findUserById(userId);       //throws UserNotFoundException
        film.getLikes().add(userId);
        return film;
    }

    public Film removeLike(Long filmId, Long userId) {
        Film film = filmStorage.findFilmById(filmId);
        userStorage.findUserById(userId);        //throws UserNotFoundException
        film.getLikes().remove(userId);
        return film;
    }

    public List<Film> findPopularFilms(Integer count) {
        List<Film> films = filmStorage.findAll();
        if (films.isEmpty()) {
            throw new FilmNotFoundException("Film collection is empty.");
        }
        return films.stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

}




