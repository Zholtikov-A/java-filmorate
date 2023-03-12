package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmService {

    final Map<Long, Film> films = new HashMap<>();
    final int maxFilmDescriptionLength = 200;
    final LocalDate minFilmRealiseDate = LocalDate.of(1895, Month.DECEMBER, 28);
    Long filmIdCounter = 0L;

    Long generateFilmId() {
        return ++filmIdCounter;
    }

    @PostMapping()
    public Film create(Film film) {
        if (film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            String message = "Field \"name\" can't be empty/null. Input received: \"" + film.getName() + "\"";
            log.info("Validation failed! " + message);
            throw new ValidationException(message);
        } else if (film.getDescription().length() > maxFilmDescriptionLength) {
            String message = "Field \"description\" length can't be more then " + maxFilmDescriptionLength + " symbols." +
                    " Received input length: " + film.getDescription().length() + " symbols";
            log.info("Validation failed! " + message);
            throw new ValidationException(message);
        } else if (film.getReleaseDate().isBefore(minFilmRealiseDate)) {
            String message = "Field \"releaseDate\" can't contain date early then \"" + minFilmRealiseDate + "\". " +
                    "Input received: \"" + film.getReleaseDate() + "\"";
            log.info("Validation failed! " + message);
            throw new ValidationException(message);
        } else if (film.getDuration() < 0) {
            String message = "Value of field \"duration\" can't be negative. Input received: \"" + film.getDuration() + "\"";
            log.info("Validation failed! " + message);
            throw new ValidationException(message);
        } else {
            film.setId(generateFilmId());
            films.put(film.getId(), film);
            log.info("New film created: " + film);
            return film;
        }
    }

    @PutMapping()
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            String message = "Film with such ID not found. Input received: \"" + film.getId() + "\"";
            log.info("Validation failed! " + message);
            throw new ValidationException(message);
        } else if (film.getName() == null || film.getName().isEmpty() || film.getName().isBlank()) {
            String message = "Field \"name\" can't be empty/null. Input received: \"" + film.getName() + "\"";
            log.info("Validation failed! " + message);
            throw new ValidationException(message);
        } else if (film.getDescription().length() > maxFilmDescriptionLength) {
            String message = "Field \"description\" length can't be more then " + maxFilmDescriptionLength + " symbols. Received input length: "
                    + film.getDescription().length() + " symbols";
            log.info("Validation failed! " + message);
            throw new ValidationException(message);
        } else if (film.getReleaseDate().isBefore(minFilmRealiseDate)) {
            String message = "Field \"releaseDate\" can't contain date early then \"" + minFilmRealiseDate + "\". Input received: \"" + film.getReleaseDate() + "\"";
            log.info("Validation failed! " + message);
            throw new ValidationException(message);
        } else if (film.getDuration() < 0) {
            String message = "Value of field \"duration\" can't be negative. Input received: \"" + film.getDuration() + "\"";
            log.info("Validation failed! " + message);
            throw new ValidationException(message);
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

