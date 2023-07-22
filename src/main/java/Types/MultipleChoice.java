package Types;

import java.util.ArrayList;

public class MultipleChoice extends QuestionAbstract {
    ArrayList<String> allAnswers;

    public MultipleChoice(String questionText, String type, ArrayList<String> answers, ArrayList<String> allAnswers) {
        super(questionText, type, answers);
        this.allAnswers = allAnswers;
    }

    public ArrayList<String> getAllAnswers() {
        return allAnswers;
    }
}
