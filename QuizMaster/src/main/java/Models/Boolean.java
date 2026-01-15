package Models;

public class Boolean extends Element{
    public String labelTrue = "True";
    public String labelFalse = "False";
    public boolean correctAnswer;

    public Boolean() {}
    public Boolean(String name, Type type, String title, boolean isRequired, String labelTrue, String labelFalse, boolean correctAnswer) {
        super(name, type, title, isRequired);
        this.labelTrue = labelTrue;
        this.labelFalse = labelFalse;
        this.correctAnswer = correctAnswer;
    }
    @Override
    public String getCorrectAnswerString() {
        return correctAnswer ? labelTrue : labelFalse;
    }

    public boolean isCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}