package Types;

import java.util.ArrayList;

public class QuestionPictureResponse extends QuestionAbstract {
    String url;

    public QuestionPictureResponse(String questionText, String url, ArrayList<String> answers) {
        super(questionText, answers);
        this.url = url;
    }

    public String getPictureUrl() {
        return url;
    }

    @Override
    public QuestionTypes getType() {
        return QuestionTypes.pictureResponse;
    }

    @Override
    public String generateQuestionText() {
        return getQuestionText() + (char) 0 + getPictureUrl() + (char) 0;
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
        html += "<img src='" + getPictureUrl() + "' alt='image' class='mb-3'>";
        html += "<input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer'\n" +
                "                       aria-label='Input' name='answer'";
        if (answers[questionInd] != null && !answers[questionInd].isEmpty()) {
            html += "value='" + answers[questionInd].get(0) + "'";
        }
        html += ">";
        return html;
    }
}
