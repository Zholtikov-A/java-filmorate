
package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    static FilmController controller;


    @BeforeEach
    void init() {
        controller = new FilmController();
    }


    @Test
    void createFilmSuccessfulCreationOfNewFilmWithBorderParameters() {
        Film film = Film.builder()
                .id(1L)
                .name("F")
                .description("Film about test and it description length must be about 200 symbols and so here they are:" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaa")
                .releaseDate(LocalDate.of(1895, Month.DECEMBER, 28))
                .duration(0)
                .build();
        assertFalse(controller.findAll().contains(film));
        controller.create(film);
        assertTrue(controller.findAll().contains(film));
    }

    @Test
    void createFilmFailInCauseOfNullNameField() {
        Film film = Film.builder()
                .id(1L)
                .name(null)
                .description("Film about test")
                .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                .duration(90)
                .build();
        boolean isValidationException = false;
        try {
            controller.create(film);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Название фильма не может быть пустым. Введено: \"null\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "Ожидалась ошибка валидации, но не произошла.");
    }

    @Test
    void createFilmFailInCauseOfEmptyNameField() {
        Film film = Film.builder()
                .id(1L)
                .name("")
                .description("Film about test")
                .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                .duration(90)
                .build();
        boolean isValidationException = false;
        try {
            controller.create(film);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Название фильма не может быть пустым. Введено: \"\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "Ожидалась ошибка валидации, но не произошла.");
    }

    @Test
    void createFilmFailInCauseOfBlankNameField() {
        Film film = Film.builder()
                .id(1L)
                .name("    ")
                .description("Film about test")
                .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                .duration(90)
                .build();
        boolean isValidationException = false;
        try {
            controller.create(film);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Название фильма не может быть пустым. Введено: \"    \"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "Ожидалась ошибка валидации, но не произошла.");
    }

    @Test
    void createFilmFailInCauseOfDescriptionLengthExceeding() {
        Film film = Film.builder()
                .id(1L)
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
            assertEquals(exception.getMessage(), "Максимальная длина описания - 200 символов, введенная длина описания: 201");
            isValidationException = true;
        }
        assertTrue(isValidationException, "Ожидалась ошибка валидации, но не произошла.");
    }

    @Test
    void createFilmFailInCauseOfEarlyReleaseDate() {
        Film film = Film.builder()
                .id(1L)
                .name("Film")
                .description("Film about test")
                .releaseDate(LocalDate.of(1895, Month.DECEMBER, 27))
                .duration(90)
                .build();
        boolean isValidationException = false;
        try {
            controller.create(film);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Дата релиза - не раньше 28 декабря 1895 года. " +
                    "Введенная дата релиза: 1895-12-27");
            isValidationException = true;
        }
        assertTrue(isValidationException, "Ожидалась ошибка валидации, но не произошла.");
    }

    @Test
    void createFilmFailInCauseOfNegativeFilmDuration() {
        Film film = Film.builder()
                .id(1L)
                .name("Film")
                .description("Film about test")
                .releaseDate(LocalDate.of(2023, Month.FEBRUARY, 10))
                .duration(-1)
                .build();
        boolean isValidationException = false;
        try {
            controller.create(film);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Продолжительность фильма должна быть положительной.");
            isValidationException = true;
        }
        assertTrue(isValidationException, "Ожидалась ошибка валидации, но не произошла.");
    }

}
