package Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageTest {

    private Page page;
    private List<Element> elements;

    @BeforeEach
    void setUp() {
        elements = new ArrayList<>();
        page = new Page();
    }

    @Test
    void testDefaultConstructor() {
        Page p = new Page();
        assertNotNull(p);
        assertEquals(0, p.getTimeLimit());
        assertNotNull(p.getElements());
        assertTrue(p.getElements().isEmpty());
    }

    @Test
    void testParameterizedConstructor() {
        int timeLimit = 30;
        Element element = new Radiogroup("q1", Type.radiogroup, "Question 1", true,
                "random", List.of("A", "B", "C"), "A");
        elements.add(element);

        Page p = new Page(timeLimit, elements);

        assertEquals(timeLimit, p.getTimeLimit());
        assertEquals(elements, p.getElements());
        assertEquals(1, p.getElements().size());
    }

    @Test
    void testSetTimeLimit() {
        int timeLimit = 25;
        page.setTimeLimit(timeLimit);
        assertEquals(timeLimit, page.getTimeLimit());
    }

    @Test
    void testSetTimeLimitZero() {
        page.setTimeLimit(0);
        assertEquals(0, page.getTimeLimit());
    }

    @Test
    void testSetTimeLimitNegative() {
        page.setTimeLimit(-5);
        assertEquals(-5, page.getTimeLimit());
    }

    @Test
    void testSetElements() {
        Element element1 = new Radiogroup("q1", Type.radiogroup, "Question 1", true,
                "random", List.of("A", "B"), "A");
        Element element2 = new Models.Boolean("q2", Type.trueFalseStatement, "Question 2", true,
                "True", "False", true);
        elements.add(element1);
        elements.add(element2);

        page.setElements(elements);
        assertEquals(2, page.getElements().size());
        assertEquals(elements, page.getElements());
    }

    @Test
    void testSetElementsNull() {
        page.setElements(null);
        assertNull(page.getElements());
    }

    @Test
    void testSetElementsEmpty() {
        page.setElements(new ArrayList<>());
        assertNotNull(page.getElements());
        assertTrue(page.getElements().isEmpty());
    }

    @Test
    void testGetTimeLimit() {
        page.timeLimit = 15;
        assertEquals(15, page.getTimeLimit());
    }

    @Test
    void testGetElements() {
        Element element = new Radiogroup("q1", Type.radiogroup, "Question", true,
                "random", List.of("A", "B"), "A");
        page.elements.add(element);
        assertEquals(1, page.getElements().size());
        assertEquals(element, page.getElements().get(0));
    }
}
