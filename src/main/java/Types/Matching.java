package Types;

import BusinessLogic.ListToString;

import java.util.*;

public class Matching extends QuestionAbstract {
    private final ArrayList<String> firstPart;
    private final ArrayList<String> secondPart;
    private final Map<String, String> answers;


    public Matching(String questionText, String type, Map<String, String> answers) {
        super(type, questionText, null);
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
    public String generateAnswers() {
        ArrayList<String> arr = new ArrayList<>();
        for (Map.Entry<String, String> x : answers.entrySet()) {
            arr.add(x.getKey());
            arr.add(x.getValue());
        }
        ListToString lts = new ListToString();
        return lts.generateString(arr);
    }
    @Override
    public boolean checkAnswer(ArrayList<String> userAnswer) {
        if(userAnswer == null || userAnswer.isEmpty()) return false;
        System.out.println(userAnswer.toString());
        System.out.println(firstPart.toString());
        System.out.println(secondPart.toString());
        for(int i = 0; i < secondPart.size(); i++) {
            System.out.println("i=" + i);
            int ind = Integer.parseInt(userAnswer.get(i));
            if(!answers.get(firstPart.get(ind-1)).equals(secondPart.get(i))) return false;
        }
        return true;
    }
}