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
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDaoImplTest {
    private final UserDaoImpl userDao;
    private final MockMvc mockMvc;
    static final ObjectMapper objectMapper =
            new ObjectMapper().disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
    static User userOne;
    static User userTwo;
    static User userThree;

    public static String asJsonString(final Object obj) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void initCreateUserUserOne() {
        userOne = User.builder()
                .name("First")
                .login("userOne")
                .email("first@usermail.ru")
                .birthday(LocalDate.of(1963, Month.MAY, 27))
                .build();
    }

    public void initCreateUserUserTwo() {
        userTwo = User.builder()
                .name("Second")
                .login("userTwo")
                .email("second@usermail.ru")
                .birthday(LocalDate.of(1988, Month.MARCH, 28))
                .build();
    }

    public void initCreateUserUserThree() {
        userThree = User.builder()
                .name("Third")
                .login("userThree")
                .email("Third@usermail.ru")
                .birthday(LocalDate.of(1963, Month.OCTOBER, 20))
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

    public void initMockPerformUsersUserTwoOk() throws Exception {
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userTwo)))
                .andExpect(status()
                        .isOk());
    }

    public void initMockPerformUsersUserThreeOk() throws Exception {
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userThree)))
                .andExpect(status()
                        .isOk());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void createUserSuccess() throws Exception {
        initCreateUserUserOne();
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userOne)))
                .andExpect(status()
                        .isOk());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void failCreateWrongObject() throws Exception {
        Film terminatorOne = Film.builder()
                .name("Terminator")
                .description("No pity. No pain. No fear.")
                .releaseDate(LocalDate.of(1984, Month.NOVEMBER, 26))
                .duration(152)
                .rate(0)
                .mpa(new Mpa(4L, "R"))
                .build();
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(terminatorOne)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void findUserByIdSuccess() throws Exception {
        initCreateUserUserOne();
        initMockPerformUsersUserOneOk();
        userOne.setId(1L);
        initCreateUserUserTwo();
        initMockPerformUsersUserTwoOk();

        User testUser = userDao.findUserById(1L);

        assertEquals(userOne, testUser);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void failFindUserByIdNotFound() throws Exception {
        mockMvc
                .perform(get("/users/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(null)))
                .andExpect(status()
                        .isNotFound());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void spaceSymbolsInLoginFailCreate() throws Exception {
        initCreateUserUserOne();
        userOne.setLogin("S a l a i a");
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userOne)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void wrongEmailFailCreate() throws Exception {
        initCreateUserUserOne();
        userOne.setEmail("me.mail");
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userOne)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void blankEmailFailCreate() throws Exception {
        initCreateUserUserOne();
        userOne.setEmail("  ");
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userOne)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void birthdayInFutureFailCreate() throws Exception {
        initCreateUserUserOne();
        userOne.setBirthday(LocalDate.of(3333, 12, 6));
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userOne)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void updateUserSuccess() throws Exception {
        initCreateUserUserOne();
        initMockPerformUsersUserOneOk();

        userOne.setId(1L);
        userOne.setName("First Updated");
        userOne.setLogin("userOneUpdated");
        userOne.setEmail("updatedOne@usermail.ru");
        userOne.setBirthday(LocalDate.of(2023, 1, 1));

        mockMvc
                .perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userOne)))
                .andExpect(status()
                        .isOk());

        User testUser = userDao.findUserById(1L);
        assertEquals(userOne, testUser);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void failUpdateUser() throws Exception {
        initCreateUserUserOne();
        initMockPerformUsersUserOneOk();

        userOne.setId(10L);
        mockMvc
                .perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userOne)))
                .andExpect(status()
                        .isNotFound());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void findAllUsersEmptyListSuccess() {
        assertEquals(new ArrayList<>(), userDao.findAll());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void findAllUsersWithOneUserSuccess() throws Exception {
        initCreateUserUserOne();
        initMockPerformUsersUserOneOk();
        userOne.setId(1L);
        List<User> checkList = new ArrayList<>();
        checkList.add(userOne);
        assertEquals(checkList, userDao.findAll());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void addFriendSuccess() throws Exception {
        initCreateUserUserOne();
        initCreateUserUserTwo();
        initMockPerformUsersUserOneOk();
        initMockPerformUsersUserTwoOk();
        userOne.setId(1L);
        userTwo.setId(2L);

        mockMvc
                .perform(put("/users/1/friends/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userOne)))
                .andExpect(status()
                        .isOk());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void failAddFriendNotFound() throws Exception {
        initCreateUserUserOne();
        initMockPerformUsersUserOneOk();
        userOne.setId(1L);

        mockMvc
                .perform(put("/users/1/friends/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userOne)))
                .andExpect(status()
                        .isNotFound());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void findFriendsEmptyListSuccess() throws Exception {
        initCreateUserUserOne();
        initMockPerformUsersUserOneOk();
        userOne.setId(1L);
        assertEquals(new ArrayList<>(), userDao.findFriends(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void findFriendsFilledListSuccess() throws Exception {
        initCreateUserUserOne();
        initCreateUserUserTwo();
        initCreateUserUserThree();
        initMockPerformUsersUserOneOk();
        initMockPerformUsersUserTwoOk();
        initMockPerformUsersUserThreeOk();
        userOne.setId(1L);
        userTwo.setId(2L);
        userThree.setId(3L);

        userDao.addFriend(1L, 2L);
        userDao.addFriend(1L, 3L);
        List<User> checkList = new ArrayList<>();
        checkList.add(userTwo);
        checkList.add(userThree);
        assertEquals(checkList, userDao.findFriends(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void removeFriendSuccess() throws Exception {
        initCreateUserUserOne();
        initCreateUserUserTwo();
        initCreateUserUserThree();
        initMockPerformUsersUserOneOk();
        initMockPerformUsersUserTwoOk();
        initMockPerformUsersUserThreeOk();
        userOne.setId(1L);
        userTwo.setId(2L);
        userThree.setId(3L);

        userDao.addFriend(1L, 2L);
        userDao.addFriend(1L, 3L);
        List<User> checkList = new ArrayList<>();
        checkList.add(userTwo);
        checkList.add(userThree);
        assertEquals(checkList, userDao.findFriends(1L));

        userDao.removeFriend(1L, 3L);
        checkList.remove(userThree);
        assertEquals(checkList, userDao.findFriends(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void findCommonFriendsFilledListSuccess() throws Exception {
        initCreateUserUserOne();
        initCreateUserUserTwo();
        initCreateUserUserThree();
        initMockPerformUsersUserOneOk();
        initMockPerformUsersUserTwoOk();
        initMockPerformUsersUserThreeOk();
        userOne.setId(1L);
        userTwo.setId(2L);
        userThree.setId(3L);

        userDao.addFriend(1L, 2L);
        userDao.addFriend(1L, 3L);

        userDao.addFriend(2L, 1L);
        userDao.addFriend(2L, 3L);

        userDao.addFriend(3L, 2L);
        userDao.addFriend(3L, 1L);

        List<User> checkList = new ArrayList<>();
        checkList.add(userThree);
        assertEquals(checkList, userDao.findCommonFriends(1L, 2L));
    }

}