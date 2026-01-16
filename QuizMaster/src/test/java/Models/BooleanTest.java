package Models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BooleanTest {

    private Boolean booleanElement;

    @BeforeEach
    void setUp() {
        booleanElement = new Boolean();
    }

    @Test
    void testDefaultConstructor() {
        Boolean b = new Boolean();
        assertNotNull(b);
        assertEquals("True", b.labelTrue);
        assertEquals("False", b.labelFalse);
        assertFalse(b.correctAnswer);
    }

    @Test
    void testParameterizedConstructor() {
        String name = "q1";
        Type type = Type.trueFalseStatement;
        String title = "Is Java object-oriented?";
        boolean isRequired = true;
        String labelTrue = "Yes";
        String labelFalse = "No";
        boolean correctAnswer = true;

        Boolean b = new Boolean(name, type, title, isRequired, labelTrue, labelFalse, correctAnswer);

        assertEquals(name, b.getName());
        assertEquals(type, b.getType());
        assertEquals(title, b.getTitle());
        assertEquals(isRequired, b.isRequired());
        assertEquals(labelTrue, b.labelTrue);
        assertEquals(labelFalse, b.labelFalse);
        assertEquals(correctAnswer, b.correctAnswer);
    }

    @Test
    void testGetCorrectAnswerStringTrue() {
        booleanElement.correctAnswer = true;
        booleanElement.labelTrue = "True";
        assertEquals("True", booleanElement.getCorrectAnswerString());
    }

    @Test
    void testGetCorrectAnswerStringFalse() {
        booleanElement.correctAnswer = false;
        booleanElement.labelFalse = "False";
        assertEquals("False", booleanElement.getCorrectAnswerString());
    }

    @Test
    void testGetCorrectAnswerStringCustomLabels() {
        booleanElement.correctAnswer = true;
        booleanElement.labelTrue = "Correct";
        booleanElement.labelFalse = "Incorrect";
        assertEquals("Correct", booleanElement.getCorrectAnswerString());

        booleanElement.correctAnswer = false;
        assertEquals("Incorrect", booleanElement.getCorrectAnswerString());
    }

    @Test
    void testIsCorrectAnswer() {
        booleanElement.correctAnswer = true;
        assertTrue(booleanElement.isCorrectAnswer());

        booleanElement.correctAnswer = false;
        assertFalse(booleanElement.isCorrectAnswer());
    }

    @Test
    void testSetCorrectAnswer() {
        booleanElement.setCorrectAnswer(true);
        assertTrue(booleanElement.isCorrectAnswer());

        booleanElement.setCorrectAnswer(false);
        assertFalse(booleanElement.isCorrectAnswer());
    }

    @Test
    void testInheritanceFromElement() {
        booleanElement.setName("test");
        booleanElement.setType(Type.trueFalseStatement);
        booleanElement.setTitle("Test Question");
        booleanElement.setIsRequired(true);

        assertEquals("test", booleanElement.getName());
        assertEquals(Type.trueFalseStatement, booleanElement.getType());
        assertEquals("Test Question", booleanElement.getTitle());
        assertTrue(booleanElement.isRequired());
    }
}
