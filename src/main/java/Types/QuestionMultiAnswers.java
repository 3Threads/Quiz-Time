package Types;

import java.util.ArrayList;

public class QuestionMultiAnswers extends QuestionAbstract {
    public QuestionMultiAnswers(String questionText, ArrayList<String> answers) {
        super(questionText, answers);
    }

    @Override
    public QuestionTypes getType() {
        return QuestionTypes.multiAnswers;
    }

    @Override
    public boolean checkAnswer(ArrayList<String> userAnswer) {
        if (userAnswer == null || userAnswer.isEmpty()) return false;
        if (userAnswer.size() != getAnswers().size()) return false;
        for (int i = 0; i < getAnswers().size(); i++) {
            if (!getAnswers().get(i).equals(userAnswer.get(i))) return false;
        }
        return true;
    }

    @Override
    public String getWriteQuizHTML(ArrayList<String>[] answers, int questionInd) {
        String html = "<div class='mb-3'>" + getQuestionText() + "</div>";
        for (int i = 0; i < getAnswers().size(); i++) {
            html += "<input class='form-control bg-dark whitePlaceholder text-light mb-3' type='text' placeholder='Answer'\n" +
                    "                       aria-label='Input' name='answer'";
            if (answers[questionInd] != null && !answers[questionInd].isEmpty() && i < answers[questionInd].size()) {
                html += "value='" + answers[questionInd].get(i) + "'";
            }
            html += ">";
        }
        return html;
    }
}
