package Types;

public class User {
    private final int id;

    private final int score;
    private final String username;
    private final int status;

    public User(int id, int score, String username, int status) {
        this.score = score;
        this.username = username;
        this.id = id;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public boolean isAdmin() {
        return status != 0;
    }
    public int getScore() {return score;}
}
