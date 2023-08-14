package Types;

public class Challenge {
    private final int userId;
    private final int quizId;

    public Challenge(int userId, int quizId) {
        this.userId = userId;
        this.quizId = quizId;
    }

    public int getQuizId() {
        return quizId;
    }

    public int getUserId() {
        return userId;
    }
}
