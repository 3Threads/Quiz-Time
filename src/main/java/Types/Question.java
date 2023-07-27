package Types;

import java.util.ArrayList;

public interface Question {
    ArrayList<String> getAnswers();

    String getQuestionText();

    QuestionTypes getType();

    String generateQuestionText();

    String generateAnswers();

    boolean checkAnswer(ArrayList<String> userAnswer);

    String getWriteQuizHTML(ArrayList<String>[] answers, int questionInd);
}
