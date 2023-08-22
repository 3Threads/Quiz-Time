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
        return ListToString.generateString(arr);
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

    @Override
    public String getWriteQuizHTML(ArrayList<String>[] answers, int questionInd) {
        String html = "<div class='mb-3'>" + getQuestionText() + "</div>";
        html += "<div class='row'>" +
                "                    <div class='col-6'>";
        int i = 0;
        for (String key : firstPart) {
            i++;

            html += "                        <div class='mt-1'>" +
                    "                            " + i + "). " + key +
                    "                        </div>";
        }
        html += "                    </div>" +
                "                    <div class='col-6'>";
        i = 0;
        for (String val : secondPart) {
            html += "                        <div class='row mt-1'>" +
                    "                            <input class='form-control bg-dark whitePlaceholder text-light mb-1 mt-1 col-2'" +
                    "            type='number'" +
                    "            placeholder='#'" +
                    "            aria-label='Input' name='answer'" +
                    "            style='width: 40px; height: 40px'";
            if (answers[questionInd] != null && !answers[questionInd].isEmpty() && i < answers[questionInd].size())
                html += "value='" + answers[questionInd].get(i) + "'";
            html += " max="+secondPart.size()+" min=1                           ><div class='col-10 d-flex align-items-center'>" + val + "</div>" +
                    "                        </div>";
            i++;
        }
        html += "                    </div>" +
                "                </div>";

        return html;
    }
}