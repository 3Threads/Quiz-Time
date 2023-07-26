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
    public ArrayList<String> getAnswers(){
        ArrayList<String> arr = new ArrayList<>();
        for (Map.Entry<String, String> x : answers.entrySet()) {
            arr.add(x.getKey());
            arr.add(x.getValue());
        }
        return arr;
    }

    @Override
    public String generateAnswers() {
        ArrayList<String> arr = getAnswers();
        ListToString lts = new ListToString();
        return lts.generateString(arr);
    }
}