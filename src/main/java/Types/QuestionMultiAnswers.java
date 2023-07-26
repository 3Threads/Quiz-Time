package Types;

import java.util.ArrayList;

public class QuestionMultiAnswers extends QuestionAbstract {
    public QuestionMultiAnswers(String questionText, ArrayList<String> answers) {
        super(questionText, answers);
    }

    @Override
    public QuestionTypes getType() {
        return QuestionTypes.multiAnswers;
    }

    @Override
    public boolean checkAnswer(ArrayList<String> userAnswer) {
        if(userAnswer == null || userAnswer.isEmpty()) return false;
        if(userAnswer.size() != getAnswers().size()) return false;
        for(int i = 0; i < getAnswers().size(); i++) {
            if(!getAnswers().get(i).equals(userAnswer.get(i))) return false;
        }
        return true;
    }
}
