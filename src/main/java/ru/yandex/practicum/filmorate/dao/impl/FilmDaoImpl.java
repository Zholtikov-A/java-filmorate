package ru.yandex.practicum.filmorate.dao.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.CustomValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FilmDaoImpl implements FilmDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserDaoImpl userDao;

    @Override
    public Film create(Film film) throws CustomValidationException {
        final String sqlFilm = "insert into filmorate.films(name, description, release_date, duration, mpa_rating_id, rate) " +
                "values(?,?,?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlFilm, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            stmt.setInt(6, film.getRate());
            return stmt;
        }, keyHolder);

        Long key = Objects.requireNonNull(keyHolder.getKey()).longValue();

        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                final String sqlGenres = "insert into filmorate.films_genre_link(film_id, genre_id) " +
                        "values(?,?);";
                jdbcTemplate.update(connection -> {
                    PreparedStatement stmt = connection.prepareStatement((sqlGenres));
                    stmt.setInt(1, key.intValue());
                    stmt.setInt(2, genre.getId().intValue());
                    return stmt;
                });
            }
        }
        return findFilmById(key);
    }

    @Override
    public Film update(Film film) {
        String sqlFilmUpdate = "update filmorate.films set name = ?, " +
                "description = ?, release_date = ?, duration = ?, " +
                "mpa_rating_id = ?, rate = ?" +
                " where film_id = ?";

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlFilmUpdate);
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            stmt.setInt(6, film.getRate());
            stmt.setLong(7, film.getId());
            return stmt;
        });

        // delete genres
        final String sqlGenresDelete = "delete from filmorate.films_genre_link " +
                "where film_id = " + film.getId();
        jdbcTemplate.update(sqlGenresDelete);

        // record genre list
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                final String sqlGenres = "insert into filmorate.films_genre_link(film_id, genre_id) " +
                        "values(?,?);";
                jdbcTemplate.update(connection -> {
                    PreparedStatement stmt = connection.prepareStatement((sqlGenres));
                    stmt.setInt(1, film.getId().intValue());
                    stmt.setInt(2, genre.getId().intValue());
                    return stmt;
                });
            }
        }
        return findFilmById(film.getId());
    }

    @Override
    public List<Film> findAll() {

        final String sql = "select f.film_id, f.name as film_name, f.description, f.release_date, f.duration, " +
                "m.mpa_rating_id, m.name as mpa_name, json_arrayagg(json_object(" +
                "  KEY 'id' VALUE g.genre_id," +
                "  KEY 'name' VALUE g.name" +
                ")) as genres, " +
                " COUNT(lk.user_id) as rate " +
                "from filmorate.films as f " +
                "left join filmorate.mpa_rating as m on f.mpa_rating_id = m.mpa_rating_id " +
                "left join filmorate.films_genre_link as fgl on f.film_id = fgl.film_id " +
                "left join filmorate.genre as g on fgl.genre_id = g.genre_id " +
                "left join filmorate.likes_films_users_link as lk on lk.film_id = f.film_id " +
                "group by f.film_id ";

        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public Film findFilmById(Long id) {

        final String sql = "select f.film_id, f.name as film_name, f.description, f.release_date, f.duration, " +
                "m.mpa_rating_id, m.name as mpa_name, json_arrayagg(json_object(" +
                "  KEY 'id' VALUE g.genre_id," +
                "  KEY 'name' VALUE g.name" +
                ")) as genres, " +
                // mine 1 line below
                " COUNT(lk.user_id) as rate " +
                "from filmorate.films as f " +
                "left join filmorate.mpa_rating as m on f.mpa_rating_id = m.mpa_rating_id " +
                "left join filmorate.films_genre_link as fgl on f.film_id = fgl.film_id " +
                "left join filmorate.genre as g on fgl.genre_id = g.genre_id " +
                // mine 1 line below
                "left join filmorate.likes_films_users_link as lk on lk.film_id = f.film_id " +
                "where f.film_id = ? " +
                "group by f.film_id ";

        final List<Film> films = jdbcTemplate.query(sql, this::mapRowToFilm, id);


        if (films.isEmpty()) {
            throw new FilmNotFoundException("Film with id \"" + id + "\" not found.");
        } else {
            return films.get(0);
        }
    }


    @Override
    public Film addLike(Long filmId, Long userId) {
        checkFilmExistence(filmId);
        userDao.checkUserExistence(userId);
        String sql = "insert into filmorate.likes_films_users_link(film_id, user_id) " +
                "values(?,?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, filmId.intValue());
            stmt.setInt(2, userId.intValue());
            return stmt;
        });
        return findFilmById(filmId);
    }

    public Film removeLike(Long filmId, Long userId) {
        checkFilmExistence(filmId);
        userDao.checkUserExistence(userId);
        final String sql = "delete from filmorate.likes_films_users_link " +
                "where film_id = ? and user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
        return findFilmById(filmId);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("film_name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(new Mpa(resultSet.getLong("mpa_rating_id"),
                        resultSet.getString("mpa_name")))
                .rate(resultSet.getInt("rate"))
                .build();

        String genresString = resultSet.getString("genres");

        final ObjectMapper objectMapper = new ObjectMapper();
        Genre[] genres = new Genre[10];
        try {
            genres = objectMapper.readValue(genresString, Genre[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Genre genre : genres) {
            if (genre != null) {
                if (genre.getId() != null)
                    film.getGenres().add(genre);
            }
        }
        return film;
    }

    @Override
    public void checkFilmExistence(Long id) {
        final String sql = "select f.film_id, " +
                "from filmorate.films as f " +
                "where f.film_id = ? ";
        final List<Film> films = jdbcTemplate.query(sql, this::mapFilmId, id);
        if (films.isEmpty()) {
            throw new FilmNotFoundException("Film with id \"" + id + "\" not found.");
        }
    }

    private Film mapFilmId(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("film_id"))
                .build();
    }

}
