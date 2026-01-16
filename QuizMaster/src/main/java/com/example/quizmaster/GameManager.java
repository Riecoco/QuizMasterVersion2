package com.example.quizmaster;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Models.Element;
import Models.GameResult;
import Models.QuizGame;
import Models.Result;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class GameManager {

    private static volatile GameManager instance;
    private QuizGame qZ;
    private String playerName = "";
    private Result result;
    private final List<GameResult> gameResults = new ArrayList<>();
    private int currentPageNr = 0;
    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final IntegerProperty timeLeft = new SimpleIntegerProperty(0);

    public GameManager() {
    }

    public static GameManager getInstance() {
        GameManager gm = instance;
        if (gm == null) {
            synchronized (GameManager.class) {
                gm = new GameManager();
            }
        }
        return gm;
    }

    public void startNewQuiz() {
        result = new Result();
        result.setQuizName(qZ.getTitle());

        gameResults.clear();
        score.set(0);
        currentPageNr = 0;
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

    public void savePlayerName(String name) {
        playerName = name;
    }

    private Result buildResult() {
        GameResult gameResult = new GameResult();
        gameResult.setCorrectQuestions(score.get());
        gameResult.setPlayerName(playerName);
        gameResult.setTotalQuestions(qZ.getPages().size());
        gameResult.setDateTime(LocalDateTime.now());

        Result result = new Result();
        result.setQuizName(qZ.getTitle());
        result.setGameResults(new ArrayList<>(List.of(gameResult)));

        return result;
    }


    public List<GameResult> readResultsFromFile() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Path path = Paths.get("results.json");

        try {
            if (!Files.exists(path)) {
                return List.of();
            }

            List<Result> results = mapper.readValue(
                    path.toFile(),
                    new TypeReference<List<Result>>() {}
            );

            return results.stream()
                    .flatMap(r -> r.getGameResults().stream())
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void appendResultToFile() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Path path = Paths.get("results.json");

        try {
            List<Result> results;

            if (Files.exists(path)) {
                results = mapper.readValue(
                        path.toFile(),
                        new TypeReference<List<Result>>() {}
                );
            } else {
                results = new ArrayList<>();
            }

            Result newResult = buildResult();

            Result existing = results.stream()
                    .filter(r -> r.getQuizName().equals(newResult.getQuizName()))
                    .findFirst()
                    .orElse(null);

            if (existing != null) {
                existing.getGameResults()
                        .addAll(newResult.getGameResults());
            } else {
                results.add(newResult);
            }

            mapper.writeValue(path.toFile(), results);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
