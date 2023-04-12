package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.validator.IsAfterCinemaBirthday;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    @Positive
    Long id;

    @NotBlank(message = "Field \"name\" can't consist of space symbols.")
    @NotEmpty(message = "Field \"name\" can't be empty.")
    @NotNull(message = "Field \"name\" can't be null.")
    String name;

    @Size(max = 200, message = "Field \"description\" length can't be more then 200 symbols.")
    String description;

    @IsAfterCinemaBirthday(message = "Field \"releaseDate\" can't contain date before 28th, December, 1895.")
    LocalDate releaseDate;

    @Positive(message = "Value of field \"duration\" can't be negative.")
    int duration;

    final LinkedHashSet<Genre> genres = new LinkedHashSet<>();

    Mpa mpa;

    int rate;

    final Set<Long> likes = new HashSet<>();

}


