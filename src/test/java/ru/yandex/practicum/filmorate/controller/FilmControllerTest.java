package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    static FilmController controller;
    static FilmService service;

    @BeforeEach
    void init() {
        service = new FilmService();
        controller = new FilmController(service);
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
        assertFalse(controller.findAll().contains(film), "List of films contains such film, but should not.");
        controller.create(film);
        assertTrue(controller.findAll().contains(film), "List of films don't contains such film. " +
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
            controller.create(film);
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
            controller.create(film);
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
            controller.create(film);
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
            controller.create(film);
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
            controller.create(film);
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
            controller.create(film);
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
        assertFalse(controller.findAll().contains(initialFilm), "List of films contains such film, but should not.");
        controller.create(initialFilm);
        assertTrue(controller.findAll().contains(initialFilm), "List of films don't contains such film. " +
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
        assertFalse(controller.findAll().contains(updatedFilm), "List of users contains such user, but should not.");
        controller.update(updatedFilm);
        assertTrue(controller.findAll().contains(updatedFilm), "List of users don't contains such user. " +
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
        assertFalse(controller.findAll().contains(initialFilm), "List of films contains such film, but should not.");
        controller.create(initialFilm);
        assertTrue(controller.findAll().contains(initialFilm), "List of films don't contains such film. " +
                "Film creation failed");
        try {
            Film updatedFilm = Film.builder()
                    .name(null)
                    .description("Film about test")
                    .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                    .duration(90)
                    .id(initialFilm.getId())
                    .build();
            controller.update(updatedFilm);
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
        assertFalse(controller.findAll().contains(initialFilm), "List of films contains such film, but should not.");
        controller.create(initialFilm);
        assertTrue(controller.findAll().contains(initialFilm), "List of films don't contains such film. " +
                "Film creation failed");
        try {
            Film updatedFilm = Film.builder()
                    .name("")
                    .description("Film about test")
                    .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                    .duration(90)
                    .id(initialFilm.getId())
                    .build();
            controller.update(updatedFilm);
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
        assertFalse(controller.findAll().contains(initialFilm), "List of films contains such film, but should not.");
        controller.create(initialFilm);
        assertTrue(controller.findAll().contains(initialFilm), "List of films don't contains such film. " +
                "Film creation failed");
        try {
            Film updatedFilm = Film.builder()
                    .name("    ")
                    .description("Film about test")
                    .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                    .duration(90)
                    .id(initialFilm.getId())
                    .build();
            controller.update(updatedFilm);
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
        assertFalse(controller.findAll().contains(initialFilm), "List of films contains such film, but should not.");
        controller.create(initialFilm);
        assertTrue(controller.findAll().contains(initialFilm), "List of films don't contains such film. " +
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
            controller.update(updatedFilm);
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
        assertFalse(controller.findAll().contains(initialFilm), "List of films contains such film, but should not.");
        controller.create(initialFilm);
        assertTrue(controller.findAll().contains(initialFilm), "List of films don't contains such film. " +
                "Film creation failed");
        try {
            Film updatedFilm = Film.builder()
                    .name("Film")
                    .description("Film about test")
                    .releaseDate(LocalDate.of(1895, Month.DECEMBER, 27))
                    .duration(90)
                    .id(initialFilm.getId())
                    .build();
            controller.update(updatedFilm);
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
        assertFalse(controller.findAll().contains(initialFilm), "List of films contains such film, but should not.");
        controller.create(initialFilm);
        assertTrue(controller.findAll().contains(initialFilm), "List of films don't contains such film. " +
                "Film creation failed");
        try {
            Film updatedFilm = Film.builder()
                    .name("Film")
                    .description("Film about test")
                    .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                    .duration(-1)
                    .id(initialFilm.getId())
                    .build();
            controller.update(updatedFilm);
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
        assertFalse(controller.findAll().contains(initialFilm), "List of films contains such film, but should not.");
        controller.create(initialFilm);
        assertTrue(controller.findAll().contains(initialFilm), "List of films don't contains such film. " +
                "Film creation failed");
        try {
            Film updatedFilm = Film.builder()
                    .name("Film")
                    .description("Film about test")
                    .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                    .duration(90)
                    .id(-1)
                    .build();
            controller.update(updatedFilm);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Film with such ID not found. Input received: \"-1\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void findAllSuccessfulCreationOfFilmList() {
        final List<Film> emptyList = controller.findAll();
        assertNotNull(emptyList, "Method returns null");
        assertTrue(emptyList.isEmpty(), "Method returns not empty list.");
        Film film = Film.builder()
                .name("Film")
                .description("Film about test")
                .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                .duration(90)
                .build();
        controller.create(film);
        final List<Film> notEmptyList = controller.findAll();
        assertFalse(notEmptyList.isEmpty(), "Method returns empty list");
        assertTrue(controller.findAll().contains(film), "List of films don't contains such film.");
    }

}
