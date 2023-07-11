package Types;

public class FriendInfo {
    private final int user1Id;
    private final int user2Id;
    private final int accepted;

    public FriendInfo(int user1Id, int user2Id, int accepted) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.accepted = accepted;
    }

    public int getAccepted() {
        return accepted;
    }

    public int getUser1Id() {
        return user1Id;
    }

    public int getUser2Id() {
        return user2Id;
    }
}
