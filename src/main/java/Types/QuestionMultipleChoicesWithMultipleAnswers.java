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

    private ArrayList<String> getIncorrectAnswers(ArrayList<String> allAnswers) {
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
        String res = ListToString.generateString(getAnswers());
        String res1 = res + (char) 0;
        String res2 = ListToString.generateString(incorrectAnswers);
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

    @Override
    public String getWriteQuizHTML(ArrayList<String>[] answers, int questionInd) {
        String html = "<div class='mb-3'>" + getQuestionText() + "</div>";
        for (String allAnswer : allAnswers) {
            html += "<div class='uk-form-controls uk-form-controls-text'>" +
                    "                    <label class='uk-margin-small'>" +
                    "                        <input class='uk-checkbox' type='checkbox' name='answer' value='" + allAnswer + "'";
            if (answers[questionInd] != null && !answers[questionInd].isEmpty() && answers[questionInd].contains(allAnswer))
                html += "checked";
            html += "> ";

            html += allAnswer;
            html += "</label></div>";
        }
        return html;
    }
}
