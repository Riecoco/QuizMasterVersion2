package com.example.quizmaster;

import Models.Element;
import Models.Page;
import Models.Result;
import Models.QuizGame;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static volatile GameManager instance;
    private QuizGame qZ;
    private Result qD;
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
        ObjectMapper mapper = createObjectMapper();
        Path path = Paths.get("results.json");

        try {
            if (isFileEmptyOrMissing(path)) {
                return new ArrayList<>();
            }

            List<Result> results = parseResultsFromFile(mapper, path);
            return extractGameResults(results);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read results.json: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            throw e; // Re-throw our custom runtime exceptions
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error reading results.json: " + e.getMessage(), e);
        }
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    private boolean isFileEmptyOrMissing(Path path) throws IOException {
        if (!Files.exists(path)) {
            return true;
        }
        return Files.size(path) == 0;
    }

    private List<Result> parseResultsFromFile(ObjectMapper mapper, Path path) {
        try {
            List<Result> results = mapper.readValue(
                    path.toFile(),
                    new TypeReference<List<Result>>() {
            }
            );
            return results != null ? results : new ArrayList<>();
        } catch (com.fasterxml.jackson.core.JsonParseException e) {
            throw new RuntimeException("Failed to parse results.json: Invalid JSON format - " + e.getMessage(), e);
        } catch (com.fasterxml.jackson.databind.JsonMappingException e) {
            throw new RuntimeException("Failed to parse results.json: JSON mapping error - " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read results.json: " + e.getMessage(), e);
        }
    }

    private List<GameResult> extractGameResults(List<Result> results) {
        return results.stream()
                .filter(r -> r != null && r.getGameResults() != null)
                .flatMap(r -> r.getGameResults().stream())
                .filter(gr -> gr != null)
                .toList();
    }

    public void appendResultToFile() {
        ObjectMapper mapper = createObjectMapperForWriting();
        Path path = Paths.get("results.json");

        try {
            List<Result> results = loadExistingResults(mapper, path);
            Result newResult = buildResult();
            validateNewResult(newResult);
            mergeOrAddResult(results, newResult);
            mapper.writeValue(path.toFile(), results);

        } catch (IOException e) {
            throw new RuntimeException("Failed to write results.json: " + e.getMessage(), e);
        } catch (IllegalStateException e) {
            throw e; // Re-throw our custom exceptions
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error writing results.json: " + e.getMessage(), e);
        }
    }
    

    private ObjectMapper createObjectMapperForWriting() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }

    private List<Result> loadExistingResults(ObjectMapper mapper, Path path) throws IOException {
        if (!Files.exists(path) || Files.size(path) == 0) {
            return new ArrayList<>();
        }

        try {
            List<Result> results = mapper.readValue(
                    path.toFile(),
                    new TypeReference<List<Result>>() {
            }
            );
            return results != null ? results : new ArrayList<>();
        } catch (com.fasterxml.jackson.core.JsonParseException
                | com.fasterxml.jackson.databind.JsonMappingException e) {
            // If file is corrupt, start fresh
            return new ArrayList<>();
        }
    }

    private void validateNewResult(Result newResult) {
        if (newResult == null) {
            throw new IllegalStateException("Failed to build result");
        }
        if (newResult.getQuizName() == null || newResult.getQuizName().trim().isEmpty()) {
            throw new IllegalStateException("Result quiz name is missing");
        }
        if (newResult.getGameResults() == null || newResult.getGameResults().isEmpty()) {
            throw new IllegalStateException("Result has no game results");
        }
    }

    private void mergeOrAddResult(List<Result> results, Result newResult) {
        Result existing = findExistingResult(results, newResult.getQuizName());
        if (existing != null) {
            mergeResults(existing, newResult);
        } else {
            results.add(newResult);
        }
    }

    private Result findExistingResult(List<Result> results, String quizName) {
        return results.stream()
                .filter(r -> r != null && r.getQuizName() != null && r.getQuizName().equals(quizName))
                .findFirst()
                .orElse(null);
    }

    private void mergeResults(Result existing, Result newResult) {
        if (existing.getGameResults() == null) {
            existing.setGameResults(new ArrayList<>());
        }
        existing.getGameResults().addAll(newResult.getGameResults());
    }

}
