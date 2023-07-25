package Types;

import BusinessLogic.ListToString;

import java.util.ArrayList;
import java.util.Map;

public class Matching extends QuestionAbstract {
    Map<String, String> answers;

    public Matching(String questionText, String type, Map<String, String> answers) {
        super(type, questionText, null);
        this.answers = answers;
    }

    public Map<String, String> getMatches() {
        return answers;
    }

    @Override
    public String generateAnswers() {
        ArrayList<String> arr = new ArrayList<>();
        for (Map.Entry<String, String> x : answers.entrySet()) {
            arr.add(x.getKey());
            arr.add(x.getValue());
        }
        ListToString lts = new ListToString();
        return lts.generateString(arr);
    }
}