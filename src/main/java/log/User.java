package log;

public class User {
    private final int id;
    private final String username;
    public User(String username, int id) {
        this.username = username;
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public int getId() {
        return id;
    }
}
