package ru.yandex.practicum.filmorate.storage.user;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InMemoryUserStorage implements UserStorage {

    final Map<Long, User> users = new HashMap<>();

    Long userId = 0L;

    Long generateUserId() {
        return ++userId;
    }

    @Override
    public User create(User user) {
        validateFields(user);
        user.setId(generateUserId());
        users.put(user.getId(), user);
        log.info("New user created: " + user);
        return user;
    }

    @Override
    public User update(User user) {
        findUserById(user.getId());    //throws UserNotFoundException
        validateFields(user);
        users.replace(user.getId(), user);
        log.info("User with ID=" + user.getId() + " updated:  " + user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findUserById(Long userId) {
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException("User with id \"" + userId + "\" not found.");
        }
        return users.get(userId);
    }

    private void validateFields(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            String message = "Field \"email\" can't be empty/null and must contains \"@\" symbol. Input received: "
                    + "\"" + user.getEmail() + "\"";
            log.info("Validation failed! " + message);
            throw new ValidationException(message);
        } else if
        (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            String message = "Field \"login\" can't be empty/null or contains space symbol. Input received: \"" + user.getLogin() + "\"";
            log.info("Validation failed! " + message);
            throw new ValidationException(message);
        } else if (user.getBirthday().isAfter(ChronoLocalDate.from(LocalDate.now().atStartOfDay()))) {
            String message = "Field \"birthday\" can't contain future date. Input received: \"" + user.getBirthday() + "\"";
            log.info("Validation failed! " + message);
            throw new ValidationException(message);
        } else {
            if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
                String invalidUserName = user.getName();
                user.setName(user.getLogin());
                log.info("Value \"" + invalidUserName + "\" of field \"name\" is invalid, value of field \"login\" " +
                        "was set up as value of field \"name\". Current value of field \"name\" is " + user.getName());
            }
            for (User existUser : users.values()) {
                if (user.getEmail().equals(existUser.getEmail())) {
                    String message = "Email \"" + user.getEmail() + "\" is already in use.";
                    throw new UserAlreadyExistException(message);
                } else if (user.getLogin().equals(existUser.getLogin())) {
                    String message = "Login \"" + user.getLogin() + "\" is already in use.";
                    throw new UserAlreadyExistException(message);
                }
            }
        }
    }

}