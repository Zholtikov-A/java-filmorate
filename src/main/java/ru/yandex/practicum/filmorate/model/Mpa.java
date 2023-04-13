package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Mpa {

    Long id;
    String name;

    public Mpa() {
    }

    public Mpa(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
