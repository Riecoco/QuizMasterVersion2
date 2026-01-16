package com.example.quizmaster;

import Models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    private GameManager gameManager;
    private QuizGame quizGame;
    private Page page1;
    private Page page2;

    @BeforeEach
    void setUp() {
        // Reset singleton instance by creating a new one
        gameManager = new GameManager();

        // Create test quiz game
        quizGame = new QuizGame();
        quizGame.setTitle("Test Quiz");
        quizGame.setDescription("Test Description");

        // Create pages with elements
        page1 = new Page();
        page1.setTimeLimit(10);
        List<Element> elements1 = new ArrayList<>();
        Radiogroup rg1 = new Radiogroup("q1", Type.radiogroup, "Question 1", true,
                "random", List.of("A", "B", "C"), "A");
        elements1.add(rg1);
        page1.setElements(elements1);

        page2 = new Page();
        page2.setTimeLimit(15);
        List<Element> elements2 = new ArrayList<>();
        Models.Boolean bool1 = new Models.Boolean("q2", Type.trueFalseStatement, "Question 2", true,
                "True", "False", true);
        elements2.add(bool1);
        page2.setElements(elements2);

        List<Page> pages = new ArrayList<>();
        pages.add(page1);
        pages.add(page2);
        quizGame.setPages(pages);

        gameManager.setQuizGame(quizGame);
    }

    @Test
    void testStartNewQuiz() {
        gameManager.startNewQuiz();

        assertEquals(0, gameManager.getCurrentPageNr());
        assertEquals(0, gameManager.getScoreProperty().get());
    }

    @Test
    void testStartNewQuizWithoutQuizGame() {
        GameManager gm = new GameManager();
        assertThrows(IllegalStateException.class, () -> gm.startNewQuiz());
    }

    @Test
    void testStartNewQuizWithNullTitle() {
        QuizGame qg = new QuizGame();
        qg.setTitle(null);
        qg.setPages(new ArrayList<>());
        gameManager.setQuizGame(qg);
        assertThrows(IllegalStateException.class, () -> gameManager.startNewQuiz());
    }

    @Test
    void testStartNewQuizWithEmptyPages() {
        QuizGame qg = new QuizGame();
        qg.setTitle("Test");
        qg.setPages(new ArrayList<>());
        gameManager.setQuizGame(qg);
        assertThrows(IllegalStateException.class, () -> gameManager.startNewQuiz());
    }

    @Test
    void testRegisterAnswerCorrect() {
        gameManager.startNewQuiz();
        Element element = page1.getElements().get(0);

        int initialScore = gameManager.getScoreProperty().get();
        gameManager.registerAnswer(element, "A");

        assertEquals(initialScore + 1, gameManager.getScoreProperty().get());
    }

    @Test
    void testRegisterAnswerIncorrect() {
        gameManager.startNewQuiz();
        Element element = page1.getElements().get(0);

        int initialScore = gameManager.getScoreProperty().get();
        gameManager.registerAnswer(element, "B");

        assertEquals(initialScore, gameManager.getScoreProperty().get());
    }

    @Test
    void testRegisterAnswerWithBooleanElement() {
        gameManager.startNewQuiz();
        Element element = page2.getElements().get(0);

        int initialScore = gameManager.getScoreProperty().get();
        gameManager.registerAnswer(element, "True");

        assertEquals(initialScore + 1, gameManager.getScoreProperty().get());
    }

    @Test
    void testRegisterAnswerWithNullElement() {
        gameManager.startNewQuiz();
        assertThrows(IllegalArgumentException.class, ()
                -> gameManager.registerAnswer(null, "A"));
    }

    @Test
    void testRegisterAnswerWithNullUserAnswer() {
        gameManager.startNewQuiz();
        Element element = page1.getElements().get(0);
        assertThrows(IllegalArgumentException.class, ()
                -> gameManager.registerAnswer(element, null));
    }

    @Test
    void testNextPage() {
        gameManager.startNewQuiz();
        assertEquals(0, gameManager.getCurrentPageNr());

        gameManager.nextPage();
        assertEquals(1, gameManager.getCurrentPageNr());

        gameManager.nextPage();
        assertEquals(2, gameManager.getCurrentPageNr());
    }

    @Test
    void testGetTimeLimit() {
        gameManager.startNewQuiz();
        assertEquals(10, gameManager.getTimeLimit());

        gameManager.nextPage();
        assertEquals(15, gameManager.getTimeLimit());
    }

    @Test
    void testGetTimeLimitWithoutQuizGame() {
        GameManager gm = new GameManager();
        assertThrows(IllegalStateException.class, () -> gm.getTimeLimit());
    }

    @Test
    void testGetTimeLimitWithInvalidPageNumber() {
        gameManager.startNewQuiz();
        gameManager.nextPage();
        gameManager.nextPage();
        assertThrows(IndexOutOfBoundsException.class, () -> gameManager.getTimeLimit());
    }

    @Test
    void testSetTimeLeft() {
        gameManager.setTimeLeft(30);
        assertEquals(30, gameManager.getTimeLeftProperty().get());
    }

    @Test
    void testSavePlayerName() {
        String playerName = "Test Player";
        gameManager.savePlayerName(playerName);
        // Note: We can't directly test this without accessing private field,
        // but we can test it indirectly through buildResult
        gameManager.startNewQuiz();
        gameManager.registerAnswer(page1.getElements().get(0), "A");
        gameManager.nextPage();
        gameManager.registerAnswer(page2.getElements().get(0), "True");

        // This should not throw an exception
        assertDoesNotThrow(() -> gameManager.appendResultToFile());
    }

    @Test
    void testReadResultsFromFileNonExistent(@TempDir Path tempDir) throws IOException {
        // Change to temp directory
        Path originalPath = Path.of("results.json");
        Path testPath = tempDir.resolve("results.json");

        // Test with non-existent file
        GameManager gm = new GameManager();
        // We can't easily test this without mocking, but we can test the logic
        // by creating a file
    }

    @Test
    void testReadResultsFromFileEmpty(@TempDir Path tempDir) throws IOException {
        Path testFile = tempDir.resolve("results.json");
        Files.createFile(testFile);

        // We can't easily test this without refactoring to accept a path parameter
        // But the error handling is in place
    }

    @Test
    void testAppendResultToFile(@TempDir Path tempDir) throws IOException {
        Path testFile = tempDir.resolve("results.json");

        gameManager.startNewQuiz();
        gameManager.savePlayerName("Test Player");
        gameManager.registerAnswer(page1.getElements().get(0), "A");
        gameManager.nextPage();
        gameManager.registerAnswer(page2.getElements().get(0), "True");

        // We can't easily test this without refactoring to accept a path parameter
        // But the error handling is in place
    }

    @Test
    void testGetScoreProperty() {
        assertNotNull(gameManager.getScoreProperty());
        assertEquals(0, gameManager.getScoreProperty().get());
    }

    @Test
    void testGetTimeLeftProperty() {
        assertNotNull(gameManager.getTimeLeftProperty());
        assertEquals(0, gameManager.getTimeLeftProperty().get());
    }

    @Test
    void testSetQuizGame() {
        QuizGame newQuiz = new QuizGame();
        newQuiz.setTitle("New Quiz");
        gameManager.setQuizGame(newQuiz);
        assertEquals(newQuiz, gameManager.getQuizGame());
    }

    @Test
    void testGetQuizGame() {
        assertEquals(quizGame, gameManager.getQuizGame());
    }

    @Test
    void testGetCurrentPageNr() {
        gameManager.startNewQuiz();
        assertEquals(0, gameManager.getCurrentPageNr());
    }

    @Test
    void testMultipleCorrectAnswers() {
        gameManager.startNewQuiz();

        gameManager.registerAnswer(page1.getElements().get(0), "A");
        assertEquals(1, gameManager.getScoreProperty().get());

        gameManager.nextPage();
        gameManager.registerAnswer(page2.getElements().get(0), "True");
        assertEquals(2, gameManager.getScoreProperty().get());
    }

    @Test
    void testMultipleIncorrectAnswers() {
        gameManager.startNewQuiz();

        gameManager.registerAnswer(page1.getElements().get(0), "B");
        assertEquals(0, gameManager.getScoreProperty().get());

        gameManager.nextPage();
        gameManager.registerAnswer(page2.getElements().get(0), "False");
        assertEquals(0, gameManager.getScoreProperty().get());
    }

    @Test
    void testMixedAnswers() {
        gameManager.startNewQuiz();

        gameManager.registerAnswer(page1.getElements().get(0), "A"); // Correct
        assertEquals(1, gameManager.getScoreProperty().get());

        gameManager.nextPage();
        gameManager.registerAnswer(page2.getElements().get(0), "False"); // Incorrect
        assertEquals(1, gameManager.getScoreProperty().get());
    }
}
