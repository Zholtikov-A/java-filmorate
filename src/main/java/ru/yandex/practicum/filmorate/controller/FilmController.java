package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap();
    protected Long filmId = 0L;

    public Long generateFilmId() {
        return ++filmId;
    }

    private final int maxFilmDescriptionLength = 200;
    private final LocalDate minFilmRealiseDate = LocalDate.of(1895, Month.DECEMBER, 28);

    @PostMapping()
    public Film create(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            log.info("Валидация не пройдена.");
            throw new ValidationException("Название фильма не может быть пустым. Введено: " + "\"" + film.getName() + "\"");
        } else if (film.getDescription().length() > maxFilmDescriptionLength) {
            log.info("Валидация не пройдена.");
            throw new ValidationException(("Максимальная длина описания - 200 символов, введенная длина описания: "
                    + film.getDescription().length()));
        } else if (film.getReleaseDate().isBefore(minFilmRealiseDate)) {
            log.info("Валидация не пройдена.");
            throw new ValidationException("Дата релиза - не раньше 28 декабря 1895 года. Введенная дата релиза: "
                    + film.getReleaseDate().toString());
        } else if (film.getDuration() < 0) {
            log.info("Валидация не пройдена.");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        } else {
            film.setId(generateFilmId());
            films.put(film.getId(), film);
            log.info(film.toString());
            return film;
        }
    }

    @PutMapping()
    public Film update(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.info("Валидация не пройдена.");
            throw new ValidationException("Введен несуществующий Id. Введенный Id: " + film.getId());
        } else if (film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            log.info("Валидация не пройдена.");
            throw new ValidationException("Название фильма не может быть пустым.");
        } else if (film.getDescription().length() > maxFilmDescriptionLength) {
            log.info("Валидация не пройдена.");
            throw new ValidationException(("Максимальная длина описания - 200 символов, введенная длина описания: "
                    + film.getDescription().length()));
        } else if (film.getReleaseDate().isBefore(minFilmRealiseDate)) {
            log.info("Валидация не пройдена.");
            throw new ValidationException("Дата релиза - не раньше 28 декабря 1895 года. Введенная дата релиза: "
                    + film.getReleaseDate().toString());
        } else if (film.getDuration() < 0) {
            log.info("Валидация не пройдена.");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        } else {
            films.put(film.getId(), film);
            log.info(film.toString());
            return film;
        }
    }

    @GetMapping()
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

}

