package com.example.quizmaster;

import Models.Element;
import Models.Page;
import Models.Result;
import Models.QuizGame;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static volatile GameManager instance;
    private QuizGame qZ;
    private Result qD;
    private int currentPageNr = 0;
    private int score = 0;

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
            score++;
            System.out.println("✅ Correct!");
        } else {
            System.out.println("❌ Incorrect!");
        }
    }

    public String getScore(){
        return Integer.toString(score);
    }


}
