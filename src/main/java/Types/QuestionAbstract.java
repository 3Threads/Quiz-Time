package Types;

import java.util.ArrayList;

public abstract class QuestionAbstract implements Question {
    private final String questionText;
    private final ArrayList<String> answers;
    private final String type;

    public QuestionAbstract(String type, String questionText, ArrayList<String> answers) {
        this.questionText = questionText;
        this.answers = answers;
        this.type = type;
    }
    public String getType(){
        return type;
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
