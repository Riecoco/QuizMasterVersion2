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

    private GameManager _gameManager = new GameManager();
    private Stage stage;

    @FXML
    private VBox menu;

    private GameController gameController;


    @FXML
    protected void onLoadQuizButtonClick(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Quiz File");
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            QuizGame quiz = objectMapper.readValue(file, QuizGame.class);
            menu.getChildren().add(new Label("✅ Quiz loaded: " + quiz.title));
            _gameManager.setQuizGame(quiz);
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
