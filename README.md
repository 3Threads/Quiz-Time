# Quiz Website

## Requirements

- JDK
- Maven
- Microsoft SQL Server

## Getting Started

1. Run the command in the terminal:

```shell
mysql -u 'Database username' -p
```

2. Enter your mySQL password.

3. Copy the script from SqlScript.sql and run it in the terminal.

4. Create `.env` file with `SQL_USERNAME` set your sql username and
   `SQL_PASSWORD` set you sql password. For example:

```dotenv
SQL_USERNAME="root"
SQL_PASSWORD="password"
```

5. If you are using Intellij IDEA for running the project you can skip following 2 steps and run it by choosing 'Quiz
   Website' for running configuration.

6. All necessary libraries should be installed by calling this command from the root directory:

```shell
mvn install
```

7. After this, you can start the server by calling this command from the root directory:

```shell
mvn tomcat7:run
```

## Testing

1. Run the command in the terminal:

```shell
mysql -u 'Database username' -p
```

2. Enter your mySQL password.

3. Copy the script from SqlScriptTest.sql and run it in the terminal.

4. Create `.env` file with `SQL_USERNAME` set your sql username and
   `SQL_PASSWORD` set you sql password. For example:

```dotenv
SQL_USERNAME="root"
SQL_PASSWORD="password"
```

5. If you are using Intellij IDEA for running the project you can run it by choosing 'Test
   Website' for running configuration.

## Functionality

This web application was developed as a project for CS108 at Free University of Tbilisi,
offering users the opportunity to challenge their friends, compare quiz scores, and even create their own quizzes for
others to take.

### Roles

- **Users** <br/>
  Authorized users have the following privileges:
    - Creating or writing quizzes those that already exist.
    - Rating and leaving comments on quizzes.
    - Adding friends and communicating with them through chat.
    - Challenging their friends by achieving higher scores.
    - get achievements by writing quizzes successfully


- **Admins** <br/>
  In addition to the standard user privileges, admins have the following extra abilities:
    - Removing any user, quiz, or comment from the platform.
    - Making important announcements.
    - Granting/taking admin privileges to/from other users/admins.

### Pages

- **Log in page** <br/>
    - User should log in with existing account or create one.
    - Please note that usernames and passwords are case-sensitive.


- **Header** <br/>
  The header (menu bar) is consistently present across all pages of the website, enhancing ease of navigation and
  coordination throughout the platform. <br/>
  It features four distinct buttons aligned from left to right:
    - "Create Quiz" - directs users to a page where quizzes can be created.
    - "Random Quiz" - leads users to a page offering a randomly generated quiz.
    - "Chats" - provides a link to the chat page for user interactions.
    - "Quizzes" - guides users to a page where they can search for their desired quizzes.
    - Search Field - allows users to search for other users, quizzes, or announcements.

      Positioned above the search field:
    - Username - upon clicking, redirects users to their profile page.
    - Notification Icon - upon clicking, opens a new window displaying friend requests, challenges, or new messages.
      Users can accept or reject friend requests and challenges, and initiate chats by clicking the respective buttons.
      This icon indicates notifications through a shaking motion, persisting until attended to. Getting notifications
      does not need reloading the page.
    - "Log Out" - initiates the logout process when clicked, allowing users to sign out.


- **Home page** <br/>
    - On the left side of the page, users are presented with announcements curated by administrators.
    - Clicking on "Announcements" or "Read more" provides users access to the complete content of each announcement.
    - The right side of the page features four distinct quiz tables, offering users the option to explore and engage
      with their preferred quizzes.


- **Profile page** <br/>
    - Users are presented with tables showcasing quizzes they have created or successfully completed.
    - The page displays the user's score and rank, earned through their quiz completion activities.
    - Users possess the capability to both add and remove friends within their network.
    - Messaging functionality enables users to engage in communication.
    - Within their personal profile, users are empowered to delete their own accounts. (Administrators maintain the
      privilege to delete any user profile.)


- **Create quiz** <br/>
  Users have the ability to create quizzes, incorporating the following options:
    - Title
    - Description
    - Selecting categories (Optional)
    - Set timer (Optional)
    - Inclusion of various question types from a pool of seven available options.


- **Random quiz** <br/>
    - Upon accessing the random quiz page, users are presented with a randomly generated quiz for exploration.
    - There are shown quiz name, average rating, quiz description, comments, 3 types of tables.
    - Each quiz entry showcases details such as quiz name, average rating, quiz description, and user comments. Three
      types of tables are displayed.
    - The creator of the quiz retains the ability to delete it through the 'delete' button.
    - Users can write the quiz or challenge a friend by utilizing the respective buttons.
    - Upon completing the quiz, users are provided with their score and the opportunity to rate and comment.
    - Users have the potential to elevate their rank by creating quizzes and attaining scores, influenced by a specific
      algorithm. Incorrect answers may result in a rank reduction.


- **Chats page** <br/>
    - Users can engage in real-time communication with their friends through the chat feature.
    - The chat functionality ensures seamless interactions without the need for page refreshes.
    - Users can enhance their conversations with the availability of emoticons and emojis.


- **Quizzes page** <br/>
    - Users have the convenience of filtering quizzes based on categories and rating preferences.
    - Quizzes can be effortlessly located by conducting searches using their respective quiz names.

