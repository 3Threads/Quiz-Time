package Types;

import java.util.ArrayList;

public class MultiAnswer extends QuestionAbstract {
    public MultiAnswer(String questionText, String type, ArrayList<String> answers) {
        super(type, questionText, answers);
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
