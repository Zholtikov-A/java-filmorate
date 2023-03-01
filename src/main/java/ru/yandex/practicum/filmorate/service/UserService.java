package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {

    final Map<Long, User> users = new HashMap<>();

    Long userId = 0L;

    Long generateUserId() {
        return ++userId;
    }

    @PostMapping
    public User create(User user) {
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
            user.setId(generateUserId());
            users.put(user.getId(), user);
            log.info("New user created: " + user);
            return user;
        }
    }

    @PutMapping
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            String message = "User with such ID not found. Input received: \"" + user.getId() + "\"";
            log.info("Validation failed! " + message);
            throw new ValidationException(message);
        } else if (user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
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
            users.replace(user.getId(), user);
            log.info("User with ID=" + user.getId() + " updated:  " + user);
            return user;
        }
    }

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

}
