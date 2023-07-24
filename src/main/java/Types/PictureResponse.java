package Types;

import java.util.ArrayList;

public class PictureResponse extends QuestionAbstract {
    String url;

    public PictureResponse(String questionText, String type, String url, ArrayList<String> answers) {
        super(type, questionText, answers);
        this.url = url;
    }

    public String getPictureUrl() {
        return url;
    }
}
