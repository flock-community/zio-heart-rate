CREATE TABLE IF NOT EXISTS users
(
    id   bigserial PRIMARY KEY,
    name varchar
);
INSERT INTO users(name)
VALUES ('Bert');
INSERT INTO users(name)
VALUES ('Ernie');
