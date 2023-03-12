
package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {


    static FilmService filmService;
    static FilmStorage filmStorage;
    static UserStorage userStorage;
    static FilmController filmController;

    @BeforeEach
    void init() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmService);
    }

    @Test
    void createFilmSuccessfulCreationOfNewFilmWithBorderParameters() {
        Film film = Film.builder()
                .name("F")
                .description("Film about test and it description length must be about 200 symbols and so here they are:" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaa")
                .releaseDate(LocalDate.of(1895, Month.DECEMBER, 28))
                .duration(0)
                .build();
        assertFalse(filmController.findAll().contains(film), "List of films contains such film, but should not.");
        filmController.create(film);
        assertTrue(filmController.findAll().contains(film), "List of films don't contains such film. " +
                "Film creation failed");
    }

    @Test
    void createFilmFailInCauseOfNullNameField() {
        Film film = Film.builder()
                .name(null)
                .description("Film about test")
                .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                .duration(90)
                .build();
        boolean isValidationException = false;
        try {
            filmController.create(film);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"name\" can't be empty/null. Input received: \"null\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void createFilmFailInCauseOfEmptyNameField() {
        Film film = Film.builder()
                .name("")
                .description("Film about test")
                .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                .duration(90)
                .build();
        boolean isValidationException = false;
        try {
            filmController.create(film);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"name\" can't be empty/null. Input received: \"\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void createFilmFailInCauseOfBlankNameField() {
        Film film = Film.builder()
                .name("    ")
                .description("Film about test")
                .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                .duration(90)
                .build();
        boolean isValidationException = false;
        try {
            filmController.create(film);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"name\" can't be empty/null. Input received: \"    \"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void createFilmFailInCauseOfDescriptionLengthExceeding() {
        Film film = Film.builder()
                .name("Film")
                .description("Film about test film about test film about test film about test film about test " +
                        "film about test film about test film about test film about test film about test film about test" +
                        " film about test film abou")
                .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                .duration(90)
                .build();
        boolean isValidationException = false;
        try {
            filmController.create(film);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"description\" length can't be more then 200 symbols. Received input length: 201 symbols");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void createFilmFailInCauseOfEarlyReleaseDate() {
        Film film = Film.builder()
                .name("Film")
                .description("Film about test")
                .releaseDate(LocalDate.of(1895, Month.DECEMBER, 27))
                .duration(90)
                .build();
        boolean isValidationException = false;
        try {
            filmController.create(film);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"releaseDate\" can't contain date early then " +
                    "\"1895-12-28\". Input received: \"1895-12-27\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void createFilmFailInCauseOfNegativeFilmDuration() {
        Film film = Film.builder()
                .name("Film")
                .description("Film about test")
                .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                .duration(-1)
                .build();
        boolean isValidationException = false;
        try {
            filmController.create(film);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Value of field \"duration\" can't be negative. Input received: \"-1\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void updateSuccessfulUpdateFilmWithBorderParameters() {
        Film initialFilm = Film.builder()
                .name("F")
                .description("Film about test and it description length must be about 200 symbols and so here they are:" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaa")
                .releaseDate(LocalDate.of(1895, Month.DECEMBER, 29))
                .duration(1)
                .build();
        assertFalse(filmController.findAll().contains(initialFilm), "List of films contains such film, but should not.");
        filmController.create(initialFilm);
        assertTrue(filmController.findAll().contains(initialFilm), "List of films don't contains such film. " +
                "Film creation failed");
        Film updatedFilm = Film.builder()
                .name("N")
                .description("Film about test and it description length must be about 200 symbols and so here they are:" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaabbb")
                .releaseDate(LocalDate.of(1895, Month.DECEMBER, 28))
                .duration(0)
                .id(initialFilm.getId())
                .build();
        assertFalse(filmController.findAll().contains(updatedFilm), "List of users contains such user, but should not.");
        filmController.update(updatedFilm);
        assertTrue(filmController.findAll().contains(updatedFilm), "List of users don't contains such user. " +
                "User update failed");
        assertNotEquals(initialFilm, updatedFilm, "Initial user is equals updated user. User update failed");
    }

    @Test
    void updateFilmFailInCauseOfNullNameField() {
        Film initialFilm = Film.builder()
                .name("Film")
                .description("Film about test")
                .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                .duration(90)
                .build();
        boolean isValidationException = false;
        assertFalse(filmController.findAll().contains(initialFilm), "List of films contains such film, but should not.");
        filmController.create(initialFilm);
        assertTrue(filmController.findAll().contains(initialFilm), "List of films don't contains such film. " +
                "Film creation failed");
        try {
            Film updatedFilm = Film.builder()
                    .name(null)
                    .description("Film about test")
                    .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                    .duration(90)
                    .id(initialFilm.getId())
                    .build();
            filmController.update(updatedFilm);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"name\" can't be empty/null. Input received: \"null\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void updateFilmFailInCauseOfEmptyNameField() {
        Film initialFilm = Film.builder()
                .name("Film")
                .description("Film about test")
                .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                .duration(90)
                .build();
        boolean isValidationException = false;
        assertFalse(filmController.findAll().contains(initialFilm), "List of films contains such film, but should not.");
        filmController.create(initialFilm);
        assertTrue(filmController.findAll().contains(initialFilm), "List of films don't contains such film. " +
                "Film creation failed");
        try {
            Film updatedFilm = Film.builder()
                    .name("")
                    .description("Film about test")
                    .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                    .duration(90)
                    .id(initialFilm.getId())
                    .build();
            filmController.update(updatedFilm);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"name\" can't be empty/null. Input received: \"\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void updateFilmFailInCauseOfBlankNameField() {
        Film initialFilm = Film.builder()
                .name("Film")
                .description("Film about test")
                .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                .duration(90)
                .build();
        boolean isValidationException = false;
        assertFalse(filmController.findAll().contains(initialFilm), "List of films contains such film, but should not.");
        filmController.create(initialFilm);
        assertTrue(filmController.findAll().contains(initialFilm), "List of films don't contains such film. " +
                "Film creation failed");
        try {
            Film updatedFilm = Film.builder()
                    .name("    ")
                    .description("Film about test")
                    .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                    .duration(90)
                    .id(initialFilm.getId())
                    .build();
            filmController.update(updatedFilm);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"name\" can't be empty/null. Input received: \"    \"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void updateFilmFailInCauseOfDescriptionLengthExceeding() {
        Film initialFilm = Film.builder()
                .name("Film")
                .description("Film about test")
                .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                .duration(90)
                .build();
        boolean isValidationException = false;
        assertFalse(filmController.findAll().contains(initialFilm), "List of films contains such film, but should not.");
        filmController.create(initialFilm);
        assertTrue(filmController.findAll().contains(initialFilm), "List of films don't contains such film. " +
                "Film creation failed");
        try {
            Film updatedFilm = Film.builder()
                    .name("Film")
                    .description("\"Film about test and it description length must be about 200 symbols and so here they are:" +
                            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                            "aaaaaaaaaaaaaaaaaaaaaaa")
                    .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                    .duration(90)
                    .id(initialFilm.getId())
                    .build();
            filmController.update(updatedFilm);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"description\" length can't be more then 200 symbols. Received input length: 201 symbols");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void updateFilmFailInCauseOfEarlyReleaseDate() {
        Film initialFilm = Film.builder()
                .name("Film")
                .description("Film about test")
                .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                .duration(90)
                .build();
        boolean isValidationException = false;
        assertFalse(filmController.findAll().contains(initialFilm), "List of films contains such film, but should not.");
        filmController.create(initialFilm);
        assertTrue(filmController.findAll().contains(initialFilm), "List of films don't contains such film. " +
                "Film creation failed");
        try {
            Film updatedFilm = Film.builder()
                    .name("Film")
                    .description("Film about test")
                    .releaseDate(LocalDate.of(1895, Month.DECEMBER, 27))
                    .duration(90)
                    .id(initialFilm.getId())
                    .build();
            filmController.update(updatedFilm);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"releaseDate\" can't contain date early then " +
                    "\"1895-12-28\". Input received: \"1895-12-27\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void updateFilmFailInCauseOfNegativeFilmDuration() {
        Film initialFilm = Film.builder()
                .name("Film")
                .description("Film about test")
                .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                .duration(90)
                .build();
        boolean isValidationException = false;
        assertFalse(filmController.findAll().contains(initialFilm), "List of films contains such film, but should not.");
        filmController.create(initialFilm);
        assertTrue(filmController.findAll().contains(initialFilm), "List of films don't contains such film. " +
                "Film creation failed");
        try {
            Film updatedFilm = Film.builder()
                    .name("Film")
                    .description("Film about test")
                    .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                    .duration(-1)
                    .id(initialFilm.getId())
                    .build();
            filmController.update(updatedFilm);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Value of field \"duration\" can't be negative. Input received: \"-1\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void updateFilmFailInCauseOfNonexistentFilmId() {
        Film initialFilm = Film.builder()
                .name("Film")
                .description("Film about test")
                .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                .duration(90)
                .build();
        boolean isValidationException = false;
        assertFalse(filmController.findAll().contains(initialFilm), "List of films contains such film, but should not.");
        filmController.create(initialFilm);
        assertTrue(filmController.findAll().contains(initialFilm), "List of films don't contains such film. " +
                "Film creation failed");
        try {
            Film updatedFilm = Film.builder()
                    .name("Film")
                    .description("Film about test")
                    .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                    .duration(90)
                    .id(-1)
                    .build();
            filmController.update(updatedFilm);
        } catch (FilmNotFoundException exception) {
            assertEquals(exception.getMessage(), "Film with id \"-1\" not found.");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void findAllSuccessfulCreationOfFilmList() {
        final List<Film> emptyList = filmController.findAll();
        assertNotNull(emptyList, "Method returns null");
        assertTrue(emptyList.isEmpty(), "Method returns not empty list.");
        Film film = Film.builder()
                .name("Film")
                .description("Film about test")
                .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                .duration(90)
                .build();
        filmController.create(film);
        final List<Film> notEmptyList = filmController.findAll();
        assertFalse(notEmptyList.isEmpty(), "Method returns empty list");
        assertTrue(filmController.findAll().contains(film), "List of films don't contains such film.");
    }

    @Test
    void getFilmByIdSuccessfulReturnFilmWithId1() {
        Film film = Film.builder()
                .name("Film")
                .description("Film about test")
                .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                .duration(90)
                .build();
        assertFalse(filmController.findAll().contains(film), "List of films contains such film, but should not.");
        filmController.create(film);
        assertTrue(filmController.findAll().contains(film), "List of films don't contains such film. " +
                "Film creation failed");
        Film savedFilm = filmController.findFilmById(1L);
        film.setId(savedFilm.getId());
        assertEquals(film, savedFilm, "Films are not equals.");
    }

    @Test
    void getFilmByIdFailReturnFilmWithNegativeId() {
        Film film = Film.builder()
                .name("Film")
                .description("Film about test")
                .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                .duration(90)
                .build();
        assertFalse(filmController.findAll().contains(film), "List of films contains such film, but should not.");
        filmController.create(film);
        assertTrue(filmController.findAll().contains(film), "List of users don't contains such user. " +
                "User creation failed");
        boolean isFilmNotFoundException = false;
        try {
            Film savedFilm = filmController.findFilmById(-1L);
            film.setId(savedFilm.getId());
            assertNotEquals(film, savedFilm, "Films are equals.");
        } catch (FilmNotFoundException exception) {
            assertEquals(exception.getMessage(), "Film with id \"-1\" not found.");
            isFilmNotFoundException = true;
        }
        assertTrue(isFilmNotFoundException, "FilmNotFoundException expected, but didn't appear.");

    }

}
