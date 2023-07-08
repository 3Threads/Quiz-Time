package Types;

public class Challange {
    private int userId;
    private int quizId;

    public Challange(int userId, int quizId) {
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
