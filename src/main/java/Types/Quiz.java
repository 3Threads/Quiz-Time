package Types;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Quiz {

    private final Time timeLimit;
    private final String quizName;
    private final String quizDescription;
    private final Date creationTime;
    private final int completed;
    private final int creatorID;

    private final int quizId;
    private final ArrayList<String> categories;

    public Quiz(Time timeLimit, int quizId, String quizName,
                String quizDescription, Date creationTime, int creatorID, int completed, String categories) {
        this.categories = new ArrayList<>(List.of(categories.split(",")));
        this.timeLimit = timeLimit;
        this.quizId = quizId;
        this.quizName = quizName;
        this.quizDescription = quizDescription;
        this.creationTime = creationTime;
        this.creatorID = creatorID;
        this.completed = completed;

    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public int getCompleted() {
        return completed;
    }

    public int getQuizId() { return quizId; }

    public String getQuizName() {
        return  quizName;
    }

    public String getQuizDescription() {
        return quizDescription;
    }


    public Date getCreationTime() {
        return creationTime;
    }

    public int getCreatorID() {
        return creatorID;
    }

    public Time getTimeLimit() {
        return timeLimit;
    }
}
