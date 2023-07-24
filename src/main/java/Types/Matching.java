package Types;

import java.util.Map;

public class Matching extends QuestionAbstract {
    Map<String, String> answers;

    public Matching(String questionText, String type, Map<String, String> answers) {
        super(type, questionText,null);
        this.answers = answers;
    }

    public Map<String, String> getMatches(){
        return answers;
    }

}