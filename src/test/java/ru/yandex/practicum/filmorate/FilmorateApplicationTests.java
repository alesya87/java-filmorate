package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.impl.FilmServiceImpl;
import ru.yandex.practicum.filmorate.service.impl.GenreServiceImpl;
import ru.yandex.practicum.filmorate.service.impl.MpaServiceImpl;
import ru.yandex.practicum.filmorate.service.impl.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmorateApplicationTests {
    private final UserServiceImpl userServiceImpl;
    private final FilmServiceImpl filmServiceImpl;
    private final GenreServiceImpl genreServiceImpl;
    private final MpaServiceImpl mpaServiceImpl;
    private final LikeStorage likeStorage;
    private User firstUser;
    private User firstUserUpdate;
    private User secondUser;
    private User thirdUser;
    private Film firstFilm;
    private Film firstFilmUpdate;
    private Film secondFilm;
    private Film thirdFilm;

    @BeforeEach
    public void beforeEach() {
        firstUser = User.builder().name("firstUser").login("firstUser").email("firstUser@ya.ru").birthday(LocalDate.of(1983, 10, 11)).build();
        firstUserUpdate = User.builder().id(1).name("firstUserUpdate").login("firstUserUpdate").email("firstUserUpdate@ya.ru").birthday(LocalDate.of(1983, 10, 11)).build();
        secondUser = User.builder().name("secondUser").login("secondUser").email("secondUser@ya.ru").birthday(LocalDate.of(1986, 10, 20)).build();
        thirdUser = User.builder().name("thirdUser").login("thirdUser").email("thirdUser@ya.ru").birthday(LocalDate.of(1987, 5, 11)).build();

        firstFilm = Film.builder().name("firstFilm").description("firstFilm description").releaseDate(LocalDate.of(2022, 11, 15)).duration(114).build();
        firstFilm.setMpa(MpaRating.builder().id(1).name("G").build());
        firstFilm.setLikes(new HashSet<>());
        firstFilm.setGenres(new HashSet<>(Arrays.asList(Genre.builder().id(2).name("Драма").build(), Genre.builder().id(1).name("Комедия").build())));
        firstFilmUpdate = Film.builder().id(1).name("firstFilmUpdate").description("firstFilm description Update").releaseDate(LocalDate.of(2023, 12, 16)).duration(114).build();
        firstFilmUpdate.setGenres(new HashSet<>(Arrays.asList(Genre.builder().id(6).name("Боевик").build())));
        firstFilmUpdate.setMpa(MpaRating.builder().id(3).name("PG-13").build());

        secondFilm = Film.builder().name("secondFilm").description("secondFilm description").releaseDate(LocalDate.of(2021, 10, 4)).duration(162).build();
        secondFilm.setMpa(MpaRating.builder().id(3).name("PG-13").build());
        secondFilm.setLikes(new HashSet<>());
        secondFilm.setGenres(new HashSet<>(Arrays.asList(Genre.builder().id(6).name("Боевик").build())));

        thirdFilm = Film.builder().name("thirdFilm").description("thirdFilm description").releaseDate(LocalDate.of(2023, 5, 10)).duration(121).build();
        thirdFilm.setMpa(MpaRating.builder().id(4).name("R").build());
        thirdFilm.setLikes(new HashSet<>());
        thirdFilm.setGenres(new HashSet<>(Arrays.asList(Genre.builder().id(2).name("Драма").build())));
    }

    @Test
    public void shouldAddUserWithId1() {
        User user = userServiceImpl.add(firstUser);
        assertEquals(1, user.getId());
    }

    @Test
    public void shouldAddUserWithId2() {
        userServiceImpl.add(firstUser);
        User user = userServiceImpl.add(secondUser);
        assertEquals(2, user.getId());
    }

    @Test
    public void shouldUpdateUser() {
        userServiceImpl.add(firstUser);
        userServiceImpl.update(firstUserUpdate);
        User user = userServiceImpl.getUserById(1);
        assertEquals("firstUserUpdate", user.getName());
        assertEquals("firstUserUpdate", user.getLogin());
        assertEquals("firstUserUpdate@ya.ru", user.getEmail());
    }

    @Test
    public void shouldReturnUserById() {
        userServiceImpl.add(firstUser);
        User user = userServiceImpl.getUserById(1);
        assertEquals("firstUser", user.getName());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionIfUserEmptyWhenGetUserById() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userServiceImpl.getUserById(1));
        assertEquals("Пользователя с id 1 не существует", exception.getMessage());
    }

    @Test
    public void shouldReturnAllUsers() {
        userServiceImpl.add(firstUser);
        userServiceImpl.add(secondUser);
        userServiceImpl.add(thirdUser);
        assertEquals(3, userServiceImpl.getAllUsers().size());
    }

    @Test
    public void shouldReturnEmptyArrayIfNoUsersWhenGetAllUsers() {
        assertEquals(0, userServiceImpl.getAllUsers().size());
    }

    @Test
    public void shouldAddFriend() {
        User user1 = userServiceImpl.add(firstUser);
        User user2 = userServiceImpl.add(secondUser);
        userServiceImpl.addFriend(user1.getId(), user2.getId());
        assertEquals(1, userServiceImpl.getAllFriends(user1.getId()).size());
        assertEquals(2, userServiceImpl.getAllFriends(user1.getId()).get(0).getId());
    }

    @Test
    public void shouldDeleteFriend() {
        User user1 = userServiceImpl.add(firstUser);
        User user2 = userServiceImpl.add(secondUser);
        userServiceImpl.addFriend(user1.getId(), user2.getId());
        userServiceImpl.deleteFriend(user1.getId(), user2.getId());
        assertEquals(0, userServiceImpl.getAllFriends(user1.getId()).size());
    }

    @Test
    public void shouldReturnMutualFriends() {
        User user1 = userServiceImpl.add(firstUser);
        User user2 = userServiceImpl.add(secondUser);
        User user3 = userServiceImpl.add(thirdUser);
        userServiceImpl.addFriend(user1.getId(), user2.getId());
        userServiceImpl.addFriend(user3.getId(), user2.getId());
        assertEquals(1, userServiceImpl.getMutualFriends(user1.getId(), user3.getId()).size());
        assertEquals(2, userServiceImpl.getMutualFriends(user1.getId(), user3.getId()).get(0).getId());
    }

    @Test
    public void shouldReturnEmptyArrayGetMutualFriends() {
        User user1 = userServiceImpl.add(firstUser);
        User user2 = userServiceImpl.add(secondUser);
        assertEquals(0, userServiceImpl.getMutualFriends(user1.getId(), user2.getId()).size());
    }

    @Test
    public void shouldAddFilmWithId1() {
        Film film = filmServiceImpl.add(firstFilm);
        assertEquals(1, film.getId());
    }

    @Test
    public void shouldAddFilmWithId2() {
        filmServiceImpl.add(firstFilm);
        Film film = filmServiceImpl.add(secondFilm);
        assertEquals(2, film.getId());
    }

    @Test
    public void shouldUpdateFilmWithNewGenreAndMpa() {
        Film film = filmServiceImpl.add(firstFilm);
        assertEquals(2, film.getGenres().size());
        List<Genre> genres = new ArrayList<>(film.getGenres());
        assertEquals(1, genres.get(0).getId());
        assertEquals(2, genres.get(1).getId());
        assertEquals(1, film.getMpa().getId());
        filmServiceImpl.update(firstFilmUpdate);
        Film filmUpdate = filmServiceImpl.getFilmById(1);
        List<Genre> genresUpdate = new ArrayList<>(filmUpdate.getGenres());
        assertEquals(6, genresUpdate.get(0).getId());
        assertEquals("firstFilmUpdate", filmUpdate.getName());
        assertEquals(1, filmUpdate.getGenres().size());
        assertEquals(3, filmUpdate.getMpa().getId());
    }

    @Test
    public void shouldReturnFilmById() {
        filmServiceImpl.add(firstFilm);
        Film film = filmServiceImpl.getFilmById(1);
        assertEquals("firstFilm", film.getName());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionIfFilmEmptyWhenGetFilmById() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> filmServiceImpl.getFilmById(1));
        assertEquals("Фильма с id 1 не существует", exception.getMessage());
    }

    @Test
    public void shouldReturnAllFilms() {
        filmServiceImpl.add(firstFilm);
        filmServiceImpl.add(secondFilm);
        filmServiceImpl.add(thirdFilm);
        assertEquals(3, filmServiceImpl.getAllFilms().size());
    }

    @Test
    public void shouldReturnEmptyArrayIfNoFilmsWhenGetAllFilms() {
        assertEquals(0, filmServiceImpl.getAllFilms().size());
    }

    @Test
    public void shouldAddAndDeleteLike() {
        Film film = filmServiceImpl.add(firstFilm);
        User user = userServiceImpl.add(firstUser);
        likeStorage.addLike(film.getId(), user.getId());
        assertEquals(1, filmServiceImpl.getFilmById(1).getRate());
        likeStorage.deleteLike(film.getId(), user.getId());
        assertEquals(0, filmServiceImpl.getFilmById(1).getRate());
    }

    @Test
    public void shouldReturnFilmsByLikesCount1() {
        filmServiceImpl.add(firstFilm);
        userServiceImpl.add(firstUser);
        Film film = filmServiceImpl.add(secondFilm);
        User user = userServiceImpl.add(secondUser);
        likeStorage.addLike(film.getId(), user.getId());
        assertEquals(1, filmServiceImpl.getFilmsByLikesCount(1).size());
        assertEquals(2, filmServiceImpl.getFilmsByLikesCount(1).get(0).getId());
    }

    @Test
    public void shouldReturnFilmsByLikesCount2() {
        Film film = filmServiceImpl.add(firstFilm);
        User user = userServiceImpl.add(firstUser);
        filmServiceImpl.add(secondFilm);
        userServiceImpl.add(secondUser);
        likeStorage.addLike(film.getId(), user.getId());
        assertEquals(2, filmServiceImpl.getFilmsByLikesCount(2).size());
        assertEquals(1, filmServiceImpl.getFilmsByLikesCount(2).get(0).getId());
    }

    @Test
    public void shouldReturnEmptyArrayWhenGetFilmsByLikesCount1() {
        assertEquals(0, filmServiceImpl.getFilmsByLikesCount(10).size());
    }

    @Test
    public void shouldReturnMpaById() {
        assertEquals("G", mpaServiceImpl.getMpaRatingById(1).getName());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionIfMpaEmptyWhenGetMpaById() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> mpaServiceImpl.getMpaRatingById(10));
        assertEquals("MPA с id 10 не существует", exception.getMessage());
    }

    @Test
    public void shouldReturnAllMpa() {
        assertEquals(5, mpaServiceImpl.getAllMpaRatings().size());
    }

    @Test
    public void shouldReturnGenreById() {
        assertEquals("Комедия", genreServiceImpl.getGenreById(1).getName());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionIfGenreEmptyWhenGetGenreById() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> genreServiceImpl.getGenreById(10));
        assertEquals("Жанра с id 10 не существует", exception.getMessage());
    }

    @Test
    public void shouldReturnAllGenres() {
        assertEquals(6, genreServiceImpl.getAllGenres().size());
    }
}
