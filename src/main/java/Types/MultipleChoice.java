package Types;

import BusinessLogic.ListToString;

import java.util.ArrayList;

public class MultipleChoice extends QuestionAbstract {
    private final ArrayList<String> incorrectAnswers;
    private final ArrayList<String> allAnswers;

    public MultipleChoice(String questionText, String type, ArrayList<String> answers, ArrayList<String> allAnswers) {
        super(type, questionText, answers);
        this.allAnswers = allAnswers;
        incorrectAnswers = getIncorrectAnswers(allAnswers);
    }

    public ArrayList<String> getAllAnswers() {
        return allAnswers;
    }

    public ArrayList<String> getIncorrectAnswers(ArrayList<String> allAnswers) {
        ArrayList<String> incorrectAnswers = new ArrayList<>(allAnswers);
        for (int i = 0; i < getAnswers().size(); i++) {
            incorrectAnswers.remove(getAnswers().get(i));
        }
        return incorrectAnswers;
    }

    public ArrayList<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    @Override
    public String generateAnswers() {
        ListToString lts = new ListToString();
        String res = lts.generateString(getAnswers());
        String res1 = res + (char) 0;
        String res2 = lts.generateString(incorrectAnswers);
        return res1 + res2;
    }
}
