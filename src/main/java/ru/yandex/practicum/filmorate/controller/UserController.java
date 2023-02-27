package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap();
    protected Long userId = 0L;

    public Long generateUserId() {
        return ++userId;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.info("Валидация не пройдена.");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ \"@\". " +
                    "Введено: " + "\"" + user.getEmail() + "\"");
        } else if
        (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.info("Валидация не пройдена.");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы. Введено: \"" + user.getLogin() + "\"");
        } else if (user.getBirthday().isAfter(ChronoLocalDate.from(LocalDate.now().atStartOfDay()))) {
            log.info("Валидация не пройдена.");
            throw new ValidationException("Дата рождения не может быть в будущем. Введенная дата рождения: " + user.getBirthday().toString());
        } else {
            if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            user.setId(generateUserId());
            users.put(user.getId(), user);
            log.info(user.toString());
            return user;
        }
    }

    @PutMapping
    public User update(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.info("Валидация не пройдена.");
            throw new ValidationException("Введен несуществующий Id. Введенный Id: " + user.getId());
        } else if (!user.getEmail().contains("@")) {
            log.info("Валидация не пройдена.");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ \"@\".");
        } else if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.info("Валидация не пройдена.");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        } else if (user.getBirthday().isAfter(ChronoLocalDate.from(LocalDate.now().atStartOfDay()))) {
            log.info("Валидация не пройдена.");
            throw new ValidationException("Дата рождения не может быть в будущем. Введенная дата рождения - " + user.getBirthday().toString());
        } else {
            if (user.getName().isEmpty() || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.replace(user.getId(), user);
            log.info(user.toString());
            return user;
        }
    }


    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }


}
