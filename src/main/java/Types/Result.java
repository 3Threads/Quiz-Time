package Types;

import java.util.Date;

public class Result {
    private final int userId;
    private final int quizId;

    private final int score;

    private final long spentTime;
    private final Date writtenDate;

    public Result(int userId, int quizId, int score, long spentTime, Date writtenDate) {
        this.userId = userId;
        this.quizId = quizId;
        this.score = score;
        this.spentTime = spentTime;
        this.writtenDate = writtenDate;
    }

    public int getUserId() {
        return userId;
    }

    public int getQuizId() {
        return quizId;
    }

    public int getScore() {
        return score;
    }

    public Date getWrittenDate() {
        return writtenDate;
    }

    public long getSpentTime() {
        return spentTime;
    }
}
