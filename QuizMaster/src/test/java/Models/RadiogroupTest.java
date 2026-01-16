package Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RadiogroupTest {

    private Radiogroup radiogroup;

    @BeforeEach
    void setUp() {
        radiogroup = new Radiogroup();
    }

    @Test
    void testDefaultConstructor() {
        Radiogroup rg = new Radiogroup();
        assertNotNull(rg);
        assertNull(rg.choicesOrder);
        assertNotNull(rg.getChoices());
        assertTrue(rg.getChoices().isEmpty());
        assertNull(rg.getCorrectAnswer());
    }

    @Test
    void testParameterizedConstructor() {
        String name = "q1";
        Type type = Type.radiogroup;
        String title = "What is 2+2?";
        boolean isRequired = true;
        String choicesOrder = "random";
        List<String> choices = List.of("3", "4", "5", "6");
        String correctAnswer = "4";

        Radiogroup rg = new Radiogroup(name, type, title, isRequired, choicesOrder, choices, correctAnswer);

        assertEquals(name, rg.getName());
        assertEquals(type, rg.getType());
        assertEquals(title, rg.getTitle());
        assertEquals(isRequired, rg.isRequired());
        assertEquals(choicesOrder, rg.getChoicesOrder());
        assertEquals(choices, rg.getChoices());
        assertEquals(correctAnswer, rg.getCorrectAnswer());
    }

    @Test
    void testGetCorrectAnswerString() {
        radiogroup.correctAnswer = "Option A";
        assertEquals("Option A", radiogroup.getCorrectAnswerString());
    }

    @Test
    void testGetCorrectAnswerStringNull() {
        radiogroup.correctAnswer = null;
        assertNull(radiogroup.getCorrectAnswerString());
    }

    @Test
    void testGetChoicesOrder() {
        radiogroup.choicesOrder = "sequential";
        assertEquals("sequential", radiogroup.getChoicesOrder());
    }

    @Test
    void testSetChoicesOrder() {
        radiogroup.setChoicesOrder("random");
        assertEquals("random", radiogroup.getChoicesOrder());
    }

    @Test
    void testGetCorrectAnswer() {
        radiogroup.correctAnswer = "Answer";
        assertEquals("Answer", radiogroup.getCorrectAnswer());
    }

    @Test
    void testSetCorrectAnswer() {
        radiogroup.setCorrectAnswer("New Answer");
        assertEquals("New Answer", radiogroup.getCorrectAnswer());
    }

    @Test
    void testGetChoices() {
        List<String> choices = List.of("A", "B", "C");
        radiogroup.setChoices(choices);
        assertEquals(choices, radiogroup.getChoices());
        assertEquals(3, radiogroup.getChoices().size());
    }

    @Test
    void testSetChoices() {
        List<String> choices = new ArrayList<>();
        choices.add("Option 1");
        choices.add("Option 2");
        radiogroup.setChoices(choices);
        assertEquals(2, radiogroup.getChoices().size());
        assertEquals("Option 1", radiogroup.getChoices().get(0));
    }

    @Test
    void testSetChoicesNull() {
        radiogroup.setChoices(null);
        assertNull(radiogroup.getChoices());
    }

    @Test
    void testSetChoicesEmpty() {
        radiogroup.setChoices(new ArrayList<>());
        assertNotNull(radiogroup.getChoices());
        assertTrue(radiogroup.getChoices().isEmpty());
    }

    @Test
    void testInheritanceFromElement() {
        radiogroup.setName("test");
        radiogroup.setType(Type.radiogroup);
        radiogroup.setTitle("Test Question");
        radiogroup.setIsRequired(false);

        assertEquals("test", radiogroup.getName());
        assertEquals(Type.radiogroup, radiogroup.getType());
        assertEquals("Test Question", radiogroup.getTitle());
        assertFalse(radiogroup.isRequired());
    }
}
