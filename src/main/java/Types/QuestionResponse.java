package Types;

import java.util.ArrayList;

public class QuestionResponse extends QuestionAbstract {
    public QuestionResponse(String questionText, String type, ArrayList<String> answers) {
        super(type, questionText, answers);
    }
    @Override
    public boolean checkAnswer(ArrayList<String> userAnswer) {
        if(userAnswer == null || userAnswer.isEmpty()) return false;
        String answer = userAnswer.get(0);
        return getAnswers().contains(answer);
    }
}
