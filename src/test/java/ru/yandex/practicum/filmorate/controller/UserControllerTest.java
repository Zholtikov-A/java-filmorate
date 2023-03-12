
package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    static UserStorage userStorage;
    static UserService userService;
    static UserController userController;


    @BeforeEach
    void init() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        userController = new UserController(userService);
    }

    @Test
    void createUserSuccessfulCreationOfNewUser() {
        User user = User.builder()
                .email("user@usermail.com")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.of(1988, Month.MARCH, 28))
                .build();
        assertFalse(userController.findAll().contains(user), "List of users contains such user, but should not.");
        userController.create(user);
        assertTrue(userController.findAll().contains(user), "List of users don't contains such user. " +
                "User creation failed");
    }

    @Test
    void createUserSuccessfulCreationOfNewUserWithBorderParametersAndNullNameField() {
        User user = User.builder()
                .email("U@")
                .login("U")
                .name(null)
                .birthday(LocalDate.from(LocalDate.now().atStartOfDay()))
                .build();
        assertFalse(userController.findAll().contains(user), "List of users contains such user, but should not.");
        userController.create(user);
        assertTrue(userController.findAll().contains(user), "List of users don't contains such user. " +
                "User creation failed");
    }

    @Test
    void createUserSuccessfulCreationOfNewUserWithBorderParametersAndEmptyNameField() {
        User user = User.builder()
                .email("U@")
                .login("U")
                .name("")
                .birthday(LocalDate.from(LocalDate.now().atStartOfDay()))
                .build();
        assertFalse(userController.findAll().contains(user), "List of users contains such user, but should not.");
        userController.create(user);
        assertTrue(userController.findAll().contains(user), "List of users don't contains such user. " +
                "User creation failed");
    }

    @Test
    void createUserSuccessfulCreationOfNewUserWithBorderParametersAndBlankNameField() {
        User user = User.builder()
                .email("F@")
                .login("U")
                .name("    ")
                .birthday(LocalDate.from(LocalDate.now().atStartOfDay()))
                .build();
        assertFalse(userController.findAll().contains(user), "List of users contains such user, but should not.");
        userController.create(user);
        assertTrue(userController.findAll().contains(user), "List of users don't contains such user. " +
                "User creation failed");
    }

    @Test
    void createUserFailInCauseOfNullEmailField() {
        User user = User.builder()
                .email(null)
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.from(LocalDate.of(1988, Month.MARCH, 28).atStartOfDay()))
                .build();
        boolean isValidationException = false;
        try {
            userController.create(user);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"email\" can't be empty/null and must contains" +
                    " \"@\" symbol. Input received: \"null\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void createUserFailInCauseOfEmptyEmailField() {
        User user = User.builder()
                .email("")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.from(LocalDate.of(1988, Month.MARCH, 28).atStartOfDay()))
                .build();
        boolean isValidationException = false;
        try {
            userController.create(user);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"email\" can't be empty/null and must contains" +
                    " \"@\" symbol. Input received: \"\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void createUserFailInCauseOfBlankEmailField() {
        User user = User.builder()
                .email("    ")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.from(LocalDate.of(1988, Month.MARCH, 28).atStartOfDay()))
                .build();
        boolean isValidationException = false;
        try {
            userController.create(user);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"email\" can't be empty/null and must contains" +
                    " \"@\" symbol. Input received: \"    \"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void createUserFailInCauseOfNullLoginField() {
        User user = User.builder()
                .email("user@usermail.com")
                .login(null)
                .name("Username Name")
                .birthday(LocalDate.from(LocalDate.of(1988, Month.MARCH, 28).atStartOfDay()))
                .build();
        boolean isValidationException = false;
        try {
            userController.create(user);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"login\" can't be empty/null or contains space symbol. " +
                    "Input received: \"null\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void createUserFailInCauseOfEmptyLoginField() {
        User user = User.builder()
                .email("user@usermail.com")
                .login("")
                .name("Username Name")
                .birthday(LocalDate.from(LocalDate.of(1988, Month.MARCH, 28).atStartOfDay()))
                .build();
        boolean isValidationException = false;
        try {
            userController.create(user);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"login\" can't be empty/null or contains space symbol. " +
                    "Input received: \"\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void createUserFailInCauseOfBlankLoginField() {
        User user = User.builder()
                .email("user@usermail.com")
                .login("Use r")
                .name("Username Name")
                .birthday(LocalDate.from(LocalDate.of(1988, Month.MARCH, 28).atStartOfDay()))
                .build();
        boolean isValidationException = false;
        try {
            userController.create(user);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"login\" can't be empty/null or contains space symbol. " +
                    "Input received: \"Use r\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void createUserFailInCauseOfFutureBirthdayField() {
        User user = User.builder()
                .email("user@usermail.com")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.MAX)
                .build();
        boolean isValidationException = false;
        try {
            userController.create(user);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"birthday\" can't contain future date. Input received: \"+999999999-12-31\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void createUserFailInCauseOfEmailAlreadyExist() {
        User user = User.builder()
                .email("user@usermail.com")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.of(1988, Month.MARCH, 28))
                .build();
        assertFalse(userController.findAll().contains(user), "List of users contains such user, but should not.");
        userController.create(user);
        assertTrue(userController.findAll().contains(user), "List of users don't contains such user. " +
                "User creation failed");
        User anotherUser = User.builder()
                .email("user@usermail.com")
                .login("AnotherUser")
                .name("Username Name")
                .birthday(LocalDate.of(1988, Month.MARCH, 28))
                .build();
        boolean isValidationException = false;
        try {
            userController.create(anotherUser);
        } catch (UserAlreadyExistException exception) {
            assertEquals(exception.getMessage(), "Email \"user@usermail.com\" is already in use.");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void createUserFailInCauseOfLoginAlreadyExist() {
        User user = User.builder()
                .email("user@usermail.com")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.of(1988, Month.MARCH, 28))
                .build();
        assertFalse(userController.findAll().contains(user), "List of users contains such user, but should not.");
        userController.create(user);
        assertTrue(userController.findAll().contains(user), "List of users don't contains such user. " +
                "User creation failed");
        User anotherUser = User.builder()
                .email("anotherUser@usermail.com")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.of(1988, Month.MARCH, 28))
                .build();
        boolean isValidationException = false;
        try {
            userController.create(anotherUser);
        } catch (UserAlreadyExistException exception) {
            assertEquals(exception.getMessage(), "Login \"User\" is already in use.");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void updateUserSuccessfulUpdateOfUser() {
        User initialUser = User.builder()
                .email("user@usermail.com")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.of(1988, Month.MARCH, 28))
                .build();
        assertFalse(userController.findAll().contains(initialUser), "List of users contains such user, but should not.");
        userController.create(initialUser);
        assertTrue(userController.findAll().contains(initialUser), "List of users don't contains such user. " +
                "User creation failed");
        User updatedUser = User.builder()
                .email("updateduser@usermail.com")
                .login("UpdatedUser")
                .name("Updated Username Name")
                .birthday(LocalDate.of(1998, Month.MARCH, 28))
                .id(initialUser.getId())
                .build();
        assertFalse(userController.findAll().contains(updatedUser), "List of users contains such user, but should not.");
        userController.update(updatedUser);
        assertTrue(userController.findAll().contains(updatedUser), "List of users don't contains such user. " +
                "User update failed");
        assertNotEquals(initialUser, updatedUser, "Initial user is equals updated user. User update failed");
    }

    @Test
    void updateUserSuccessfulUpdateUserWithBorderParametersAndNullNameField() {
        User initialUser = User.builder()
                .email("E@")
                .login("L")
                .name(null)
                .birthday(LocalDate.from(LocalDate.now().atStartOfDay()))
                .build();
        assertFalse(userController.findAll().contains(initialUser), "List of users contains such user, but should not.");
        userController.create(initialUser);
        assertTrue(userController.findAll().contains(initialUser), "List of users don't contains such user. " +
                "User creation failed");
        User updatedUser = User.builder()
                .email("F@")
                .login("B")
                .name(null)
                .birthday(LocalDate.from(LocalDate.now().atStartOfDay().minusDays(1)))
                .id(initialUser.getId())
                .build();
        assertFalse(userController.findAll().contains(updatedUser), "List of users contains such user, but should not.");
        userController.update(updatedUser);
        assertTrue(userController.findAll().contains(updatedUser), "List of users don't contains such user. " +
                "User update failed");
        assertNotEquals(initialUser, updatedUser, "Initial user is equals updated user. User update failed");
    }

    @Test
    void updateUserSuccessfulUpdateUserWithBorderParametersAndEmptyNameField() {
        User initialUser = User.builder()
                .email("E@")
                .login("L")
                .name("")
                .birthday(LocalDate.from(LocalDate.now().atStartOfDay()))
                .build();
        assertFalse(userController.findAll().contains(initialUser), "List of users contains such user, but should not.");
        userController.create(initialUser);
        assertTrue(userController.findAll().contains(initialUser), "List of users don't contains such user. " +
                "User creation failed");
        User updatedUser = User.builder()
                .email("F@")
                .login("B")
                .name("")
                .birthday(LocalDate.from(LocalDate.now().atStartOfDay().minusDays(1)))
                .id(initialUser.getId())
                .build();
        assertFalse(userController.findAll().contains(updatedUser), "List of users contains such user, but should not.");
        userController.update(updatedUser);
        assertTrue(userController.findAll().contains(updatedUser), "List of users don't contains such user. " +
                "User update failed");
        assertNotEquals(initialUser, updatedUser, "Initial user is equals updated user. User update failed");
    }

    @Test
    void updateUserSuccessfulUpdateUserWithBorderParametersAndBlankNameField() {
        User initialUser = User.builder()
                .email("E@")
                .login("L")
                .name("    ")
                .birthday(LocalDate.from(LocalDate.now().atStartOfDay()))
                .build();
        assertFalse(userController.findAll().contains(initialUser), "List of users contains such user, but should not.");
        userController.create(initialUser);
        assertTrue(userController.findAll().contains(initialUser), "List of users don't contains such user. " +
                "User creation failed");
        User updatedUser = User.builder()
                .email("F@")
                .login("B")
                .name(" ")
                .birthday(LocalDate.from(LocalDate.now().atStartOfDay().minusDays(1)))
                .id(initialUser.getId())
                .build();
        assertFalse(userController.findAll().contains(updatedUser), "List of users contains such user, but should not.");
        userController.update(updatedUser);
        assertTrue(userController.findAll().contains(updatedUser), "List of users don't contains such user. " +
                "User update failed");
        assertNotEquals(initialUser, updatedUser, "Initial user is equals updated user. User update failed");
    }

    @Test
    void updateUserFailInCauseOfNullEmailField() {
        User initialUser = User.builder()
                .email("user@usermail.com")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.of(1988, Month.MARCH, 28))
                .build();
        assertFalse(userController.findAll().contains(initialUser), "List of users contains such user, but should not.");
        userController.create(initialUser);
        assertTrue(userController.findAll().contains(initialUser), "List of users don't contains such user. " +
                "User creation failed");
        boolean isValidationException = false;
        try {
            User updatedUser = User.builder()
                    .email(null)
                    .login("UpdatedUser")
                    .name("Updated Username Name")
                    .birthday(LocalDate.of(1998, Month.MARCH, 28))
                    .id(initialUser.getId())
                    .build();
            userController.update(updatedUser);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"email\" can't be empty/null and must contains" +
                    " \"@\" symbol. Input received: \"null\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void updateUserFailInCauseOfEmptyEmailField() {
        User initialUser = User.builder()
                .email("user@usermail.com")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.of(1988, Month.MARCH, 28))
                .build();
        assertFalse(userController.findAll().contains(initialUser), "List of users contains such user, but should not.");
        userController.create(initialUser);
        assertTrue(userController.findAll().contains(initialUser), "List of users don't contains such user. " +
                "User creation failed");
        boolean isValidationException = false;
        try {
            User updatedUser = User.builder()
                    .email("")
                    .login("UpdatedUser")
                    .name("Updated Username Name")
                    .birthday(LocalDate.of(1998, Month.MARCH, 28))
                    .id(initialUser.getId())
                    .build();
            userController.update(updatedUser);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"email\" can't be empty/null and must contains" +
                    " \"@\" symbol. Input received: \"\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void updateUserFailInCauseOfBlankEmailField() {
        User initialUser = User.builder()
                .email("user@usermail.com")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.of(1988, Month.MARCH, 28))
                .build();
        assertFalse(userController.findAll().contains(initialUser), "List of users contains such user, but should not.");
        userController.create(initialUser);
        assertTrue(userController.findAll().contains(initialUser), "List of users don't contains such user. " +
                "User creation failed");
        boolean isValidationException = false;
        try {
            User updatedUser = User.builder()
                    .email("    ")
                    .login("UpdatedUser")
                    .name("Updated Username Name")
                    .birthday(LocalDate.of(1998, Month.MARCH, 28))
                    .id(initialUser.getId())
                    .build();
            userController.update(updatedUser);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"email\" can't be empty/null and must contains" +
                    " \"@\" symbol. Input received: \"    \"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void updateUserFailInCauseOfNullLoginField() {
        User initialUser = User.builder()
                .email("user@usermail.com")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.of(1988, Month.MARCH, 28))
                .build();
        assertFalse(userController.findAll().contains(initialUser), "List of users contains such user, but should not.");
        userController.create(initialUser);
        assertTrue(userController.findAll().contains(initialUser), "List of users don't contains such user. " +
                "User creation failed");
        boolean isValidationException = false;
        try {
            User updatedUser = User.builder()
                    .email("user@usermail.com")
                    .login(null)
                    .name("Updated Username Name")
                    .birthday(LocalDate.of(1998, Month.MARCH, 28))
                    .id(initialUser.getId())
                    .build();
            userController.update(updatedUser);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"login\" can't be empty/null or contains space symbol. " +
                    "Input received: \"null\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void updateUserFailInCauseOfEmptyLoginField() {
        User initialUser = User.builder()
                .email("user@usermail.com")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.of(1988, Month.MARCH, 28))
                .build();
        assertFalse(userController.findAll().contains(initialUser), "List of users contains such user, but should not.");
        userController.create(initialUser);
        assertTrue(userController.findAll().contains(initialUser), "List of users don't contains such user. " +
                "User creation failed");
        boolean isValidationException = false;
        try {
            User updatedUser = User.builder()
                    .email("user@usermail.com")
                    .login("")
                    .name("Updated Username Name")
                    .birthday(LocalDate.of(1998, Month.MARCH, 28))
                    .id(initialUser.getId())
                    .build();
            userController.update(updatedUser);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"login\" can't be empty/null or contains space symbol. " +
                    "Input received: \"\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void updateUserFailInCauseOfBlankLoginField() {
        User initialUser = User.builder()
                .email("user@usermail.com")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.of(1988, Month.MARCH, 28))
                .build();
        assertFalse(userController.findAll().contains(initialUser), "List of users contains such user, but should not.");
        userController.create(initialUser);
        assertTrue(userController.findAll().contains(initialUser), "List of users don't contains such user. " +
                "User creation failed");
        boolean isValidationException = false;
        try {
            User updatedUser = User.builder()
                    .email("user@usermail.com")
                    .login("Updated User")
                    .name("Updated Username Name")
                    .birthday(LocalDate.of(1998, Month.MARCH, 28))
                    .id(initialUser.getId())
                    .build();
            userController.update(updatedUser);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"login\" can't be empty/null or contains space symbol. " +
                    "Input received: \"Updated User\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void updateUserFailInCauseOfFutureBirthdayField() {
        User initialUser = User.builder()
                .email("user@usermail.com")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.of(1988, Month.MARCH, 28))
                .build();
        assertFalse(userController.findAll().contains(initialUser), "List of users contains such user, but should not.");
        userController.create(initialUser);
        assertTrue(userController.findAll().contains(initialUser), "List of users don't contains such user. " +
                "User creation failed");
        boolean isValidationException = false;
        try {
            User updatedUser = User.builder()
                    .email("user@usermail.com")
                    .login("Updated")
                    .name("Updated Username Name")
                    .birthday(LocalDate.MAX)
                    .id(initialUser.getId())
                    .build();
            userController.update(updatedUser);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"birthday\" can't contain future date. Input received: \"+999999999-12-31\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void updateUserFailInCauseOfNonexistentUserId() {
        User initialUser = User.builder()
                .email("user@usermail.com")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.of(1988, Month.MARCH, 28))
                .build();
        assertFalse(userController.findAll().contains(initialUser), "List of users contains such user, but should not.");
        userController.create(initialUser);
        assertTrue(userController.findAll().contains(initialUser), "List of users don't contains such user. " +
                "User creation failed");
        boolean isValidationException = false;
        try {
            User updatedUser = User.builder()
                    .email("user@usermail.com")
                    .login("Updated")
                    .name("Updated Username Name")
                    .birthday(LocalDate.of(1998, Month.MARCH, 28))
                    .id(-1L)
                    .build();
            userController.update(updatedUser);
        } catch (UserNotFoundException exception) {
            assertEquals(exception.getMessage(), "User with id \"-1\" not found.");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void findAllSuccessfulCreationOfUserList() {
        final List<User> emptyList = userController.findAll();
        assertNotNull(emptyList, "Method returns null");
        assertTrue(emptyList.isEmpty(), "Method returns not empty list.");
        User user = User.builder()
                .email("user@usermail.com")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.of(1988, Month.MARCH, 28))
                .build();
        userController.create(user);
        final List<User> notEmptyList = userController.findAll();
        assertFalse(notEmptyList.isEmpty(), "Method returns empty list");
        assertTrue(userController.findAll().contains(user), "List of users don't contains such user.");
    }

    @Test
    void getUserByIdSuccessfulReturnUserWithId1() {
        User user = User.builder()
                .email("user@usermail.com")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.of(1988, Month.MARCH, 28))
                .build();
        assertFalse(userController.findAll().contains(user), "List of users contains such user, but should not.");
        userController.create(user);
        assertTrue(userController.findAll().contains(user), "List of users don't contains such user. " +
                "User creation failed");
        User savedUser = userController.findUserById(1L);
        user.setId(savedUser.getId());
        assertEquals(user, savedUser, "Users are not equals.");
    }

    @Test
    void getUserByIdFailReturnUserWithNegativeId() {
        User user = User.builder()
                .email("user@usermail.com")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.of(1988, Month.MARCH, 28))
                .build();
        assertFalse(userController.findAll().contains(user), "List of users contains such user, but should not.");
        userController.create(user);
        assertTrue(userController.findAll().contains(user), "List of users don't contains such user. " +
                "User creation failed");
        boolean isUserNotFoundException = false;
        try {
            User savedUser = userController.findUserById(-1L);
            user.setId(savedUser.getId());
            assertNotEquals(user, savedUser, "Users are equals.");
        } catch (UserNotFoundException exception) {
            assertEquals(exception.getMessage(), "User with id \"-1\" not found.");
            isUserNotFoundException = true;
        }
        assertTrue(isUserNotFoundException, "UserNotFoundException expected, but didn't appear.");
    }

}
