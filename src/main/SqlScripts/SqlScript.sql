CREATE DATABASE IF NOT EXISTS QUIZWEBSITE;
USE QUIZWEBSITE;
DROP TABLE IF EXISTS FRIENDS;
DROP TABLE IF EXISTS QUIZZES;
DROP TABLE IF EXISTS COMPLETEDQUIZZES;
DROP TABLE IF EXISTS CHALANGES;
DROP TABLE IF EXISTS MESSAGES;
DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS
(
    ID       int primary key NOT NULL AUTO_INCREMENT,
    USERNAME CHAR(64) UNIQUE not null,
    PASSWORD CHAR(64)        not null
);

CREATE TABLE FRIENDS
(
    ID       int primary key NOT NULL AUTO_INCREMENT,
    USER1ID  int             not null,
    USER2ID  int             not null,
    ACCEPTED tinyint default 0,
    CHECK (ACCEPTED = 0 OR ACCEPTED = 1),
    FOREIGN KEY (USER1ID) REFERENCES USERS (ID),
    FOREIGN KEY (USER2ID) REFERENCES USERS (ID)
);

CREATE TABLE QUIZZES
(
    ID           int primary key NOT NULL AUTO_INCREMENT,
    QUIZNAME     CHAR(64) UNIQUE not null,
    DESCRIPT     TEXT,
    COMPLETED    INT      default 0,
    CREATIONTIME DATETIME default current_timestamp,
    CREATORID    INT             not null,
    QUESTIONS    CHAR(64),
    FOREIGN KEY (CREATORID) REFERENCES USERS (ID)
);

CREATE TABLE COMPLETEDQUIZZES
(
    ID        int primary key NOT NULL AUTO_INCREMENT,
    USERID    INT             not null,
    QUIZID    INT             not null,
    SCORE     INT      default 0,
    SPENTTIME TIME            not null,
    WRITETIME DATETIME default current_timestamp,
    FOREIGN KEY (USERID) references USERS (ID),
    FOREIGN KEY (QUIZID) references QUIZZES (ID)
);

CREATE TABLE CHALANGES
(
    ID      int primary key NOT NULL AUTO_INCREMENT,
    USER1ID int             not null,
    USER2ID int             not null,
    QUIZID  INT             not null,
    FOREIGN KEY (USER1ID) REFERENCES USERS (ID),
    FOREIGN KEY (USER2ID) REFERENCES USERS (ID),
    FOREIGN KEY (QUIZID) references QUIZZES (ID)
);

CREATE TABLE MESSAGES
(
    ID       int primary key NOT NULL AUTO_INCREMENT,
    USER1ID  int             not null,
    USER2ID  int             not null,
    MESSAGE  TEXT,
    SENDDATE DATETIME default current_timestamp,
    FOREIGN KEY (USER1ID) REFERENCES USERS (ID),
    FOREIGN KEY (USER2ID) REFERENCES USERS (ID)
);