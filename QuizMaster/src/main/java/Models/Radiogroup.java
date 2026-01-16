package Models;

import java.util.ArrayList;
import java.util.List;

public class Radiogroup extends Element {
    public String choicesOrder;
    public List<String> choices = new ArrayList<>();
    public String correctAnswer;

    public Radiogroup() {
    }

    public Radiogroup(String name, Type type, String title, boolean isRequired, String choicesOrder, List<String> choices, String correctAnswer) {
        super(name, type, title, isRequired);
        this.choicesOrder = choicesOrder;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String getCorrectAnswerString() {
        return correctAnswer;
    }

    public String getChoicesOrder() {
        return choicesOrder;
    }

    public void setChoicesOrder(String choicesOrder) {
        this.choicesOrder = choicesOrder;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }
}
