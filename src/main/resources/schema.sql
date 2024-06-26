DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS film_genres CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS friends CASCADE;
DROP TABLE IF EXISTS mpa_ratings CASCADE;
DROP TABLE IF EXISTS likes CASCADE;

DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS film_genres CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS friends CASCADE;
DROP TABLE IF EXISTS mpa_ratings CASCADE;
DROP TABLE IF EXISTS likes CASCADE;

CREATE TABLE IF NOT EXISTS users (
    id INT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255),
    login VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    birthday DATE
);

CREATE TABLE IF NOT EXISTS mpa_ratings (
    id INT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    id INT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(200) NOT NULL,
    duration INT NOT NULL,
    release_date TIMESTAMP NOT NULL,
    rate INT,
    mpa_id INT REFERENCES mpa_ratings(id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS genres (
    id INT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genres (
    film_id INT NOT NULL REFERENCES films (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    genre_id INT NOT NULL REFERENCES genres (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS likes (
    film_id INT NOT NULL REFERENCES films (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    user_id INT NOT NULL REFERENCES users (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    PRIMARY KEY (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS friends (
    user_id INT NOT NULL REFERENCES users (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    friend_id INT NOT NULL REFERENCES users (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    status BOOLEAN NOT NULL,
    PRIMARY KEY (user_id, friend_id)
);
