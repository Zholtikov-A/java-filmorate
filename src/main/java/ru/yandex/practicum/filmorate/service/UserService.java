package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User addFriend(Long userId, Long friendId) {
        boolean isUserIdCorrect = false;
        boolean isFriendIdCorrect = false;
        for (User user : userStorage.findAll()) {
            if (user.getId() == userId) {
                isUserIdCorrect = true;
            } else if (user.getId() == friendId) {
                isFriendIdCorrect = true;
            }
        }
        if (!isUserIdCorrect) {
            throw new UserNotFoundException("User with ID \"" + userId + "\" not found");
        } else if (!isFriendIdCorrect) {
            throw new UserNotFoundException("User with ID \"" + friendId + "\" not found");
        }
        userStorage.findUserById(userId).getFriends().add(friendId);
        userStorage.findUserById(friendId).getFriends().add(userId);
        return userStorage.findUserById(friendId);
    }

    public User removeFriend(Long userId, Long friendId) {
        boolean isUserIdCorrect = false;
        boolean isFriendIdCorrect = false;
        for (User user : userStorage.findAll()) {
            if (user.getId() == userId) {
                isUserIdCorrect = true;
            }
            if (user.getId() == friendId) {
                isFriendIdCorrect = true;
            }
        }
        if (!isUserIdCorrect) {
            throw new UserNotFoundException("User with ID \"" + userId + "\" not found");
        } else if (!isFriendIdCorrect) {
            throw new UserNotFoundException("User with ID \"" + friendId + "\" not found");
        }
        userStorage.findUserById(userId).getFriends().remove(friendId);
        return userStorage.findUserById(friendId);
    }

    public List<User> findFriends(Long userId) {
        List<User> friends = new ArrayList<>();
        for (Long friendId : userStorage.findUserById(userId).getFriends()) {
            friends.add(userStorage.findUserById(friendId));
        }
        return friends;
    }

    public Set<User> getCommonFriends(Long userId, Long otherId) {

        Set<User> commonFriends = new HashSet<>();
        for (Long friendId : userStorage.findUserById(userId).getFriends()) {
            if (userStorage.findUserById(otherId).getFriends().contains(friendId)) {
                commonFriends.add(userStorage.findUserById(friendId));
            }
        }
        return commonFriends;
    }

}
