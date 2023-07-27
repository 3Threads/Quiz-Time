package Types;

import java.util.ArrayList;

public class QuestionTextResponse extends QuestionAbstract {
    public QuestionTextResponse(String questionText, ArrayList<String> answers) {
        super(questionText, answers);
    }

    @Override
    public QuestionTypes getType() {
        return QuestionTypes.textResponse;
    }

    @Override
    public boolean checkAnswer(ArrayList<String> userAnswer) {
        if (userAnswer == null || userAnswer.isEmpty()) return false;
        String answer = userAnswer.get(0);
        return getAnswers().contains(answer);
    }

    @Override
    public String getWriteQuizHTML(ArrayList<String>[] answers, int questionInd) {
        String html = "<div class='mb-3'>" + getQuestionText() + "</div>";
        html += "<input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer'\n" +
                "                       aria-label='Input' name='answer'";
        if (answers[questionInd] != null && !answers[questionInd].isEmpty()) {
            html += "value='" + answers[questionInd].get(0) + "'";
        }
        html += ">";
        return html;
    }
}
