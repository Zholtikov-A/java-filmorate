package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film addLike(Long filmId, Long userId) {
        boolean isFilmIdCorrect = false;
        for (Film film : filmStorage.findAll()) {
            if (film.getId() == filmId) {
                isFilmIdCorrect = true;
                break;
            }
        }
        if (!isFilmIdCorrect) {
            throw new FilmNotFoundException("User with ID \"" + userId + "\" not found");
        }
        boolean isUserIdCorrect = false;
        for (User user : userStorage.findAll()) {
            if (user.getId() == userId) {
                isUserIdCorrect = true;
                break;
            }
        }
        if (!isUserIdCorrect) {
            throw new FilmNotFoundException("User with ID \"" + userId + "\" not found");
        }
        filmStorage.findFilmById(filmId).getLikes().add(userId);
        return filmStorage.findFilmById(filmId);
    }

    public Film removeLike(Long filmId, Long userId) {
        boolean isFilmIdCorrect = false;
        for (Film film : filmStorage.findAll()) {
            if (film.getId() == filmId) {
                isFilmIdCorrect = true;
                break;
            }
        }
        if (!isFilmIdCorrect) {
            throw new FilmNotFoundException("User with ID \"" + userId + "\" not found");
        }
        boolean isUserIdCorrect = false;
        for (User user : userStorage.findAll()) {
            if (user.getId() == userId) {
                isUserIdCorrect = true;
                break;
            }
        }
        if (!isUserIdCorrect) {
            throw new FilmNotFoundException("User with ID \"" + userId + "\" not found");
        }
        filmStorage.findFilmById(filmId).getLikes().remove(userId);
        return filmStorage.findFilmById(filmId);
    }

    public List<Film> findPopularFilms(Integer count) {
        if (filmStorage.findAll().isEmpty()) {
            throw new FilmNotFoundException("Film collection is empty.");
        }
        return filmStorage.findAll().stream().sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size()).limit(count).collect(Collectors.toList());
    }

}




