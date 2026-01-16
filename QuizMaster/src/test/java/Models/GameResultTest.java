package Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class GameResultTest {

    private GameResult gameResult;

    @BeforeEach
    void setUp() {
        gameResult = new GameResult();
    }

    @Test
    void testDefaultConstructor() {
        GameResult gr = new GameResult();
        assertNotNull(gr);
        assertNull(gr.getPlayerName());
        assertNull(gr.getTotalQuestions());
        assertNull(gr.getCorrectQuestions());
        assertNull(gr.getDateTime());
    }

    @Test
    void testParameterizedConstructor() {
        String playerName = "John Doe";
        Integer totalQuestions = 10;
        Integer correctQuestions = 8;
        LocalDateTime dateTime = LocalDateTime.now();

        GameResult gr = new GameResult(playerName, totalQuestions, correctQuestions, dateTime);

        assertEquals(playerName, gr.getPlayerName());
        assertEquals(totalQuestions, gr.getTotalQuestions());
        assertEquals(correctQuestions, gr.getCorrectQuestions());
        assertEquals(dateTime, gr.getDateTime());
    }

    @Test
    void testSetPlayerName() {
        String playerName = "Jane Doe";
        gameResult.setPlayerName(playerName);
        assertEquals(playerName, gameResult.getPlayerName());
    }

    @Test
    void testSetPlayerNameNull() {
        gameResult.setPlayerName(null);
        assertNull(gameResult.getPlayerName());
    }

    @Test
    void testSetPlayerNameEmpty() {
        gameResult.setPlayerName("");
        assertEquals("", gameResult.getPlayerName());
    }

    @Test
    void testSetTotalQuestions() {
        Integer totalQuestions = 15;
        gameResult.setTotalQuestions(totalQuestions);
        assertEquals(totalQuestions, gameResult.getTotalQuestions());
    }

    @Test
    void testSetTotalQuestionsNull() {
        gameResult.setTotalQuestions(null);
        assertNull(gameResult.getTotalQuestions());
    }

    @Test
    void testSetTotalQuestionsZero() {
        gameResult.setTotalQuestions(0);
        assertEquals(0, gameResult.getTotalQuestions());
    }

    @Test
    void testSetCorrectQuestions() {
        Integer correctQuestions = 12;
        gameResult.setCorrectQuestions(correctQuestions);
        assertEquals(correctQuestions, gameResult.getCorrectQuestions());
    }

    @Test
    void testSetCorrectQuestionsNull() {
        gameResult.setCorrectQuestions(null);
        assertNull(gameResult.getCorrectQuestions());
    }

    @Test
    void testSetCorrectQuestionsZero() {
        gameResult.setCorrectQuestions(0);
        assertEquals(0, gameResult.getCorrectQuestions());
    }

    @Test
    void testSetCorrectQuestionsGreaterThanTotal() {
        gameResult.setTotalQuestions(10);
        gameResult.setCorrectQuestions(15);
        assertEquals(15, gameResult.getCorrectQuestions());
        // Note: This doesn't validate business logic, just tests the setter
    }

    @Test
    void testSetDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 15, 10, 30);
        gameResult.setDateTime(dateTime);
        assertEquals(dateTime, gameResult.getDateTime());
    }

    @Test
    void testSetDateTimeNull() {
        gameResult.setDateTime(null);
        assertNull(gameResult.getDateTime());
    }

    @Test
    void testGetPlayerName() {
        gameResult.playerName = "Direct Name";
        assertEquals("Direct Name", gameResult.getPlayerName());
    }

    @Test
    void testGetTotalQuestions() {
        gameResult.totalQuestions = 20;
        assertEquals(20, gameResult.getTotalQuestions());
    }

    @Test
    void testGetCorrectQuestions() {
        gameResult.correctQuestions = 18;
        assertEquals(18, gameResult.getCorrectQuestions());
    }

    @Test
    void testGetDateTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        gameResult.dateTime = dateTime;
        assertEquals(dateTime, gameResult.getDateTime());
    }
}
