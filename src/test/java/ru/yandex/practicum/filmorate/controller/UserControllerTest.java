package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    static UserController controller;


    @BeforeEach
    void init() {
        controller = new UserController();
    }

    @Test
    void createUserSuccessfulCreationOfNewUser() {
        User user = User.builder()
                .email("user@usermail.com")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.of(1988, Month.MARCH, 28))
                .build();
        assertFalse(controller.findAll().contains(user), "List of users contains such user, but should not.");
        controller.create(user);
        assertTrue(controller.findAll().contains(user), "List of users don't contains such user. " +
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
        assertFalse(controller.findAll().contains(user), "List of users contains such user, but should not.");
        controller.create(user);
        assertTrue(controller.findAll().contains(user), "List of users don't contains such user. " +
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
        assertFalse(controller.findAll().contains(user), "List of users contains such user, but should not.");
        controller.create(user);
        assertTrue(controller.findAll().contains(user), "List of users don't contains such user. " +
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
        assertFalse(controller.findAll().contains(user), "List of users contains such user, but should not.");
        controller.create(user);
        assertTrue(controller.findAll().contains(user), "List of users don't contains such user. " +
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
            controller.create(user);
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
            controller.create(user);
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
            controller.create(user);
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
            controller.create(user);
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
            controller.create(user);
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
            controller.create(user);
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
            controller.create(user);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "Field \"birthday\" can't contain future date. Input received: \"+999999999-12-31\"");
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
        assertFalse(controller.findAll().contains(initialUser), "List of users contains such user, but should not.");
        controller.create(initialUser);
        assertTrue(controller.findAll().contains(initialUser), "List of users don't contains such user. " +
                "User creation failed");
        User updatedUser = User.builder()
                .email("updateduser@usermail.com")
                .login("UpdatedUser")
                .name("Updated Username Name")
                .birthday(LocalDate.of(1998, Month.MARCH, 28))
                .id(initialUser.getId())
                .build();
        assertFalse(controller.findAll().contains(updatedUser), "List of users contains such user, but should not.");
        controller.update(updatedUser);
        assertTrue(controller.findAll().contains(updatedUser), "List of users don't contains such user. " +
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
        assertFalse(controller.findAll().contains(initialUser), "List of users contains such user, but should not.");
        controller.create(initialUser);
        assertTrue(controller.findAll().contains(initialUser), "List of users don't contains such user. " +
                "User creation failed");
        User updatedUser = User.builder()
                .email("F@")
                .login("B")
                .name(null)
                .birthday(LocalDate.from(LocalDate.now().atStartOfDay().minusDays(1)))
                .id(initialUser.getId())
                .build();
        assertFalse(controller.findAll().contains(updatedUser), "List of users contains such user, but should not.");
        controller.update(updatedUser);
        assertTrue(controller.findAll().contains(updatedUser), "List of users don't contains such user. " +
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
        assertFalse(controller.findAll().contains(initialUser), "List of users contains such user, but should not.");
        controller.create(initialUser);
        assertTrue(controller.findAll().contains(initialUser), "List of users don't contains such user. " +
                "User creation failed");
        User updatedUser = User.builder()
                .email("F@")
                .login("B")
                .name("")
                .birthday(LocalDate.from(LocalDate.now().atStartOfDay().minusDays(1)))
                .id(initialUser.getId())
                .build();
        assertFalse(controller.findAll().contains(updatedUser), "List of users contains such user, but should not.");
        controller.update(updatedUser);
        assertTrue(controller.findAll().contains(updatedUser), "List of users don't contains such user. " +
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
        assertFalse(controller.findAll().contains(initialUser), "List of users contains such user, but should not.");
        controller.create(initialUser);
        assertTrue(controller.findAll().contains(initialUser), "List of users don't contains such user. " +
                "User creation failed");
        User updatedUser = User.builder()
                .email("F@")
                .login("B")
                .name(" ")
                .birthday(LocalDate.from(LocalDate.now().atStartOfDay().minusDays(1)))
                .id(initialUser.getId())
                .build();
        assertFalse(controller.findAll().contains(updatedUser), "List of users contains such user, but should not.");
        controller.update(updatedUser);
        assertTrue(controller.findAll().contains(updatedUser), "List of users don't contains such user. " +
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
        assertFalse(controller.findAll().contains(initialUser), "List of users contains such user, but should not.");
        controller.create(initialUser);
        assertTrue(controller.findAll().contains(initialUser), "List of users don't contains such user. " +
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
            controller.update(updatedUser);
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
        assertFalse(controller.findAll().contains(initialUser), "List of users contains such user, but should not.");
        controller.create(initialUser);
        assertTrue(controller.findAll().contains(initialUser), "List of users don't contains such user. " +
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
            controller.update(updatedUser);
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
        assertFalse(controller.findAll().contains(initialUser), "List of users contains such user, but should not.");
        controller.create(initialUser);
        assertTrue(controller.findAll().contains(initialUser), "List of users don't contains such user. " +
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
            controller.update(updatedUser);
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
        assertFalse(controller.findAll().contains(initialUser), "List of users contains such user, but should not.");
        controller.create(initialUser);
        assertTrue(controller.findAll().contains(initialUser), "List of users don't contains such user. " +
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
            controller.update(updatedUser);
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
        assertFalse(controller.findAll().contains(initialUser), "List of users contains such user, but should not.");
        controller.create(initialUser);
        assertTrue(controller.findAll().contains(initialUser), "List of users don't contains such user. " +
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
            controller.update(updatedUser);
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
        assertFalse(controller.findAll().contains(initialUser), "List of users contains such user, but should not.");
        controller.create(initialUser);
        assertTrue(controller.findAll().contains(initialUser), "List of users don't contains such user. " +
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
            controller.update(updatedUser);
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
        assertFalse(controller.findAll().contains(initialUser), "List of users contains such user, but should not.");
        controller.create(initialUser);
        assertTrue(controller.findAll().contains(initialUser), "List of users don't contains such user. " +
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
            controller.update(updatedUser);
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
        assertFalse(controller.findAll().contains(initialUser), "List of users contains such user, but should not.");
        controller.create(initialUser);
        assertTrue(controller.findAll().contains(initialUser), "List of users don't contains such user. " +
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
            controller.update(updatedUser);
        } catch (ValidationException exception) {
            assertEquals(exception.getMessage(), "User with such ID not found. Input received: \"-1\"");
            isValidationException = true;
        }
        assertTrue(isValidationException, "ValidationException expected, but didn't appear.");
    }

    @Test
    void findAllSuccessfulCreationOfUserList() {
        final List<User> emptyList = controller.findAll();
        assertNotNull(emptyList, "Method returns null");
        assertTrue(emptyList.isEmpty(), "Method returns not empty list.");
        User user = User.builder()
                .email("user@usermail.com")
                .login("User")
                .name("Username Name")
                .birthday(LocalDate.of(1988, Month.MARCH, 28))
                .build();
        controller.create(user);
        final List<User> notEmptyList = controller.findAll();
        assertFalse(notEmptyList.isEmpty(), "Method returns empty list");
        assertTrue(controller.findAll().contains(user), "List of users don't contains such user.");
    }
}