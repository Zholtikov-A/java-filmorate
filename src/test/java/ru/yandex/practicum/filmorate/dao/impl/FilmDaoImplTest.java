package ru.yandex.practicum.filmorate.dao.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDaoImplTest {

    private final FilmDaoImpl filmDao;
    private final MockMvc mockMvc;
    static final ObjectMapper objectMapper =
            new ObjectMapper().disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
    static User userOne;
    static Film terminatorOne;

    public static String asJsonString(final Object obj) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void initFilmTerminatorOne() {
        terminatorOne = Film.builder()
                .name("Terminator")
                .description("No pity. No pain. No fear.")
                .releaseDate(LocalDate.of(1984, Month.NOVEMBER, 26))
                .duration(152)
                .rate(0)
                .mpa(new Mpa(4L, "R"))
                .build();
    }

    public void initMockTerminatorOneOk() throws Exception {
        mockMvc
                .perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(terminatorOne)))
                .andExpect(status()
                        .isOk());
    }

    public void initCreateUserUserOne() {
        userOne = User.builder()
                .name("First")
                .login("userOne")
                .email("first@usermail.ru")
                .birthday(LocalDate.of(1963, Month.MAY, 27))
                .build();
    }

    public void initMockPerformUsersUserOneOk() throws Exception {
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userOne)))
                .andExpect(status()
                        .isOk());
    }


    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void filmCreateSuccess() throws Exception {
        initFilmTerminatorOne();
        mockMvc
                .perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(terminatorOne)))
                .andExpect(status()
                        .isOk());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void filmFindByIdSuccess() throws Exception {
        initFilmTerminatorOne();
        initMockTerminatorOneOk();
        terminatorOne.setId(1L);

        Film testFilm = filmDao.findFilmById(1L);
        assertEquals(terminatorOne, testFilm);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void failFindFilmByWrongId() throws Exception {
        mockMvc
                .perform(get("/films/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(null)))
                .andExpect(status()
                        .isNotFound());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void findAllFilmsEmptyListSuccess() {
        assertEquals(filmDao.findAll(), new ArrayList<>());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void findAllFilmsWithOneFilmOnListSuccess() throws Exception {
        initFilmTerminatorOne();
        initMockTerminatorOneOk();
        terminatorOne.setId(1L);

        ArrayList<Film> checkList = new ArrayList<>();
        checkList.add(terminatorOne);
        assertEquals(checkList, filmDao.findAll());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void failCreateFilmEmptyName() throws Exception {
        initFilmTerminatorOne();
        terminatorOne.setName("");

        mockMvc
                .perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(terminatorOne)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void failCreateFilmDescriptionTooLong() throws Exception {
        initFilmTerminatorOne();
        terminatorOne.setDescription("Too long description. Too long description. Too long description. Too long description. " +
                "Too long description. Too long description. Too long description. Too long description. Too long description. " +
                "Too long description. Too long description. Too long description. Too long description. Too long description. ");
        mockMvc
                .perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(terminatorOne)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void failCreateFilmReleaseDateIsTooEarly() throws Exception {
        initFilmTerminatorOne();
        terminatorOne.setReleaseDate(LocalDate.of(1000, 1, 1));

        mockMvc
                .perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(terminatorOne)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void failCreateFilmNegativeDuration() throws Exception {
        initFilmTerminatorOne();
        terminatorOne.setDuration(-152);

        mockMvc
                .perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(terminatorOne)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void filmUpdateSuccess() throws Exception {
        initFilmTerminatorOne();
        initMockTerminatorOneOk();
        terminatorOne.setId(1L);
        assertEquals(terminatorOne, filmDao.findFilmById(1L));

        Film updateTerminator = Film.builder()
                .id(1L)
                .name("Terminator Updated")
                .description("No pity. No pain. No fear. Updated")
                .releaseDate(LocalDate.of(1984, Month.NOVEMBER, 26))
                .duration(152)
                .mpa(new Mpa(4L, "R"))
                .build();

        mockMvc
                .perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateTerminator)))
                .andExpect(status()
                        .isOk());
        assertEquals(updateTerminator, filmDao.findFilmById(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void filmUpdateFailNegativeId() throws Exception {
        initFilmTerminatorOne();
        initMockTerminatorOneOk();
        terminatorOne.setId(1L);
        assertEquals(terminatorOne, filmDao.findFilmById(1L));

        Film updateTerminator = Film.builder()
                .id(-1L)
                .name("Terminator Updated")
                .description("No pity. No pain. No fear. Updated")
                .releaseDate(LocalDate.of(1984, Month.NOVEMBER, 26))
                .duration(152)
                .rate(1)
                .mpa(new Mpa(4L, "R"))
                .build();

        mockMvc
                .perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateTerminator)))
                .andExpect(status()
                        .isBadRequest());
        assertNotEquals(updateTerminator, filmDao.findFilmById(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void filmUpdateFailNameIsBlank() throws Exception {
        initFilmTerminatorOne();
        initMockTerminatorOneOk();
        terminatorOne.setId(1L);
        assertEquals(terminatorOne, filmDao.findFilmById(1L));

        Film updateTerminator = Film.builder()
                .id(1L)
                .name("    ")
                .description("No pity. No pain. No fear. Updated")
                .releaseDate(LocalDate.of(1984, Month.NOVEMBER, 26))
                .duration(152)
                .rate(1)
                .mpa(new Mpa(4L, "R"))
                .build();

        mockMvc
                .perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateTerminator)))
                .andExpect(status()
                        .isBadRequest());
        assertNotEquals(updateTerminator, filmDao.findFilmById(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void filmUpdateFailNameIsEmpty() throws Exception {
        initFilmTerminatorOne();
        initMockTerminatorOneOk();
        terminatorOne.setId(1L);
        assertEquals(terminatorOne, filmDao.findFilmById(1L));

        Film updateTerminator = Film.builder()
                .id(1L)
                .name("")
                .description("No pity. No pain. No fear. Updated")
                .releaseDate(LocalDate.of(1984, Month.NOVEMBER, 26))
                .duration(152)
                .rate(1)
                .mpa(new Mpa(4L, "R"))
                .build();

        mockMvc
                .perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateTerminator)))
                .andExpect(status()
                        .isBadRequest());
        assertNotEquals(updateTerminator, filmDao.findFilmById(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void filmUpdateFailNameIsNull() throws Exception {
        initFilmTerminatorOne();
        initMockTerminatorOneOk();
        terminatorOne.setId(1L);
        assertEquals(terminatorOne, filmDao.findFilmById(1L));

        Film updateTerminator = Film.builder()
                .id(1L)
                .name(null)
                .description("No pity. No pain. No fear. Updated")
                .releaseDate(LocalDate.of(1984, Month.NOVEMBER, 26))
                .duration(152)
                .rate(1)
                .mpa(new Mpa(4L, "R"))
                .build();

        mockMvc
                .perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateTerminator)))
                .andExpect(status()
                        .isBadRequest());
        assertNotEquals(updateTerminator, filmDao.findFilmById(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void filmUpdateFailDescriptionTooLong() throws Exception {
        initFilmTerminatorOne();
        initMockTerminatorOneOk();
        terminatorOne.setId(1L);
        assertEquals(terminatorOne, filmDao.findFilmById(1L));


        Film updateTerminator = Film.builder()
                .id(1L)
                .name("Terminator. Updated")
                .description("Too long description. Too long description. Too long description. Too long description. " +
                        "Too long description. Too long description. Too long description. Too long description. Too long description. " +
                        "Too long description. Too long description. Too long description. Too long description. Too long description. ")
                .releaseDate(LocalDate.of(1984, Month.NOVEMBER, 26))
                .duration(152)
                .rate(1)
                .mpa(new Mpa(4L, "R"))
                .build();

        mockMvc
                .perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateTerminator)))
                .andExpect(status()
                        .isBadRequest());
        assertNotEquals(updateTerminator, filmDao.findFilmById(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void filmUpdateFailToEarlyDate() throws Exception {
        initFilmTerminatorOne();
        initMockTerminatorOneOk();
        terminatorOne.setId(1L);
        assertEquals(terminatorOne, filmDao.findFilmById(1L));

        Film updateTerminator = Film.builder()
                .id(1L)
                .name("Arrival of a Terminator")
                .description("Machine was sent to the past to prevent arrival of a train")
                .releaseDate(LocalDate.of(1895, Month.DECEMBER, 27))
                .duration(152)
                .rate(1)
                .mpa(new Mpa(4L, "R"))
                .build();

        mockMvc
                .perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateTerminator)))
                .andExpect(status()
                        .isBadRequest());
        assertNotEquals(updateTerminator, filmDao.findFilmById(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void filmUpdateFailNegativeDuration() throws Exception {
        initFilmTerminatorOne();
        initMockTerminatorOneOk();
        terminatorOne.setId(1L);
        assertEquals(terminatorOne, filmDao.findFilmById(1L));

        Film updateTerminator = Film.builder()
                .id(1L)
                .name("Terminator Updated")
                .description("No pity. No pain. No fear. Updated")
                .releaseDate(LocalDate.of(1984, Month.NOVEMBER, 26))
                .duration(-1)
                .rate(1)
                .mpa(new Mpa(4L, "R"))
                .build();

        mockMvc
                .perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateTerminator)))
                .andExpect(status()
                        .isBadRequest());
        assertNotEquals(updateTerminator, filmDao.findFilmById(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void addLikeSuccess() throws Exception {
        initFilmTerminatorOne();
        initMockTerminatorOneOk();
        initCreateUserUserOne();
        initMockPerformUsersUserOneOk();
        terminatorOne.setId(1L);
        assertEquals(terminatorOne, filmDao.findFilmById(1L));

        final Film initialFilm = filmDao.findFilmById(1L);

        final Film likedFilm = filmDao.findFilmById(1L);
        likedFilm.setRate(1);

        filmDao.addLike(1L, 1L);

        assertNotEquals(likedFilm, initialFilm);
        assertEquals(likedFilm, filmDao.findFilmById(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void addLikeFailFilmIdIsNegative() throws Exception {
        initFilmTerminatorOne();
        initMockTerminatorOneOk();
        initCreateUserUserOne();
        initMockPerformUsersUserOneOk();
        terminatorOne.setId(1L);
        assertEquals(terminatorOne, filmDao.findFilmById(1L));

        final Film likedFilm = filmDao.findFilmById(1L);
        likedFilm.setRate(1);

        try {
            filmDao.addLike(-1L, 1L);
        } catch (FilmNotFoundException exception) {
            assertEquals(exception.getMessage(), "Film with id \"-1\" not found.");
        }
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void addLikeFailUserIdIsNegative() throws Exception {
        initFilmTerminatorOne();
        initMockTerminatorOneOk();
        initCreateUserUserOne();
        initMockPerformUsersUserOneOk();
        terminatorOne.setId(1L);
        assertEquals(terminatorOne, filmDao.findFilmById(1L));

        final Film likedFilm = filmDao.findFilmById(1L);
        likedFilm.setRate(1);

        try {
            filmDao.addLike(1L, -1L);
        } catch (UserNotFoundException exception) {
            assertEquals(exception.getMessage(), "User with id \"-1\" not found.");
        }
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void removeLikeSuccess() throws Exception {
        initFilmTerminatorOne();
        initMockTerminatorOneOk();
        initCreateUserUserOne();
        initMockPerformUsersUserOneOk();
        terminatorOne.setId(1L);
        assertEquals(terminatorOne, filmDao.findFilmById(1L));

        final Film initialFilm = filmDao.findFilmById(1L);

        final Film likedFilm = filmDao.findFilmById(1L);
        likedFilm.setRate(1);
        filmDao.addLike(1L, 1L);

        assertNotEquals(likedFilm, initialFilm);
        assertEquals(likedFilm, filmDao.findFilmById(1L));

        final Film filmWithLike = filmDao.findFilmById(1L);
        filmDao.removeLike(1L, 1L);
        final Film filmWithOutLike = filmDao.findFilmById(1L);

        assertNotEquals(filmWithLike, filmWithOutLike);
        assertEquals(initialFilm, filmDao.findFilmById(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void removeLikeFailNegativeFilmId() throws Exception {
        initFilmTerminatorOne();
        initMockTerminatorOneOk();
        initCreateUserUserOne();
        initMockPerformUsersUserOneOk();
        terminatorOne.setId(1L);
        assertEquals(terminatorOne, filmDao.findFilmById(1L));

        final Film initialFilm = filmDao.findFilmById(1L);

        final Film likedFilm = filmDao.findFilmById(1L);
        likedFilm.setRate(1);
        filmDao.addLike(1L, 1L);

        assertNotEquals(likedFilm, initialFilm);
        assertEquals(likedFilm, filmDao.findFilmById(1L));

        try {
            filmDao.removeLike(-1L, 1L);
        } catch (FilmNotFoundException exception) {
            assertEquals(exception.getMessage(), "Film with id \"-1\" not found.");
        }
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void removeLikeFailNegativeUserId() throws Exception {
        initFilmTerminatorOne();
        initMockTerminatorOneOk();
        initCreateUserUserOne();
        initMockPerformUsersUserOneOk();
        terminatorOne.setId(1L);
        assertEquals(terminatorOne, filmDao.findFilmById(1L));

        final Film initialFilm = filmDao.findFilmById(1L);

        final Film likedFilm = filmDao.findFilmById(1L);
        likedFilm.setRate(1);
        filmDao.addLike(1L, 1L);

        assertNotEquals(likedFilm, initialFilm);
        assertEquals(likedFilm, filmDao.findFilmById(1L));

        try {
            filmDao.removeLike(1L, -1L);
        } catch (UserNotFoundException exception) {
            assertEquals(exception.getMessage(), "User with id \"-1\" not found.");
        }
    }
}