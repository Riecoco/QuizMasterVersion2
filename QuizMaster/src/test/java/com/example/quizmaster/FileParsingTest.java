package com.example.quizmaster;

import Models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileParsingTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testParseValidQuizJson(@TempDir Path tempDir) throws IOException {
        String validJson = """
                {
                  "title": "Test Quiz",
                  "description": "Test Description",
                  "pages": [
                    {
                      "timeLimit": 10,
                      "elements": [
                        {
                          "type": "radiogroup",
                          "name": "q1",
                          "title": "Question 1",
                          "choicesOrder": "random",
                          "choices": ["A", "B", "C"],
                          "correctAnswer": "A",
                          "isRequired": true
                        }
                      ]
                    }
                  ],
                  "completedHtml": "<h1>Done</h1>",
                  "completedHtmlOnCondition": []
                }
                """;

        Path testFile = tempDir.resolve("test-quiz.json");
        Files.writeString(testFile, validJson);

        QuizGame quiz = objectMapper.readValue(testFile.toFile(), QuizGame.class);

        assertNotNull(quiz);
        assertEquals("Test Quiz", quiz.getTitle());
        assertEquals("Test Description", quiz.getDescription());
        assertEquals(1, quiz.getPages().size());
        assertEquals(10, quiz.getPages().get(0).getTimeLimit());
        assertEquals(1, quiz.getPages().get(0).getElements().size());
    }

    @Test
    void testParseEmptyJsonFile(@TempDir Path tempDir) throws IOException {
        Path emptyFile = tempDir.resolve("empty.json");
        Files.createFile(emptyFile);

        assertThrows(Exception.class, () -> {
            objectMapper.readValue(emptyFile.toFile(), QuizGame.class);
        });
    }

    @Test
    void testParseCorruptJsonFile(@TempDir Path tempDir) throws IOException {
        String corruptJson = "{ invalid json }";
        Path corruptFile = tempDir.resolve("corrupt.json");
        Files.writeString(corruptFile, corruptJson);

        assertThrows(Exception.class, () -> {
            objectMapper.readValue(corruptFile.toFile(), QuizGame.class);
        });
    }

    @Test
    void testParseJsonWithMissingFields(@TempDir Path tempDir) throws IOException {
        String incompleteJson = """
                {
                  "title": "Test Quiz"
                }
                """;

        Path incompleteFile = tempDir.resolve("incomplete.json");
        Files.writeString(incompleteFile, incompleteJson);

        // Should parse but with null/empty fields
        QuizGame quiz = objectMapper.readValue(incompleteFile.toFile(), QuizGame.class);
        assertNotNull(quiz);
        assertEquals("Test Quiz", quiz.getTitle());
        assertNull(quiz.getDescription());
    }

    @Test
    void testParseJsonWithInvalidTimeLimit(@TempDir Path tempDir) throws IOException {
        String jsonWithInvalidTime = """
                {
                  "title": "Test Quiz",
                  "pages": [
                    {
                      "timeLimit": -5,
                      "elements": []
                    }
                  ]
                }
                """;

        Path testFile = tempDir.resolve("invalid-time.json");
        Files.writeString(testFile, jsonWithInvalidTime);

        QuizGame quiz = objectMapper.readValue(testFile.toFile(), QuizGame.class);
        assertNotNull(quiz);
        assertEquals(-5, quiz.getPages().get(0).getTimeLimit());
    }

    @Test
    void testParseJsonWithEmptyPages(@TempDir Path tempDir) throws IOException {
        String jsonWithEmptyPages = """
                {
                  "title": "Test Quiz",
                  "pages": []
                }
                """;

        Path testFile = tempDir.resolve("empty-pages.json");
        Files.writeString(testFile, jsonWithEmptyPages);

        QuizGame quiz = objectMapper.readValue(testFile.toFile(), QuizGame.class);
        assertNotNull(quiz);
        assertTrue(quiz.getPages().isEmpty());
    }

    @Test
    void testParseJsonWithNullTitle(@TempDir Path tempDir) throws IOException {
        String jsonWithNullTitle = """
                {
                  "title": null,
                  "pages": []
                }
                """;

        Path testFile = tempDir.resolve("null-title.json");
        Files.writeString(testFile, jsonWithNullTitle);

        QuizGame quiz = objectMapper.readValue(testFile.toFile(), QuizGame.class);
        assertNotNull(quiz);
        assertNull(quiz.getTitle());
    }

    @Test
    void testParseResultsJson(@TempDir Path tempDir) throws IOException {
        String resultsJson = """
                [
                  {
                    "quizName": "Test Quiz",
                    "gameResults": [
                      {
                        "playerName": "Player1",
                        "totalQuestions": 10,
                        "correctQuestions": 8,
                        "dateTime": "2024-01-15T10:30:00"
                      }
                    ]
                  }
                ]
                """;

        Path resultsFile = tempDir.resolve("results.json");
        Files.writeString(resultsFile, resultsJson);

        com.fasterxml.jackson.core.type.TypeReference<List<Result>> typeRef
                = new com.fasterxml.jackson.core.type.TypeReference<List<Result>>() {
        };
        List<Result> results = objectMapper.readValue(resultsFile.toFile(), typeRef);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Test Quiz", results.get(0).getQuizName());
        assertEquals(1, results.get(0).getGameResults().size());
    }

    @Test
    void testParseResultsJsonEmpty(@TempDir Path tempDir) throws IOException {
        String emptyResultsJson = "[]";

        Path resultsFile = tempDir.resolve("empty-results.json");
        Files.writeString(resultsFile, emptyResultsJson);

        com.fasterxml.jackson.core.type.TypeReference<List<Result>> typeRef
                = new com.fasterxml.jackson.core.type.TypeReference<List<Result>>() {
        };
        List<Result> results = objectMapper.readValue(resultsFile.toFile(), typeRef);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void testParseResultsJsonCorrupt(@TempDir Path tempDir) throws IOException {
        String corruptResultsJson = "{ invalid }";

        Path resultsFile = tempDir.resolve("corrupt-results.json");
        Files.writeString(resultsFile, corruptResultsJson);

        com.fasterxml.jackson.core.type.TypeReference<List<Result>> typeRef
                = new com.fasterxml.jackson.core.type.TypeReference<List<Result>>() {
        };

        assertThrows(Exception.class, () -> {
            objectMapper.readValue(resultsFile.toFile(), typeRef);
        });
    }

    @Test
    void testParseBooleanElement(@TempDir Path tempDir) throws IOException {
        String jsonWithBoolean = """
                {
                  "title": "Test Quiz",
                  "pages": [
                    {
                      "timeLimit": 10,
                      "elements": [
                        {
                          "type": "boolean",
                          "name": "q1",
                          "title": "Is Java OOP?",
                          "labelTrue": "True",
                          "labelFalse": "False",
                          "correctAnswer": true,
                          "isRequired": true
                        }
                      ]
                    }
                  ]
                }
                """;

        Path testFile = tempDir.resolve("boolean-quiz.json");
        Files.writeString(testFile, jsonWithBoolean);

        QuizGame quiz = objectMapper.readValue(testFile.toFile(), QuizGame.class);
        assertNotNull(quiz);
        Element element = quiz.getPages().get(0).getElements().get(0);
        assertTrue(element instanceof Models.Boolean);
        Models.Boolean boolElement = (Models.Boolean) element;
        assertEquals("True", boolElement.getCorrectAnswerString());
    }

    @Test
    void testParseRadiogroupElement(@TempDir Path tempDir) throws IOException {
        String jsonWithRadiogroup = """
                {
                  "title": "Test Quiz",
                  "pages": [
                    {
                      "timeLimit": 10,
                      "elements": [
                        {
                          "type": "radiogroup",
                          "name": "q1",
                          "title": "What is 2+2?",
                          "choicesOrder": "random",
                          "choices": ["3", "4", "5"],
                          "correctAnswer": "4",
                          "isRequired": true
                        }
                      ]
                    }
                  ]
                }
                """;

        Path testFile = tempDir.resolve("radiogroup-quiz.json");
        Files.writeString(testFile, jsonWithRadiogroup);

        QuizGame quiz = objectMapper.readValue(testFile.toFile(), QuizGame.class);
        assertNotNull(quiz);
        Element element = quiz.getPages().get(0).getElements().get(0);
        assertTrue(element instanceof Radiogroup);
        Radiogroup rgElement = (Radiogroup) element;
        assertEquals("4", rgElement.getCorrectAnswerString());
        assertEquals(3, rgElement.getChoices().size());
    }
}
