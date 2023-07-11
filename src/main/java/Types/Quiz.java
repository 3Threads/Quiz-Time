package Types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Quiz {
    private final String quizName;
    private final String quizDescription;
    private final Date creationTime;
    private final int completed;
    private final int creatorID;

    private final int quizId;

    private final ArrayList<String> questionsID;
    public Quiz(int quizId,String quizName, String quizDescription, Date creationTime, int creatorID, String questions, int completed) {
        this.quizId = quizId;
        this.quizName = quizName;
        this.quizDescription = quizDescription;
        this.creationTime = creationTime;
        this.creatorID = creatorID;
        this.completed = completed;
        String[] questionsIDs = questions.split(",");
        questionsID = new ArrayList<>(Arrays.asList(questionsIDs));
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

    public ArrayList<String> getQuestionsID() {
        return questionsID;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public int getCreatorID() {
        return creatorID;
    }
}
