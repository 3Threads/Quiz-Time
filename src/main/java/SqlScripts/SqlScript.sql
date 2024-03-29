DROP DATABASE IF EXISTS QUIZ_WEBSITE;
CREATE DATABASE QUIZ_WEBSITE;
USE QUIZ_WEBSITE;

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

INSERT INTO USERS (USERNAME, STATUS, PASSWORD, SCORE)
VALUES ('USER1', 1, '2def06fb91eb2d1bcd9e22dd131c6999113b552b', 100),
       ('USER2', 0, '2def06fb91eb2d1bcd9e22dd131c6999113b552b', 300),
       ('USER3', 0, '2def06fb91eb2d1bcd9e22dd131c6999113b552b', 500),
       ('USER4', 0, '2def06fb91eb2d1bcd9e22dd131c6999113b552b', 700),
       ('USER5', 1, '2def06fb91eb2d1bcd9e22dd131c6999113b552b', 900),
       ('USER6', 0, '2def06fb91eb2d1bcd9e22dd131c6999113b552b', 1100);

INSERT INTO MESSAGES (USER1_ID, USER2_ID, MESSAGE)
VALUES (1, 2, 'hey'),
       (2, 1, 'Hello'),
       (5, 3, 'Accept my friend request'),
       (3, 5, 'NO!'),
       (3, 4, 'Done inserting!');

INSERT INTO FRIENDS (USER1_ID, USER2_ID, ACCEPTED)
VALUES (1, 2, 1),
       (2, 3, 0),
       (4, 5, 1),
       (5, 3, 0);

INSERT INTO QUIZZES (QUIZ_NAME, DESCRIPTION, COMPLETED, CREATION_TIME, CREATOR_ID, CATEGORIES)
VALUES ('quiz1', 'QUIZ', 0, default, 1, 'Sports'),
       ('quiz2', 'QUIZ', 2, default, 1, 'Sports'),
       ('quiz3', 'QUIZ', 2, default, 2, 'Music'),
       ('quiz4', 'QUIZ', 0, default, 3, 'History,Sports'),
       ('quiz5', 'QUIZ', 1, default, 4, default),
       ('quiz6', 'QUIZ', 1, default, 5, 'Science,Music');

INSERT INTO COMPLETED_QUIZZES (USER_ID, QUIZ_ID, SCORE, SPENT_TIME, WRITE_TIME)
VALUES (1, 3, 10, 17000, default),
       (2, 3, 10, 21000, default),
       (2, 5, 1, 10000, default),
       (2, 6, 0, 10000, default),
       (4, 2, 20, 12000, default),
       (4, 2, 22, 59000, default);

INSERT INTO CHALLENGES (USER1_ID, USER2_ID, QUIZ_ID)
VALUES (1, 2, 1),
       (1, 3, 1),
       (1, 4, 1),
       (1, 5, 2),
       (2, 1, 3),
       (3, 5, 5);

INSERT INTO QUESTIONS(CATEGORY_NAME, QUIZ_ID, QUESTION_TEXT, ANSWERS)
VALUES ('textResponse', 1, 'Is this text response?', CONCAT('yes', CHAR(0), 'of course', CHAR(0))),
       ('fillInTheBlank', 2, CONCAT('This is', char(0), 'question', char(0)), CONCAT('fill in the blank', char(0))),
       ('pictureResponse', 3, CONCAT('Hwo is on the picture?', char(0),
                                     'https://images.ctfassets.net/1fvlg6xqnm65/38dVtZPZ3GgHDqabOUqPDX/7d9cb3fe57de48b4fec7f2182957c59f/Lewis_Hamilton_Header.png?w=3840&q=80',
                                     CHAR(0)),
        CONCAT('lewis', char(0), 'hamilton', char(0), 'lewis hamilton', char(0))),
       ('multipleChoices', 4, 'Score for this project?',
        CONCAT('120%', char(0), char(0), '90%', char(0), '80%', char(0))),
       ('multipleChoicesWithMultipleAnswers', 5, 'Team contains?',
        CONCAT('niko', char(0), 'akaki', char(0), 'dachi', char(0), 'lasha', char(0), char(0), 'giorgi', char(0))),
       ('multiAnswers', 6, 'Count from 1', CONCAT('1', char(0), '2', char(0), '3', char(0)));

INSERT INTO ANNOUNCEMENTS (TITLE, BODY, WRITER_ID)
VALUES ('New quiz announced', 'Are you ready for the best Quiz?', 1),
       ('I am new admin', 'I am going to add many news.', 2),
       ('Hello guys', 'The best announcement is here. I am writer of it. Please active more people.', 1);
