package com.yrazlik.loltr.model;

/**
 * Created by yrazlik on 28/03/17.
 */

public class QuizQuestion {

    public static final String WRONG_ANSWER_ON_PURPOSE = "XXX_WRONG_ANSWER_XXX";

    protected String text;
    protected boolean isCorrectAnswer;

    public QuizQuestion(String text, boolean isCorrectAnswer) {
        this.text = text;
        this.isCorrectAnswer = isCorrectAnswer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrectAnswer() {
        return isCorrectAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        isCorrectAnswer = correctAnswer;
    }
}
