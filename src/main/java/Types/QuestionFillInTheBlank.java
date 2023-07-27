package Types;

import java.util.ArrayList;

public class QuestionFillInTheBlank extends QuestionAbstract {
    private final String questionText2;

    public QuestionFillInTheBlank(String questionText1, String questionText2, ArrayList<String> answers) {
        super(questionText1, answers);
        this.questionText2 = questionText2;
    }

    public String getQuestionText2() {
        return questionText2;
    }

    @Override
    public QuestionTypes getType() {
        return QuestionTypes.fillInTheBlank;
    }

    @Override
    public String generateQuestionText() {
        return getQuestionText() + (char) 0 + getQuestionText2() + (char) 0;
    }

    @Override
    public boolean checkAnswer(ArrayList<String> userAnswer) {
        if (userAnswer == null || userAnswer.isEmpty()) return false;
        String answer = userAnswer.get(0);
        return getAnswers().contains(answer);
    }

    @Override
    public String getWriteQuizHTML(ArrayList<String>[] answers, int questionInd) {
        String html = "<div class='uk-form-controls uk-form-controls-text row'>" +
                "                    <div class='col-auto d-flex align-Items-center'>" + getQuestionText() +
                "                    </div>" +
                "                    <div class='col-auto'>" +
                "                        <input class='form-control bg-dark whitePlaceholder text-light' type='text' placeholder='Answer'" +
                "                               aria-label='Input' name='answer' style='width: 100px'";
        if (answers[questionInd] != null && !answers[questionInd].isEmpty())
            html += " value='" + answers[questionInd].get(0) + "'";
        html += "                    ></div>" +
                "                    <div class='col-auto d-flex align-Items-center'>" + getQuestionText2() +
                "                    </div>" +
                "                </div>";
        return html;
    }

}
