package com.example.quizmaster;

import Models.Result;
import Models.QuizGame;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private List<QuizGame> quizGames;
    QuizGame qZ;
    Result qD;
    private int currentPageNr = 0;
    private IntegerProperty score = new SimpleIntegerProperty(0);
    private IntegerProperty timeLeft = new SimpleIntegerProperty(0);

    public GameManager() {
    }

    public static GameManager getInstance() {
        GameManager gm = instance;
        if (gm == null){
            synchronized (GameManager.class){
                gm = new GameManager();
            }
        }
        return gm;
    }

    public int getCurrentPageNr() {
        return currentPageNr;
    }

    public void nextPage() {
        currentPageNr++;
    }
    public GameManager() {
        quizGames = new ArrayList<>();
    }

    public void setQuizGame(QuizGame game) {
        qZ = game;
    }
    public QuizGame getQuizGame() {
        return qZ;
    }
    public Result getResult() {
        return qD;
    }

    public void setQuizDetails(Result details) {
        qD = details;
    }

    public void registerAnswer(Element element, String userAnswer) {
        if (element.getCorrectAnswerString().equals(userAnswer)) {
            score.set(score.get() + 1);
            System.out.println("✅ Correct!");
        } else {
            System.out.println("❌ Incorrect!");
        }
    }

    public IntegerProperty getScoreProperty() {
        return score;
    }
    public IntegerProperty getTimeLeftProperty() {
        return timeLeft;
    }
    public void setTimeLeft(int timeLeft) {
        this.timeLeft.set(timeLeft);
    }

    public int getTimeLimit() {
        return qZ.getPages().get(currentPageNr).getTimeLimit();
    }
    

}
