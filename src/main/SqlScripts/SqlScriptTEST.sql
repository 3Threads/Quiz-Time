CREATE DATABASE IF NOT EXISTS QUIZWEBSITE;
USE QUIZWEBSITE;
CREATE TABLE USERSTEST
(
    ID       int primary key NOT NULL AUTO_INCREMENT,
    USERNAME CHAR(64) UNIQUE,
    EMAIL    CHAR(64) UNIQUE,
    PASSWORD CHAR(64)
);