DROP DATABASE IF EXISTS QUIZ_WEBSITE_TEST;
CREATE DATABASE QUIZ_WEBSITE_TEST;
USE QUIZ_WEBSITE_TEST;

CREATE TABLE USERS
(
    ID       int primary key NOT NULL AUTO_INCREMENT,
    USERNAME CHAR(64) UNIQUE not null,
    STATUS   TINYINT DEFAULT 0,
    SCORE    int     DEFAULT 0,
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
    COMPLETED     INT       default 0,
    CREATION_TIME DATETIME(3)  default current_timestamp(3),
    CREATOR_ID    INT             not null,
    TIME_LIMIT    TIME      default 0,
    CATEGORIES    char(255) default 'other',
    FOREIGN KEY (CREATOR_ID) REFERENCES USERS (ID) ON DELETE CASCADE
);
CREATE TABLE RATINGS
(
    ID         int primary key NOT NULL AUTO_INCREMENT,
    USER_ID    INT             NOT NULL,
    QUIZ_ID    INT             NOT NULL,
    RATING     INT             NOT NULL,
    COMMENT    TEXT,
    RATED_DATE DATETIME(3) default current_timestamp(3),
    FOREIGN KEY (USER_ID) REFERENCES USERS (ID) ON DELETE CASCADE,
    FOREIGN KEY (QUIZ_ID) REFERENCES QUIZZES (ID) ON DELETE CASCADE
);
CREATE TABLE COMPLETED_QUIZZES
(
    ID         int primary key NOT NULL AUTO_INCREMENT,
    USER_ID    INT             not null,
    QUIZ_ID    INT             not null,
    SCORE      INT      default 0,
    SPENT_TIME LONG            not null,
    WRITE_TIME DATETIME(3) default current_timestamp(3),
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
    SEND_DATE DATETIME(3) default current_timestamp(3),
    SEEN      tinyint  default 0,
    FOREIGN KEY (USER1_ID) REFERENCES USERS (ID) ON DELETE CASCADE,
    FOREIGN KEY (USER2_ID) REFERENCES USERS (ID) ON DELETE CASCADE
);

CREATE TABLE QUESTIONS
(
    ID            int primary key not null AUTO_INCREMENT,
    CATEGORY_NAME char(64)        not null,
    QUIZ_ID       int             NOT NULL,
    QUESTION_TEXT TEXT,
    ANSWERS       TEXT,
    FOREIGN KEY (QUIZ_ID) REFERENCES QUIZZES (ID) ON DELETE CASCADE
);

CREATE TABLE ANNOUNCEMENTS
(
    ID         int primary key NOT NULL AUTO_INCREMENT,
    TITLE      TEXT            not null,
    BODY       TEXT            not null,
    WRITER_ID  INT             not null,
    WRITE_TIME DATETIME(3) default current_timestamp(3),
    FOREIGN KEY (WRITER_ID) REFERENCES USERS (ID) ON DELETE CASCADE
);