package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    static UserController controller;


    @BeforeEach
    void init() {
        controller = new UserController();
    }

    @Test
    void createFilmSuccessfulCreationOfNewUser() {
        User user = User.builder()
                .id(controller.generateUserId())
                .email("user@usermail.com")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.of(1988, Month.MARCH, 28))
                .build();
        assertFalse(controller.findAll().contains(user));
        controller.create(user);
        assertTrue(controller.findAll().contains(user));
    }

    @Test
    void createFilmSuccessfulCreationOfNewUserWithBorderParametersAndNullNameField() {
        User user = User.builder()
                .id(controller.generateUserId())
                .email("U@")
                .login("U")
                .name(null)
                .birthday(LocalDate.from(LocalDate.now().atStartOfDay()))
                .build();
        assertFalse(controller.findAll().contains(user));
        controller.create(user);
        assertTrue(controller.findAll().contains(user));
    }

    @Test
    void createFilmSuccessfulCreationOfNewUserWithBorderParametersAndEmptyNameField() {
        User user = User.builder()
                .id(controller.generateUserId())
                .email("U@")
                .login("U")
                .name("")
                .birthday(LocalDate.from(LocalDate.now().atStartOfDay()))
                .build();
        assertFalse(controller.findAll().contains(user));
        controller.create(user);
        assertTrue(controller.findAll().contains(user));
    }

    @Test
    void createFilmSuccessfulCreationOfNewUserWithBorderParametersAndBlankNameField() {
        User user = User.builder()
                .id(controller.generateUserId())
                .email("F@")
                .login("U")
                .name("    ")
                .birthday(LocalDate.from(LocalDate.now().atStartOfDay()))
                .build();
        assertFalse(controller.findAll().contains(user));
        controller.create(user);
        assertTrue(controller.findAll().contains(user));
    }

    @Test
    void createFilmFailInCauseOfNullEmailField() {
        User user = User.builder()
                .id(controller.generateUserId())
                .email(null)
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.from(LocalDate.of(1988, Month.MARCH, 28).atStartOfDay()))
                .build();
        boolean isValidationException = false;
        try {
            controller.create(user);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Электронная почта не может быть пустой и должна содержать" +
                    " символ \"@\". Введено: \"null\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "Ожидалась ошибка валидации, но не произошла.");
    }

    @Test
    void createFilmFailInCauseOfEmptyEmailField() {
        User user = User.builder()
                .id(controller.generateUserId())
                .email("")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.from(LocalDate.of(1988, Month.MARCH, 28).atStartOfDay()))
                .build();
        boolean isValidationException = false;
        try {
            controller.create(user);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Электронная почта не может быть пустой и должна содержать" +
                    " символ \"@\". Введено: \"\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "Ожидалась ошибка валидации, но не произошла.");
    }

    @Test
    void createFilmFailInCauseOfBlankEmailField() {
        User user = User.builder()
                .id(controller.generateUserId())
                .email("    ")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.from(LocalDate.of(1988, Month.MARCH, 28).atStartOfDay()))
                .build();
        boolean isValidationException = false;
        try {
            controller.create(user);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Электронная почта не может быть пустой и должна содержать" +
                    " символ \"@\". Введено: \"    \"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "Ожидалась ошибка валидации, но не произошла.");
    }

    @Test
    void createFilmFailInCauseOfNullLoginField() {
        User user = User.builder()
                .id(controller.generateUserId())
                .email("user@usermail.com")
                .login(null)
                .name("Username Name")
                .birthday(LocalDate.from(LocalDate.of(1988, Month.MARCH, 28).atStartOfDay()))
                .build();
        boolean isValidationException = false;
        try {
            controller.create(user);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Логин не может быть пустым и содержать пробелы. Введено: \"null\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "Ожидалась ошибка валидации, но не произошла.");
    }

    @Test
    void createFilmFailInCauseOfEmptyLoginField() {
        User user = User.builder()
                .id(controller.generateUserId())
                .email("user@usermail.com")
                .login("")
                .name("Username Name")
                .birthday(LocalDate.from(LocalDate.of(1988, Month.MARCH, 28).atStartOfDay()))
                .build();
        boolean isValidationException = false;
        try {
            controller.create(user);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Логин не может быть пустым и содержать пробелы. Введено: \"\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "Ожидалась ошибка валидации, но не произошла.");
    }

    @Test
    void createFilmFailInCauseOfBlankLoginField() {
        User user = User.builder()
                .id(controller.generateUserId())
                .email("user@usermail.com")
                .login("Use r")
                .name("Username Name")
                .birthday(LocalDate.from(LocalDate.of(1988, Month.MARCH, 28).atStartOfDay()))
                .build();
        boolean isValidationException = false;
        try {
            controller.create(user);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Логин не может быть пустым и содержать пробелы. Введено: \"Use r\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "Ожидалась ошибка валидации, но не произошла.");
    }

    @Test
    void createFilmFailInCauseOfFutureBirthdayField() {
        User user = User.builder()
                .id(controller.generateUserId())
                .email("user@usermail.com")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.MAX)
                .build();
        boolean isValidationException = false;
        try {
            controller.create(user);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Дата рождения не может быть в будущем. Введенная дата рождения: +999999999-12-31");
            isValidationException = true;
        }
        assertTrue(isValidationException, "Ожидалась ошибка валидации, но не произошла.");
    }

}