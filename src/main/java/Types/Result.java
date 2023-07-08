package Types;

import java.sql.Time;
import java.util.Date;

public class Result {
    private int userId;
    private int quizId;

    private int score;

    private Time spentTime;
    private Date writtenDate;

    public Result(int userId, int quizId, int score, Time spentTime, Date writtenDate) {
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

    public Time getSpentTime() {
        return spentTime;
    }
}
