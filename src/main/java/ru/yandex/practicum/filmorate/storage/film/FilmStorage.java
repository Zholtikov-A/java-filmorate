package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    List<Film> findAll();

    Film findFilmById(Long filmId);

}

