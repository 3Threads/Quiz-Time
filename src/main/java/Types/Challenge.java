package Types;

public class Challenge {
    private int userId;
    private int quizId;

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
