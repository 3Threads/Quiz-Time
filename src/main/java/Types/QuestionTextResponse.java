package Types;

import java.util.ArrayList;

public class QuestionTextResponse extends QuestionAbstract {
    public QuestionTextResponse(String questionText, ArrayList<String> answers) {
        super(questionText, answers);
    }

    @Override
    public QuestionTypes getType() {
        return QuestionTypes.textResponse;
    }

    @Override
    public boolean checkAnswer(ArrayList<String> userAnswer) {
        if (userAnswer == null || userAnswer.isEmpty()) return false;
        String answer = userAnswer.get(0);
        return getAnswers().contains(answer);
    }
}
