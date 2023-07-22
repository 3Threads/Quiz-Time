package Types;

import java.util.ArrayList;

public class MultiAnswer extends QuestionAbstract {
    public MultiAnswer(String questionText, String type, ArrayList<String> answers) {
        super(questionText, type, answers);
    }
}
