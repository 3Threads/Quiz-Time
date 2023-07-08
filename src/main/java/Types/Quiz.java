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

    private  ArrayList<String> questionsID;
    public Quiz(String quizName, String quizDescription, Date creationTime, int creatorID, String questions, int completed) {
        this.quizName = quizName;
        this.quizDescription = quizDescription;
        this.creationTime = creationTime;
        this.creatorID = creatorID;
        this.completed = completed;
        String[] questionsIDs = questions.split(",");
        questionsID = new ArrayList<>(Arrays.asList(questionsIDs));
    }
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
