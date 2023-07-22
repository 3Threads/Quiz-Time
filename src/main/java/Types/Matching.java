package Types;

import java.util.Map;

public class Matching extends QuestionAbstract {
    String questionText;
    Map<String, String> answers;

    public Matching(String questionText, String type, Map<String, String> answers) {
        super(questionText, type, null);
        this.questionText = questionText;
        this.answers = answers;
    }

    public Map<String, String> getMatches(){
        return answers;
    }

}