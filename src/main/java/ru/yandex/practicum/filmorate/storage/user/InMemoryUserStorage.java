package ru.yandex.practicum.filmorate.storage.user;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

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
        user.setId(generateUserId());
        users.put(user.getId(), user);
        log.info("New user created: " + user);
        return user;
    }

    @Override
    public User update(User user) {
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


}