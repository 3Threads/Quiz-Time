package Types;

import java.util.ArrayList;

public class QuestionResponse extends QuestionAbstract {
    public QuestionResponse(String questionText, String type, ArrayList<String> answers) {
        super(type, questionText, answers);
    }
}
