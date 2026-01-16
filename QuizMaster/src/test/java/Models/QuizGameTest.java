package Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuizGameTest {

    private QuizGame quizGame;
    private List<Page> pages;
    private List<CompletedHTMLOnCondition> conditions;

    @BeforeEach
    void setUp() {
        pages = new ArrayList<>();
        conditions = new ArrayList<>();
        quizGame = new QuizGame();
    }

    @Test
    void testDefaultConstructor() {
        QuizGame game = new QuizGame();
        assertNotNull(game);
        assertNull(game.getTitle());
        assertNull(game.getDescription());
        assertNotNull(game.getPages());
        assertTrue(game.getPages().isEmpty());
        assertNull(game.getCompletedHtml());
    }

    @Test
    void testParameterizedConstructor() {
        String title = "Test Quiz";
        String description = "Test Description";
        String completedHtml = "<h1>Completed</h1>";

        Page page1 = new Page(10, new ArrayList<>());
        pages.add(page1);

        CompletedHTMLOnCondition condition = new CompletedHTMLOnCondition("score > 5", "<h2>Great!</h2>");
        conditions.add(condition);

        QuizGame game = new QuizGame(title, description, pages, completedHtml, conditions);

        assertEquals(title, game.getTitle());
        assertEquals(description, game.getDescription());
        assertEquals(pages, game.getPages());
        assertEquals(completedHtml, game.getCompletedHtml());
        assertEquals(1, game.getPages().size());
    }

    @Test
    void testSetTitle() {
        String title = "New Title";
        quizGame.setTitle(title);
        assertEquals(title, quizGame.getTitle());
    }

    @Test
    void testSetTitleNull() {
        quizGame.setTitle(null);
        assertNull(quizGame.getTitle());
    }

    @Test
    void testSetDescription() {
        String description = "New Description";
        quizGame.setDescription(description);
        assertEquals(description, quizGame.getDescription());
    }

    @Test
    void testSetPages() {
        Page page1 = new Page(10, new ArrayList<>());
        Page page2 = new Page(20, new ArrayList<>());
        pages.add(page1);
        pages.add(page2);

        quizGame.setPages(pages);
        assertEquals(2, quizGame.getPages().size());
        assertEquals(pages, quizGame.getPages());
    }

    @Test
    void testSetPagesNull() {
        quizGame.setPages(null);
        assertNull(quizGame.getPages());
    }

    @Test
    void testSetCompletedHtml() {
        String html = "<h1>Test HTML</h1>";
        quizGame.setCompletedHtml(html);
        assertEquals(html, quizGame.getCompletedHtml());
    }

    @Test
    void testSetCompletedHtmlNull() {
        quizGame.setCompletedHtml(null);
        assertNull(quizGame.getCompletedHtml());
    }

    @Test
    void testGetTitle() {
        quizGame.title = "Direct Title";
        assertEquals("Direct Title", quizGame.getTitle());
    }

    @Test
    void testGetDescription() {
        quizGame.description = "Direct Description";
        assertEquals("Direct Description", quizGame.getDescription());
    }

    @Test
    void testGetPages() {
        Page page = new Page(15, new ArrayList<>());
        quizGame.pages.add(page);
        assertEquals(1, quizGame.getPages().size());
        assertEquals(page, quizGame.getPages().get(0));
    }

    @Test
    void testGetCompletedHtml() {
        quizGame.completedHtml = "Direct HTML";
        assertEquals("Direct HTML", quizGame.getCompletedHtml());
    }
}
