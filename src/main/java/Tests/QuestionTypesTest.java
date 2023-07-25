package Tests;

import Types.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

public class QuestionTypesTest {

    private static final String TYPE1 = "questionResponse";
    private static final String TYPE2 = "fillInTheBlank";
    private static final String TYPE3 = "pictureResponse";
    private static final String TYPE4 = "multiAnswer";
    private static final String TYPE5 = "matching";
    private static final String TYPE6 = "multipleChoice";

    @Test
    public void testQuestionResponse() {
        String q1Text = "W is Lebron's height?";
        ArrayList<String> answers1 = new ArrayList<>(List.of(new String[]{"2.06 m", "206 cm"}));
        String generatedAnswers1 = "2.06 m" + (char) 0 + "206 cm" + (char) 0;

        assertQuestionResponse(q1Text, answers1, generatedAnswers1);

        String q2Text = "Who is the goat?";
        ArrayList<String> answers2 = new ArrayList<>(List.of(new String[]{"machika", "nikoloz machavariani", "Nikoloz Machavariani"}));
        String generatedAnswers2 = "machika" + (char) 0 + "nikoloz machavariani" + (char) 0 + "Nikoloz Machavariani" + (char) 0;

        assertQuestionResponse(q2Text, answers2, generatedAnswers2);
    }

    private void assertQuestionResponse(String q1Text, ArrayList<String> answers1, String generatedAnswers1) {
        Question q1 = new QuestionResponse(q1Text, TYPE1, answers1);
        assertEquals(TYPE1, q1.getType());
        assertEquals(q1Text, q1.getQuestionText());
        assertEquals(q1Text, q1.generateQuestionText());
        assertEquals(generatedAnswers1, q1.generateAnswers());
        assertEquals(answers1, q1.getAnswers());
    }

    @Test
    public void testFillInTheBlank() {
        String q1Text1 = "niko is the";
        String q1Text2 = "programmer";
        ArrayList<String> answers1 = new ArrayList<>(List.of(new String[]{"coolest", "greatest", "best"}));
        String generatedAnswers1 = "coolest" + (char) 0 + "greatest" + (char) 0 + "best" + (char) 0;
        String generatedQuestions1 = "niko is the" + (char) 0 + "programmer" + (char) 0;

        assertFillInTheBlank(q1Text1, q1Text2, answers1, generatedAnswers1, generatedQuestions1);


        String q2Text1 = "Fill";
        String q2Text2 = "the blank";
        ArrayList<String> answers2 = new ArrayList<>(List.of(new String[]{"in"}));
        String generatedAnswers2 = "in" + (char) 0;
        String generatedQuestions2 = "Fill" + (char) 0 + "the blank" + (char) 0;

        assertFillInTheBlank(q2Text1, q2Text2, answers2, generatedAnswers2, generatedQuestions2);
    }

    private void assertFillInTheBlank(String q2Text1, String q2Text2, ArrayList<String> answers2, String generatedAnswers2, String generatedQuestions2) {
        Question q2 = new FillInTheBlank(q2Text1, q2Text2, TYPE2, answers2);
        assertEquals(TYPE2, q2.getType());
        assertEquals(q2Text1, q2.getQuestionText());
        assertEquals(q2Text2, ((FillInTheBlank) q2).getQuestionText2());
        assertEquals(generatedQuestions2, q2.generateQuestionText());
        assertEquals(generatedAnswers2, q2.generateAnswers());
        assertEquals(answers2, q2.getAnswers());
    }

    @Test
    public void testsMultipleChoice() {
        String q1Text = "What is on the photo?";
        String url1 = "https://tinyurl.com/doggo123321";
        ArrayList<String> answers1 = new ArrayList<>(List.of(new String[]{"Dog", "dog", "dzagli", "ძაღლი"}));
        String generatedAnswers1 = "Dog" + (char) 0 + "dog" + (char) 0 + "dzagli" + (char) 0 + "ძაღლი" + (char) 0;
        String generatedQuestion1 = q1Text + (char) 0 + url1 + (char) 0;

        Question q1 = new PictureResponse(q1Text, TYPE3, url1, answers1);
        assertEquals(TYPE3, q1.getType());
        assertEquals(q1Text, q1.getQuestionText());
        assertEquals(generatedQuestion1, q1.generateQuestionText());
        assertEquals(generatedAnswers1, q1.generateAnswers());
        assertEquals(answers1, q1.getAnswers());
        assertEquals(url1, ((PictureResponse) q1).getPictureUrl());
    }

    @Test
    public void testMultiAnswer() {
        String q1Text = "Top 3 ufc goat";
        ArrayList<String> answers1 = new ArrayList<>(List.of(new String[]{"Jon Jones", "Volkanovski", "Chael Sonnen"}));
        ArrayList<String> answersFalse1 = new ArrayList<>(List.of(new String[]{"Volkanovski", "Jon Jones", "Chael Sonnen"}));
        String generatedAnswers1 = "Jon Jones" + (char) 0 + "Volkanovski" + (char) 0 + "Chael Sonnen" + (char) 0;

        Question q1 = new MultiAnswer(q1Text, TYPE4, answers1);
        assertEquals(TYPE4, q1.getType());
        assertEquals(q1Text, q1.getQuestionText());
        assertEquals(q1Text, q1.generateQuestionText());
        assertEquals(generatedAnswers1, q1.generateAnswers());
        assertEquals(answers1, q1.getAnswers());
        assertFalse(answersFalse1.equals(q1.getAnswers()));
    }

    @Test
    public void testMultipleChoice() {
        String q1Text = "Is Lasha meloti?";
        ArrayList<String> allAnswers1 = new ArrayList<>(List.of(new String[]{"yes", "unfortunately", "no"}));
        ArrayList<String> correctAnswers1 = new ArrayList<>(List.of(new String[]{"yes", "unfortunately"}));
        String generatedAnswers1 = "yes" + (char) 0 + "unfortunately" + (char) 0 + (char) 0 + "no" + (char) 0;

        assertMultipleChoice(q1Text, allAnswers1, correctAnswers1, generatedAnswers1);


        String q2Text = "Is Lasha meloti?";
        ArrayList<String> allAnswers2 = new ArrayList<>(List.of(new String[]{"yes", "unfortunately", "no", "Yes", "YES", "Unfortunately", "NO"}));
        ArrayList<String> correctAnswers2 = new ArrayList<>(List.of(new String[]{"yes", "unfortunately", "Yes", "YES", "Unfortunately"}));
        String generatedAnswers2 = "yes" + (char) 0 + "unfortunately" + (char) 0 + "Yes" + (char) 0 + "YES" + (char) 0 + "Unfortunately" + (char) 0 + (char) 0 + "no" + (char) 0 + "NO" + (char) 0;

        assertMultipleChoice(q2Text, allAnswers2, correctAnswers2, generatedAnswers2);
    }

    private void assertMultipleChoice(String q1Text, ArrayList<String> allAnswers1, ArrayList<String> correctAnswers1, String generatedAnswers1) {
        Question q1 = new MultipleChoice(q1Text, TYPE6, correctAnswers1, allAnswers1);
        assertEquals(TYPE6, q1.getType());
        assertEquals(q1Text, q1.getQuestionText());
        assertEquals(q1Text, q1.generateQuestionText());
        assertEquals(generatedAnswers1, q1.generateAnswers());
        assertEquals(correctAnswers1, q1.getAnswers());
    }

    @Test
    public void testMatching() {
        String qText = "Connect names with correct last names:";
        Map<String, String> answers = new HashMap<>();
        String q1 = "niko";
        String a1 = "khetsuriani";
        String q2 = "akaki";
        String a2 = "gurgenidze";
        String q3 = "lasha";
        String a3 = "kuprashvili";
        String q4 = "dachuti";
        String a4 = "goshadze";
        answers.put(q1, a1);
        answers.put(q2, a2);
        answers.put(q3, a3);
        answers.put(q4, a4);

        Question q = new Matching(qText, TYPE5, answers);

        ArrayList<String> resSplit = new ArrayList<>(List.of(new String[]{q1,a1,q2,a2,q3,a3,q4,a4}));
        Collections.sort(resSplit);

        String generated = q.generateAnswers();
        String ch = String.valueOf(generated.charAt(generated.length() - 1));
        ArrayList<String> actualSplit = new ArrayList<>(List.of(generated.split(ch)));
        Collections.sort(actualSplit);

        assertEquals(qText, q.getQuestionText());
        assertEquals(TYPE5, q.getType());
        assertEquals(qText, q.generateQuestionText());
        assertEquals(answers, ((Matching) q).getMatches());
        assertEquals(resSplit, actualSplit);
    }

}

