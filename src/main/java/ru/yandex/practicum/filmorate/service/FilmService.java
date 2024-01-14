package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmService {

    FilmDao filmDao;
    UserDao userDao;

    public Film addLike(Long filmId, Long userId) {
        userDao.checkUserExistence(userId);
        filmDao.checkFilmExistence(filmId);

        filmDao.addLike(filmId, userId);
        log.info("Like was added to film");
        return filmDao.findFilmById(filmId);
    }

    public Film removeLike(Long filmId, Long userId) {
        userDao.checkUserExistence(userId);
        filmDao.checkFilmExistence(filmId);
        filmDao.removeLike(filmId, userId);
        log.info("Like was removed from film");
        return filmDao.findFilmById(filmId);
    }

    public List<Film> findPopularFilms(Integer count) {
        List<Film> films = filmDao.findAll();
        if (films.isEmpty()) {
            String message = "Film collection is empty.";
            log.debug(message);
            throw new FilmNotFoundException(message);
        }
        return films.stream()
                .sorted((film1, film2) -> film2.getRate() - film1.getRate())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film create(Film film) {
        log.info("Film " + film.getName() + " was successfully saved!");
        return filmDao.create(film);
    }

    public Film update(Film film) {
        filmDao.checkFilmExistence(film.getId());
        log.info("Film " + film.getName() + " was successfully updated!");
        return filmDao.update(film);
    }

    public List<Film> findAll() {
        return filmDao.findAll();
    }

    public Film findFilmById(Long id) {
        filmDao.checkFilmExistence(id);
        return filmDao.findFilmById(id);
    }

}