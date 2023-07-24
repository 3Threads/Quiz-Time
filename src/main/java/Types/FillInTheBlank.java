package Types;

import java.util.ArrayList;

public class FillInTheBlank extends QuestionAbstract {
    private final String questionText2;

    public FillInTheBlank(String questionText1, String questionText2, String type, ArrayList<String> answers) {
        super(type, questionText1, answers);
        this.questionText2 = questionText2;
    }

    public String getQuestionText2() {
        return questionText2;
    }

    @Override
    public String generateQuestionText() {
        return getQuestionText() + (char) 0 + getQuestionText2() + (char) 0;
    }
}
