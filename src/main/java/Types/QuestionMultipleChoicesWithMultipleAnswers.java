package Types;

import BusinessLogic.ListToString;

import java.util.ArrayList;
import java.util.Collections;

public class QuestionMultipleChoicesWithMultipleAnswers extends QuestionAbstract {
    private final ArrayList<String> incorrectAnswers;
    private final ArrayList<String> allAnswers;

    public QuestionMultipleChoicesWithMultipleAnswers(String questionText, ArrayList<String> answers, ArrayList<String> allAnswers) {
        super(questionText, answers);
        Collections.shuffle(allAnswers);
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
    public QuestionTypes getType() {
        return QuestionTypes.multipleChoicesWithMultipleAnswers;
    }

    @Override
    public String generateAnswers() {
        ListToString lts = new ListToString();
        String res = lts.generateString(getAnswers());
        String res1 = res + (char) 0;
        String res2 = lts.generateString(incorrectAnswers);
        return res1 + res2;
    }

    @Override
    public boolean checkAnswer(ArrayList<String> userAnswer) {
        if (userAnswer == null || userAnswer.isEmpty()) return false;
        if (userAnswer.size() != getAnswers().size()) return false;
        for (String us : userAnswer) {
            if (!getAnswers().contains(us)) return false;
        }
        return true;
    }
}
