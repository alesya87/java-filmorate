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
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.*;

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
class FilmoRateApplicationTests {
    private final UserService userService;
    private final FilmService filmService;
    private final GenreService genreService;
    private final MpaService mpaService;
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

        thirdFilm = Film.builder().name("thirdFilm").description("thirdFilm description").releaseDate(LocalDate.of(2023, 05, 10)).duration(121).build();
        thirdFilm.setMpa(MpaRating.builder().id(4).name("R").build());
        thirdFilm.setLikes(new HashSet<>());
        thirdFilm.setGenres(new HashSet<>(Arrays.asList(Genre.builder().id(2).name("Драма").build())));
    }

    @Test
    public void shouldAddUserWithId1() {
        User user = userService.add(firstUser);
        assertEquals(1, user.getId());
    }

    @Test
    public void shouldAddUserWithId2() {
        userService.add(firstUser);
        User user = userService.add(secondUser);
        assertEquals(2, user.getId());
    }

    @Test
    public void shouldUpdateUser() {
        userService.add(firstUser);
        userService.update(firstUserUpdate);
        User user = userService.getUserById(1);
        assertEquals("firstUserUpdate", user.getName());
        assertEquals("firstUserUpdate", user.getLogin());
        assertEquals("firstUserUpdate@ya.ru", user.getEmail());
    }

    @Test
    public void shouldReturnUserById() {
        userService.add(firstUser);
        User user = userService.getUserById(1);
        assertEquals("firstUser", user.getName());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionIfUserEmptyWhenGetUserById() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.getUserById(1));
        assertEquals("Пользователя с id 1 не существует", exception.getMessage());
    }

    @Test
    public void shouldReturnAllUsers() {
        userService.add(firstUser);
        userService.add(secondUser);
        userService.add(thirdUser);
        assertEquals(3, userService.getAllUsers().size());
    }

    @Test
    public void shouldReturnEmptyArrayIfNoUsersWhenGetAllUsers() {
        assertEquals(0, userService.getAllUsers().size());
    }

    @Test
    public void shouldAddFriend() {
        User user1 = userService.add(firstUser);
        User user2 = userService.add(secondUser);
        userService.addFriend(user1.getId(), user2.getId());
        assertEquals(1, userService.getAllFriends(user1.getId()).size());
        assertEquals(2, userService.getAllFriends(user1.getId()).get(0).getId());
    }

    @Test
    public void shouldDeleteFriend() {
        User user1 = userService.add(firstUser);
        User user2 = userService.add(secondUser);
        userService.addFriend(user1.getId(), user2.getId());
        userService.deleteFriend(user1.getId(), user2.getId());
        assertEquals(0, userService.getAllFriends(user1.getId()).size());
    }

    @Test
    public void shouldReturnMutualFriends() {
        User user1 = userService.add(firstUser);
        User user2 = userService.add(secondUser);
        User user3 = userService.add(thirdUser);
        userService.addFriend(user1.getId(), user2.getId());
        userService.addFriend(user3.getId(), user2.getId());
        assertEquals(1, userService.getMutualFriends(user1.getId(), user3.getId()).size());
        assertEquals(2, userService.getMutualFriends(user1.getId(), user3.getId()).get(0).getId());
    }

    @Test
    public void shouldReturnEmptyArrayGetMutualFriends() {
        User user1 = userService.add(firstUser);
        User user2 = userService.add(secondUser);
        assertEquals(0, userService.getMutualFriends(user1.getId(), user2.getId()).size());
    }

    @Test
    public void shouldAddFilmWithId1() {
        Film film = filmService.add(firstFilm);
        assertEquals(1, film.getId());
    }

    @Test
    public void shouldAddFilmWithId2() {
        filmService.add(firstFilm);
        Film film = filmService.add(secondFilm);
        assertEquals(2, film.getId());
    }

    @Test
    public void shouldUpdateFilmWithNewGenreAndMpa() {
        Film film = filmService.add(firstFilm);
        assertEquals(2, film.getGenres().size());
        List<Genre> genres = new ArrayList<>(film.getGenres());
        assertEquals(1, genres.get(0).getId());
        assertEquals(2, genres.get(1).getId());
        assertEquals(1, film.getMpa().getId());
        filmService.update(firstFilmUpdate);
        Film filmUpdate = filmService.getFilmById(1);
        List<Genre> genresUpdate = new ArrayList<>(filmUpdate.getGenres());
        assertEquals(6, genresUpdate.get(0).getId());
        assertEquals("firstFilmUpdate", filmUpdate.getName());
        assertEquals(1, filmUpdate.getGenres().size());
        assertEquals(3, filmUpdate.getMpa().getId());
    }

    @Test
    public void shouldReturnFilmById() {
        filmService.add(firstFilm);
        Film film = filmService.getFilmById(1);
        assertEquals("firstFilm", film.getName());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionIfFilmEmptyWhenGetFilmById() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> filmService.getFilmById(1));
        assertEquals("Фильма с id 1 не существует", exception.getMessage());
    }

    @Test
    public void shouldReturnAllFilms() {
        filmService.add(firstFilm);
        filmService.add(secondFilm);
        filmService.add(thirdFilm);
        assertEquals(3, filmService.getAllFilms().size());
    }

    @Test
    public void shouldReturnEmptyArrayIfNoFilmsWhenGetAllFilms() {
        assertEquals(0, filmService.getAllFilms().size());
    }

    @Test
    public void shouldAddAndDeleteLike() {
        Film film = filmService.add(firstFilm);
        User user = userService.add(firstUser);
        likeStorage.addLike(film.getId(), user.getId());
        assertEquals(1, filmService.getFilmById(1).getRate());
        likeStorage.deleteLike(film.getId(), user.getId());
        assertEquals(0, filmService.getFilmById(1).getRate());
    }

    @Test
    public void shouldReturnFilmsByLikesCount1() {
        filmService.add(firstFilm);
        userService.add(firstUser);
        Film film = filmService.add(secondFilm);
        User user = userService.add(secondUser);
        likeStorage.addLike(film.getId(), user.getId());
        assertEquals(1, filmService.getFilmsByLikesCount(1).size());
        assertEquals(2, filmService.getFilmsByLikesCount(1).get(0).getId());
    }

    @Test
    public void shouldReturnFilmsByLikesCount2() {
        Film film = filmService.add(firstFilm);
        User user = userService.add(firstUser);
        filmService.add(secondFilm);
        userService.add(secondUser);
        likeStorage.addLike(film.getId(), user.getId());
        assertEquals(2, filmService.getFilmsByLikesCount(2).size());
        assertEquals(1, filmService.getFilmsByLikesCount(2).get(0).getId());
    }

    @Test
    public void shouldReturnEmptyArrayWhenGetFilmsByLikesCount1() {
        assertEquals(0, filmService.getFilmsByLikesCount(10).size());
    }

    @Test
    public void shouldReturnMpaById() {
        assertEquals("G", mpaService.getMpaRatingById(1).getName());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionIfMpaEmptyWhenGetMpaById() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> mpaService.getMpaRatingById(10));
        assertEquals("MPA с id 10 не существует", exception.getMessage());
    }

    @Test
    public void shouldReturnAllMpa() {
        assertEquals(5, mpaService.getAllMpaRatings().size());
    }

    @Test
    public void shouldReturnGenreById() {
        assertEquals("Комедия", genreService.getGenreById(1).getName());
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionIfGenreEmptyWhenGetGenreById() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> genreService.getGenreById(10));
        assertEquals("Жанра с id 10 не существует", exception.getMessage());
    }

    @Test
    public void shouldReturnAllGenres() {
        assertEquals(6, genreService.getAllGenres().size());
    }
}
