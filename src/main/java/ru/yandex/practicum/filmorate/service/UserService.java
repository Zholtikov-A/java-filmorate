package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User findUserById(Long userId) {
        return userStorage.findUserById(userId);
    }

    public User addFriend(Long userId, Long friendId) {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        return user;
    }

    public User removeFriend(Long userId, Long friendId) {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        return user;
    }

    public List<User> findFriends(Long userId) {
        userStorage.findUserById(userId);        //throws UserNotFoundException
        return userStorage.findAll().stream()
                .filter(user -> user.getFriends().contains(userId))
                .collect(Collectors.toList());
    }


    public List<User> getCommonFriends(Long userId, Long otherId) {
        User user = userStorage.findUserById(userId);
        User otherUser = userStorage.findUserById(otherId);
        Set<Long> userFriends = user.getFriends();
        Set<Long> otherUserFriends = otherUser.getFriends();
        Set<Long> intersectSet = new HashSet<>(userFriends);
        intersectSet.retainAll(otherUserFriends);
        List<User> commonFriends = new ArrayList<>();
        for (Long id : intersectSet) {
            commonFriends.add(userStorage.findUserById(id));
        }
        return commonFriends;
    }

}
