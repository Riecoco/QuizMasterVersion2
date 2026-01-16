package com.example.quizmaster;

import Models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MenuController {

    private final GameManager _gameManager = GameManager.getInstance();
    @FXML
    private VBox menu;

    @FXML
    protected void onLoadQuizButtonClick(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Quiz File");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                // Check if file exists and is readable
                if (!file.exists()) {
                    menu.getChildren().add(new Label("❌ Error: File does not exist"));
                    return;
                }
                
                if (!file.canRead()) {
                    menu.getChildren().add(new Label("❌ Error: Cannot read file"));
                    return;
                }
                
                // Check if file is empty
                if (file.length() == 0) {
                    menu.getChildren().add(new Label("❌ Error: File is empty"));
                    return;
                }
                
                ObjectMapper objectMapper = new ObjectMapper();
                QuizGame quiz = objectMapper.readValue(file, QuizGame.class);
                
                // Validate quiz data
                if (quiz == null) {
                    menu.getChildren().add(new Label("❌ Error: Failed to parse quiz file"));
                    return;
                }
                
                if (quiz.title == null || quiz.title.trim().isEmpty()) {
                    menu.getChildren().add(new Label("❌ Error: Quiz title is missing"));
                    return;
                }
                
                if (quiz.pages == null || quiz.pages.isEmpty()) {
                    menu.getChildren().add(new Label("❌ Error: Quiz has no pages"));
                    return;
                }
                
                // Validate pages
                for (int i = 0; i < quiz.pages.size(); i++) {
                    Page page = quiz.pages.get(i);
                    if (page == null) {
                        menu.getChildren().add(new Label("❌ Error: Page " + (i + 1) + " is null"));
                        return;
                    }
                    if (page.elements == null || page.elements.isEmpty()) {
                        menu.getChildren().add(new Label("❌ Error: Page " + (i + 1) + " has no elements"));
                        return;
                    }
                    if (page.timeLimit <= 0) {
                        menu.getChildren().add(new Label("❌ Error: Page " + (i + 1) + " has invalid time limit"));
                        return;
                    }
                }
                
                menu.getChildren().add(new Label("✅ Quiz loaded: " + quiz.title));
                _gameManager.setQuizGame(quiz);
            } catch (com.fasterxml.jackson.core.JsonParseException e) {
                menu.getChildren().add(new Label("❌ Error: Invalid JSON format - " + e.getMessage()));
            } catch (com.fasterxml.jackson.databind.JsonMappingException e) {
                menu.getChildren().add(new Label("❌ Error: JSON mapping error - " + e.getMessage()));
            } catch (IOException e) {
                menu.getChildren().add(new Label("❌ Error: Failed to read file - " + e.getMessage()));
            } catch (Exception e) {
                menu.getChildren().add(new Label("❌ Error: Unexpected error - " + e.getMessage()));
            }
        }
    }

    @FXML
    public void onStartQuizButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game-view.fxml"));
            Parent root = loader.load();

            GameController gameController = loader.getController();
            gameController.initialize(_gameManager);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
