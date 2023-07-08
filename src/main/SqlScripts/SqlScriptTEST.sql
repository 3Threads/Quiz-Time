CREATE DATABASE IF NOT EXISTS QUIZWEBSITE;
USE QUIZWEBSITE;
DROP TABLE IF EXISTS FRIENDSTEST;
DROP TABLE IF EXISTS QUIZZESTEST;
DROP TABLE IF EXISTS COMPLETEDQUIZZESTEST;
DROP TABLE IF EXISTS CHALANGESTEST;
DROP TABLE IF EXISTS MESSAGESTEST;
DROP TABLE IF EXISTS USERSTEST;

CREATE TABLE USERSTEST
(
    ID       int primary key NOT NULL AUTO_INCREMENT,
    USERNAME CHAR(64) UNIQUE not null,
    PASSWORD CHAR(64)        not null
);

CREATE TABLE FRIENDSTEST
(
    ID       int primary key NOT NULL AUTO_INCREMENT,
    USER1ID  int             not null,
    USER2ID  int             not null,
    ACCEPTED tinyint default 0,
    CHECK (ACCEPTED = 0 OR ACCEPTED = 1),
    FOREIGN KEY (USER1ID) REFERENCES USERSTEST (ID),
    FOREIGN KEY (USER2ID) REFERENCES USERSTEST (ID)
);

CREATE TABLE QUIZZESTEST
(
    ID           int primary key NOT NULL AUTO_INCREMENT,
    QUIZNAME     CHAR(64) UNIQUE not null,
    DESCRIPT     TEXT,
    COMPLETED    INT      default 0,
    CREATIONTIME DATETIME default current_timestamp,
    CREATORID    INT             not null,
    QUESTIONS    CHAR(64),
    FOREIGN KEY (CREATORID) REFERENCES USERSTEST (ID)
);

CREATE TABLE COMPLETEDQUIZZESTEST
(
    ID        int primary key NOT NULL AUTO_INCREMENT,
    USERID    INT             not null,
    QUIZID    INT             not null,
    SCORE     INT      default 0,
    SPENTTIME TIME            not null,
    WRITETIME DATETIME default current_timestamp,
    FOREIGN KEY (USERID) references USERSTEST (ID),
    FOREIGN KEY (QUIZID) references QUIZZESTEST (ID)
);

CREATE TABLE CHALANGESTEST
(
    ID      int primary key NOT NULL AUTO_INCREMENT,
    USER1ID int             not null,
    USER2ID int             not null,
    QUIZID  INT             not null,
    FOREIGN KEY (USER1ID) REFERENCES USERSTEST (ID),
    FOREIGN KEY (USER2ID) REFERENCES USERSTEST (ID),
    FOREIGN KEY (QUIZID) references QUIZZESTEST (ID)
);

CREATE TABLE MESSAGESTEST
(
    ID       int primary key NOT NULL AUTO_INCREMENT,
    USER1ID  int             not null,
    USER2ID  int             not null,
    MESSAGE  TEXT,
    SENDDATE DATETIME default current_timestamp,
    FOREIGN KEY (USER1ID) REFERENCES USERSTEST (ID),
    FOREIGN KEY (USER2ID) REFERENCES USERSTEST (ID)
);