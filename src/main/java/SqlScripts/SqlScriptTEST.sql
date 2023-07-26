CREATE DATABASE IF NOT EXISTS QUIZ_WEBSITE_TEST;
USE QUIZ_WEBSITE_TEST;
DROP TABLE IF EXISTS FRIENDS;
DROP TABLE IF EXISTS COMPLETED_QUIZZES;
DROP TABLE IF EXISTS CHALLENGES;
DROP TABLE IF EXISTS MESSAGES;
DROP TABLE IF EXISTS QUESTIONS;
DROP TABLE IF EXISTS QUIZZES;
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
    USER1_ID int             not null,
    USER2_ID int             not null,
    ACCEPTED tinyint default 0,
    CHECK (ACCEPTED = 0 OR ACCEPTED = 1),
    FOREIGN KEY (USER1_ID) REFERENCES USERS (ID) ON DELETE CASCADE,
    FOREIGN KEY (USER2_ID) REFERENCES USERS (ID) ON DELETE CASCADE
);


CREATE TABLE QUIZZES
(
    ID            int primary key NOT NULL AUTO_INCREMENT,
    QUIZ_NAME     CHAR(64) UNIQUE not null,
    DESCRIPTION   TEXT,
    COMPLETED     INT      default 0,
    CREATION_TIME DATETIME default current_timestamp,
    CREATOR_ID    INT             not null,
    FOREIGN KEY (CREATOR_ID) REFERENCES USERS (ID) ON DELETE CASCADE
);

CREATE TABLE COMPLETED_QUIZZES
(
    ID         int primary key NOT NULL AUTO_INCREMENT,
    USER_ID    INT             not null,
    QUIZ_ID    INT             not null,
    SCORE      INT      default 0,
    SPENT_TIME TIME            not null,
    WRITE_TIME DATETIME default current_timestamp,
    FOREIGN KEY (USER_ID) references USERS (ID) ON DELETE CASCADE,
    FOREIGN KEY (QUIZ_ID) references QUIZZES (ID) ON DELETE CASCADE
);

CREATE TABLE CHALLENGES
(
    ID       int primary key NOT NULL AUTO_INCREMENT,
    USER1_ID int             not null,
    USER2_ID int             not null,
    QUIZ_ID  INT             not null,
    FOREIGN KEY (USER1_ID) REFERENCES USERS (ID) ON DELETE CASCADE,
    FOREIGN KEY (USER2_ID) REFERENCES USERS (ID) ON DELETE CASCADE,
    FOREIGN KEY (QUIZ_ID) references QUIZZES (ID) ON DELETE CASCADE
);

CREATE TABLE MESSAGES
(
    ID        int primary key NOT NULL AUTO_INCREMENT,
    USER1_ID  int             not null,
    USER2_ID  int             not null,
    MESSAGE   TEXT,
    SEND_DATE DATETIME default current_timestamp,
    SEEN      tinyint  default 0,
    FOREIGN KEY (USER1_ID) REFERENCES USERS (ID) ON DELETE CASCADE,
    FOREIGN KEY (USER2_ID) REFERENCES USERS (ID) ON DELETE CASCADE
);

CREATE TABLE QUESTIONS
(
    ID            int primary key not null AUTO_INCREMENT,
    CATEGORY_NAME char(64)        not null,
    QUIZ_ID       int DEFAULT NULL,
    QUESTION_TEXT TEXT,
    ANSWERS       TEXT,
    FOREIGN KEY (QUIZ_ID) REFERENCES QUIZZES (ID) ON DELETE CASCADE,
    CHECK (CATEGORY_NAME = 'fillInTheBlank' OR CATEGORY_NAME = 'matching' OR CATEGORY_NAME = 'multiAnswers' OR
           CATEGORY_NAME = 'multipleChoices' OR CATEGORY_NAME = 'multipleChoicesWithMultipleAnswers' OR
           CATEGORY_NAME = 'pictureResponse' OR CATEGORY_NAME = 'textResponse')
);