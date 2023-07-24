package Types;

import BusinessLogic.ListToString;

import java.util.ArrayList;

public class MultipleChoice extends QuestionAbstract {
    ArrayList<String> incorrectAnswers;

    public MultipleChoice(String questionText, String type, ArrayList<String> answers, ArrayList<String> allAnswers) {
        super(type, questionText, answers);
        incorrectAnswers = getIncorrectAnswers(allAnswers);
    }

    public ArrayList<String> getIncorrectAnswers(ArrayList<String> allAnswers){
        for(int i = 0; i<getAnswers().size(); i++){
            allAnswers.remove(getAnswers().get(i));
        }
        return allAnswers;
    }

    @Override
    public String generateAnswers() {
        ListToString lts = new ListToString();
        String res = lts.generateString(getAnswers());
        String res1 = res + (char) 0;
        String res2 = lts.generateString(incorrectAnswers);
        return res1+res2;
    }
}
