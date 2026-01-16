package com.example.quizmaster;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Models.Element;
import Models.GameResult;
import Models.Page;
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
        if (qZ == null) {
            throw new IllegalStateException("Quiz game not loaded. Please load a quiz first.");
        }
        if (qZ.getTitle() == null || qZ.getTitle().trim().isEmpty()) {
            throw new IllegalStateException("Quiz title is missing");
        }
        if (qZ.getPages() == null || qZ.getPages().isEmpty()) {
            throw new IllegalStateException("Quiz has no pages");
        }

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
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null");
        }
        if (userAnswer == null) {
            throw new IllegalArgumentException("User answer cannot be null");
        }
        if (element.getCorrectAnswerString() == null) {
            throw new IllegalStateException("Element has no correct answer defined");
        }

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
        if (qZ == null) {
            throw new IllegalStateException("Quiz game not loaded");
        }
        if (qZ.getPages() == null || qZ.getPages().isEmpty()) {
            throw new IllegalStateException("Quiz has no pages");
        }
        if (currentPageNr < 0 || currentPageNr >= qZ.getPages().size()) {
            throw new IndexOutOfBoundsException("Current page number is out of bounds: " + currentPageNr);
        }
        Page currentPage = qZ.getPages().get(currentPageNr);
        if (currentPage == null) {
            throw new IllegalStateException("Page at index " + currentPageNr + " is null");
        }
        int timeLimit = currentPage.getTimeLimit();
        if (timeLimit <= 0) {
            throw new IllegalStateException("Invalid time limit: " + timeLimit);
        }
        return timeLimit;
    }

    public void savePlayerName(String name) {
        playerName = name;
    }

    private Result buildResult() {
        if (qZ == null) {
            throw new IllegalStateException("Quiz game not loaded");
        }
        if (qZ.getTitle() == null || qZ.getTitle().trim().isEmpty()) {
            throw new IllegalStateException("Quiz title is missing");
        }
        if (qZ.getPages() == null) {
            throw new IllegalStateException("Quiz pages are null");
        }

        GameResult gameResult = new GameResult();
        gameResult.setCorrectQuestions(score.get());
        gameResult.setPlayerName(playerName != null ? playerName : "Unknown");
        gameResult.setTotalQuestions(qZ.getPages().size());
        gameResult.setDateTime(LocalDateTime.now());

        Result newResult = new Result();
        newResult.setQuizName(qZ.getTitle());
        newResult.setGameResults(new ArrayList<>(List.of(gameResult)));

        return newResult;
    }

    public List<GameResult> readResultsFromFile() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Path path = Paths.get("results.json");

        try {
            if (!Files.exists(path)) {
                return new ArrayList<>();
            }

            // Check if file is empty
            if (Files.size(path) == 0) {
                return new ArrayList<>();
            }

            List<Result> results;
            try {
                results = mapper.readValue(
                        path.toFile(),
                        new TypeReference<List<Result>>() {
                }
                );
            } catch (com.fasterxml.jackson.core.JsonParseException e) {
                throw new RuntimeException("Failed to parse results.json: Invalid JSON format - " + e.getMessage(), e);
            } catch (com.fasterxml.jackson.databind.JsonMappingException e) {
                throw new RuntimeException("Failed to parse results.json: JSON mapping error - " + e.getMessage(), e);
            }

            if (results == null) {
                return new ArrayList<>();
            }

            return results.stream()
                    .filter(r -> r != null && r.getGameResults() != null)
                    .flatMap(r -> r.getGameResults().stream())
                    .filter(gr -> gr != null)
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException("Failed to read results.json: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            throw e; // Re-throw our custom runtime exceptions
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error reading results.json: " + e.getMessage(), e);
        }
    }

    public void appendResultToFile() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        Path path = Paths.get("results.json");

        try {
            List<Result> results;

            if (Files.exists(path)) {
                // Check if file is empty
                if (Files.size(path) == 0) {
                    results = new ArrayList<>();
                } else {
                    try {
                        results = mapper.readValue(
                                path.toFile(),
                                new TypeReference<List<Result>>() {
                        }
                        );
                        if (results == null) {
                            results = new ArrayList<>();
                        }
                    } catch (com.fasterxml.jackson.core.JsonParseException | com.fasterxml.jackson.databind.JsonMappingException e) {
                        // If file is corrupt, start fresh
                        results = new ArrayList<>();
                    }
                }
            } else {
                results = new ArrayList<>();
            }

            Result newResult = buildResult();

            if (newResult == null) {
                throw new IllegalStateException("Failed to build result");
            }
            if (newResult.getQuizName() == null || newResult.getQuizName().trim().isEmpty()) {
                throw new IllegalStateException("Result quiz name is missing");
            }
            if (newResult.getGameResults() == null || newResult.getGameResults().isEmpty()) {
                throw new IllegalStateException("Result has no game results");
            }

            Result existing = results.stream()
                    .filter(r -> r != null && r.getQuizName() != null && r.getQuizName().equals(newResult.getQuizName()))
                    .findFirst()
                    .orElse(null);

            if (existing != null) {
                if (existing.getGameResults() == null) {
                    existing.setGameResults(new ArrayList<>());
                }
                existing.getGameResults().addAll(newResult.getGameResults());
            } else {
                results.add(newResult);
            }

            mapper.writeValue(path.toFile(), results);

        } catch (IOException e) {
            throw new RuntimeException("Failed to write results.json: " + e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw e; // Re-throw our custom exceptions
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error writing results.json: " + e.getMessage(), e);
        }
    }

}
