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
}
