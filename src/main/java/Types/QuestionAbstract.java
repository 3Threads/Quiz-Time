package Types;

import java.util.ArrayList;

public abstract class QuestionAbstract implements Question {
    private final String questionText;
    private final ArrayList<String> answers;

    public QuestionAbstract(String questionText, ArrayList<String> answers) {
        this.questionText = questionText;
        this.answers = answers;
    }

    @Override
    public ArrayList<String> getAnswers() {
        return answers;
    }

    @Override
    public String getQuestionText() {
        return questionText;
    }
}
