# java-filmorate
Template repository for Filmorate project.

![Диаграма](diagram.png)

String sqlUpdateUserById = "update users set name = ?, login = ?, email = ?, birthday = ? where id = ?";

String sqlGetUserById = "select id, name, login, email, birthday from users where id = ?";

String sqlGetAllUsers = "select id, name, login, email, birthday from users";

String sqlSelectFriendStatus = "select COUNT(status) > 0 from friends where user_id = ? and friend_id = ?";

String sqlAddFriend = "insert into friends(user_id, friend_id, status) values (?, ?, ?)";

String sqlDeleteFriend = "delete from friends where user_id = ? and friend_id = ?";

String sqlGetFriendsByUserId = "select id, name, login, email, birthday from users u inner join friends f on u.id = f.friend_id where f.user_id = ?";

String sqlGetMutualFriends = "select id, name, login, email, birthday from users where id in (select f.friend_id from friends as f inner join friends as f2 on f.friend_id = f2.friend_id where f.user_id = ? and f2.user_id = ?)";

String sqlGetFriendsIdByUserId = "select friend_id from friends where user_id = ?";

String sqlUpdateFilmById = "update films set name = ?, description = ?, duration = ?, release_date = ?, mpa_id = ? where id = ?";

String sqlGetFilmById = "select id, name, description, duration, release_date, rate from films where id = ?";

String sqlGetAllFilms = "select id, name, description, duration, release_date, rate from films order by id";

String sqlGetAllFilmsByLikesCount = "select id, name, description, duration, release_date, rate from films order by rate DESC limit ?";

String sqlLinkFilmWithGenre = "insert into film_genres (film_id, genre_id) values (?, ?)";

String sqlUnlinkFilmWithGenre = "delete from film_genres where film_id = ?";

String sqlGetGenreById = "select id, name from genres where id = ?";

String sqlGetAllGenres = "select id, name from genres";

String sqlGetGenresByFilmId = "select id, name from genres g inner join film_genres fg on g.id = fg.genre_id where fg.film_id = ?";

String sqlGetLikesCountByFilmId = "select count(user_id) from likes where film_id = ?";

String sqlGetLikesByFilmId = "select user_id from likes where film_id = ?";

String sqlAddLike = "insert into likes (film_id, user_id) values (?, ?)";

String sqlIncreaseFilmRate = "update films set rate = (select COUNT(user_id) as rate from likes where film_id = ?) where id = ?";

String sqlDeleteLike = "delete from likes where film_id =? and user_id = ?";

String sqlDecreaseFilmRate = "update films set rate = (select COUNT(user_id) as rate from likes where film_id = ?) where id = ?";

String sqlGetMpaRatingById = "select id, name from mpa_ratings where id = ?";

String sqlGetAllMpaRatings = "select id, name from mpa_ratings";

String sqlGetMpaRatingsByFilmId = "select mpa.id, mpa.name from mpa_ratings mpa inner join films f on mpa.id = f.mpa_id where f.id = ?";
