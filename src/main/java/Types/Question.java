package Types;

import java.util.ArrayList;

public interface Question {
    ArrayList<String> getAnswers();

    String getQuestionText();

    String getType();

    String generateQuestionText();

    String generateAnswers();
}
