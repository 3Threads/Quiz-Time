package Types;

import BusinessLogic.ListToString;

import java.util.ArrayList;
import java.util.Collections;

public class QuestionMultipleChoices extends QuestionAbstract {
    private final ArrayList<String> incorrectAnswers;
    private final ArrayList<String> allAnswers;

    public QuestionMultipleChoices(String questionText, ArrayList<String> answers, ArrayList<String> allAnswers) {
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
        return QuestionTypes.multipleChoices;
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
        String answer = userAnswer.get(0);
        return answer.equals(getAnswers().get(0));
    }

    @Override
    public String getWriteQuizHTML(ArrayList<String>[] answers, int questionInd) {
        String html = "<div class='mb-3'>" + getQuestionText() + "</div>";
        for (String allAnswer : allAnswers) {
            html += "<div class='uk-form-controls uk-form-controls-text'>" +
                    "                    <label class='uk-margin-small'>" +
                    "                        <input class='uk-radio' type='radio' name='answer' value='" + allAnswer + "'";
            if (answers[questionInd] != null && !answers[questionInd].isEmpty() && answers[questionInd].contains(allAnswer))
                html += "checked";
            html += ">";

            html += allAnswer;
            html += "</label></div>";
        }
        return html;
    }
}
