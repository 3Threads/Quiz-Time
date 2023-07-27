package Types;

public class User {
    private final int id;
    private final String username;
    private final int status;

    public User(int id, String username, int status) {
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
}
