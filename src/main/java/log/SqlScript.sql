CREATE DATABASE IF NOT EXISTS users;
USE users;
DROP TABLE IF EXISTS usernamesAndPasswords;
CREATE TABLE usernamesAndPasswords (
    username CHAR(64),
    email CHAR(64) CONSTRAINT UNIQUE,
    password CHAR(64)
);