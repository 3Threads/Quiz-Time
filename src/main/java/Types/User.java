package Types;

public class User {
    private final int id;
    private final String username;

    public User(int id, String username) {
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
