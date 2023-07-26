package Types;

import BusinessLogic.ListToString;

import java.util.*;

public class QuestionMatching extends QuestionAbstract {
    private final ArrayList<String> firstPart;
    private final ArrayList<String> secondPart;
    private final Map<String, String> answers;


    public QuestionMatching(String questionText, Map<String, String> answers) {
        super(questionText, null);
        this.answers = answers;
        this.firstPart = new ArrayList<>(answers.keySet());
        this.secondPart = new ArrayList<>(answers.values());
        Collections.shuffle(firstPart);
        Collections.shuffle(secondPart);
    }

    public Map<String, String> getMatches() {
        return answers;
    }

    public ArrayList<String> getFirstPart() {
        return firstPart;
    }

    public ArrayList<String> getSecondPart() {
        return secondPart;
    }

    @Override
    public ArrayList<String> getAnswers() {
        ArrayList<String> arr = new ArrayList<>();
        for (Map.Entry<String, String> x : answers.entrySet()) {
            arr.add(x.getKey());
            arr.add(x.getValue());
        }
        return arr;
    }

    @Override
    public QuestionTypes getType() {
        return QuestionTypes.matching;
    }

    @Override
    public String generateAnswers() {
        ArrayList<String> arr = getAnswers();
        ListToString lts = new ListToString();
        return lts.generateString(arr);
    }

    @Override
    public boolean checkAnswer(ArrayList<String> userAnswer) {
        if (userAnswer == null || userAnswer.isEmpty()) return false;
        for (int i = 0; i < secondPart.size(); i++) {
            try {
                int ind = Integer.parseInt(userAnswer.get(i));
                if (!answers.get(firstPart.get(ind - 1)).equals(secondPart.get(i))) return false;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
}