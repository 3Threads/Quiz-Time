package Types;

public class User {
    private final int id;

    private final int rank;
    private final String username;
    private final int status;

    public User(int id, int rank, String username, int status) {
        this.rank = rank;
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
    public int getRank() {return rank;}
}
