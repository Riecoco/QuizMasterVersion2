package com.example.quizmaster;

import Models.Result;
import Models.QuizGame;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private List<QuizGame> quizGames;
    QuizGame qZ;
    Result qD;
    private int currentPageNr = 0;

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
}
