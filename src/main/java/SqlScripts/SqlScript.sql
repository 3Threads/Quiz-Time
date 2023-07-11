CREATE DATABASE IF NOT EXISTS QUIZ_WEBSITE;
USE QUIZ_WEBSITE;
DROP TABLE IF EXISTS FRIENDS;
DROP TABLE IF EXISTS COMPLETED_QUIZZES;
DROP TABLE IF EXISTS CHALLENGES;
DROP TABLE IF EXISTS MESSAGES;
DROP TABLE IF EXISTS QUIZZES;
DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS
(
    ID       int primary key NOT NULL AUTO_INCREMENT,
    USERNAME CHAR(64) UNIQUE not null,
    PASSWORD CHAR(64)        not null
);

INSERT INTO USERS (USERNAME, PASSWORD)
VALUES ('USER1', 'USER1'),
       ('USER2', 'USER2'),
       ('USER3', 'USER3'),
       ('USER4', 'USER4'),
       ('USER5', 'USER5');
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

INSERT INTO FRIENDS (USER1_ID, USER2_ID, ACCEPTED)
VALUES (1, 2, 1),
       (2, 3, 0),
       (4, 5, 1),
       (5, 3, 0);

CREATE TABLE QUIZZES
(
    ID            int primary key NOT NULL AUTO_INCREMENT,
    QUIZ_NAME     CHAR(64) UNIQUE not null,
    DESCRIPTION   TEXT,
    COMPLETED     INT      default 0,
    CREATION_TIME DATETIME default current_timestamp,
    CREATOR_ID    INT             not null,
    QUESTIONS     CHAR(64),
    FOREIGN KEY (CREATOR_ID) REFERENCES USERS (ID) ON DELETE CASCADE
);
# INSERT INTO QUIZZES (QUIZ_NAME, DESCRIPTION, COMPLETED, CREATION_TIME, CREATOR_ID, QUESTIONS)
# VALUES ('quiz1', 'QUIZ', 0, default, 1, '1, 2, 3'),
#        ('quiz2', 'QUIZ', 2, default, 1, '1'),
#        ('quiz3', 'QUIZ', 2, default, 2, '2'),
#        ('quiz4', 'QUIZ', 0, default, 3, '3'),
#        ('quiz5', 'QUIZ', 1, default, 4, '4'),
#        ('quiz6', 'QUIZ', 1, default, 5, '5');

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
INSERT INTO COMPLETED_QUIZZES (USER_ID, QUIZ_ID, SCORE, SPENT_TIME, WRITE_TIME)
VALUES (1, 3, 10, 17, default),
       (2, 3, 10, 21, default),
       (2, 5, 1, 100, default),
       (2, 6, 0, 0, default),
       (4, 2, 20, 10, default),
       (4, 2, 22, 8, default);



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
INSERT INTO CHALLENGES (USER1_ID, USER2_ID, QUIZ_ID)
VALUES (1, 2, 1),
       (1, 3, 1),
       (1, 4, 1),
       (1, 5, 2),
       (2, 1, 3),
       (3, 5, 5);

CREATE TABLE MESSAGES
(
    ID        int primary key NOT NULL AUTO_INCREMENT,
    USER1_ID  int             not null,
    USER2_ID  int             not null,
    MESSAGE   TEXT,
    SEND_DATE DATETIME default current_timestamp,
    FOREIGN KEY (USER1_ID) REFERENCES USERS (ID) ON DELETE CASCADE,
    FOREIGN KEY (USER2_ID) REFERENCES USERS (ID) ON DELETE CASCADE
);
INSERT INTO MESSAGES (USER1_ID, USER2_ID, MESSAGE)
VALUES (1, 2, 'hey'),
       (2, 1, 'Hello'),
       (5, 3, 'Accept my friend request'),
       (3, 5, 'NO!'),
       (3, 4, 'Done inserting!');