package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    Long id;

    @Email(message = "Invalid email.")
    String email;

    @Pattern(regexp = "[^ ]*", message = "Field \"login\" can't contains space symbols.")
    @NotEmpty(message = "Field \"login\" can't be empty.")
    @NotNull(message = "Field \"login\" can't be null.")
    String login;

    String name;

    @Past(message = "Field \"birthday\" can't contain future date.")
    LocalDate birthday;

    final Set<Long> friendsIds = new HashSet<>();

    public User() {
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("email", email);
        values.put("login", login);
        values.put("name", name);
        values.put("birthday", birthday);

        return values;
    }

}
