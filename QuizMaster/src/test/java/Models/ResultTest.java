package Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    private Result result;
    private List<GameResult> gameResults;

    @BeforeEach
    void setUp() {
        gameResults = new ArrayList<>();
        result = new Result();
    }

    @Test
    void testDefaultConstructor() {
        Result r = new Result();
        assertNotNull(r);
        assertNull(r.getQuizName());
        assertNull(r.getGameResults());
    }

    @Test
    void testParameterizedConstructor() {
        String quizName = "Test Quiz";
        GameResult gameResult = new GameResult("Player1", 10, 8, LocalDateTime.now());
        gameResults.add(gameResult);

        Result r = new Result(quizName, gameResults);

        assertEquals(quizName, r.getQuizName());
        assertEquals(gameResults, r.getGameResults());
        assertEquals(1, r.getGameResults().size());
    }

    @Test
    void testSetQuizName() {
        String quizName = "New Quiz";
        result.setQuizName(quizName);
        assertEquals(quizName, result.getQuizName());
    }

    @Test
    void testSetQuizNameNull() {
        result.setQuizName(null);
        assertNull(result.getQuizName());
    }

    @Test
    void testSetQuizNameEmpty() {
        result.setQuizName("");
        assertEquals("", result.getQuizName());
    }

    @Test
    void testSetGameResults() {
        GameResult gameResult1 = new GameResult("Player1", 10, 7, LocalDateTime.now());
        GameResult gameResult2 = new GameResult("Player2", 10, 9, LocalDateTime.now());
        gameResults.add(gameResult1);
        gameResults.add(gameResult2);

        result.setGameResults(gameResults);
        assertEquals(2, result.getGameResults().size());
        assertEquals(gameResults, result.getGameResults());
    }

    @Test
    void testSetGameResultsNull() {
        result.setGameResults(null);
        assertNull(result.getGameResults());
    }

    @Test
    void testSetGameResultsEmpty() {
        result.setGameResults(new ArrayList<>());
        assertNotNull(result.getGameResults());
        assertTrue(result.getGameResults().isEmpty());
    }

    @Test
    void testGetQuizName() {
        result.setQuizName("Test");
        assertEquals("Test", result.getQuizName());
    }

    @Test
    void testGetGameResults() {
        GameResult gameResult = new GameResult("Player", 5, 4, LocalDateTime.now());
        gameResults.add(gameResult);
        result.setGameResults(gameResults);
        assertEquals(1, result.getGameResults().size());
        assertEquals(gameResult, result.getGameResults().get(0));
    }
}
