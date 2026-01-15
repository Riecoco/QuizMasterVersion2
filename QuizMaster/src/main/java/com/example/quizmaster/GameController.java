package com.example.quizmaster;

import Models.*;
import factories.QuizViewBodyFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    public GameManager _gameManager = new GameManager();
    @FXML
    public TextField playerName;
    @FXML
    public VBox nameForm;
    @FXML
    public Button next;
    @FXML
    public VBox quizViewBodyBox;
    @FXML
    private Label quizName;
    @FXML
    private Label questionTitle;
    @FXML
    private VBox quizOptions;

    public void initialize(GameManager manager) {
        _gameManager = manager;
    }

    private void setUpQuizUI(QuizGame quizGame) {
        int pageNr = _gameManager.getCurrentPageNr();

        if (pageNr >= quizGame.getPages().size()) {
            System.out.println("No more pages - end of quiz");
            // TO DO: Show results on the screen
            return;
        }

        // Set header and question
        quizName.setText(quizGame.title);
        questionTitle.setText(quizGame.getPages().get(pageNr).elements.getFirst().title);

        // Populate the options
        populateQuizBody(quizGame);
    }

    private void populateQuizBody(QuizGame quizGame) {
        int pageNr = _gameManager.getCurrentPageNr();
        if (pageNr >= quizGame.getPages().size()) return;

        List<Element> elements = quizGame.getPages().get(pageNr).elements;

        quizOptions.getChildren().clear();
        QuizViewBodyFactory factory = new QuizViewBodyFactory();
        for (Element e : elements) {
            VBox elementViewBody = factory.createElementViewBody(e);

            quizOptions.getChildren().add(elementViewBody);
        }
    }


    public void onEnterNameButtonClick(ActionEvent actionEvent) {
        try {
            nameForm.setVisible(false);
            nameForm.setManaged(false);

            Result quizGameResult = new Result();
            quizGameResult.setPlayerName(playerName.getText());
            _gameManager.setQuizDetails(quizGameResult);

            System.out.println("Player name: " + quizGameResult.getPlayerName());

            quizViewBodyBox.setManaged(true);
            quizViewBodyBox.setVisible(true);
            next.setManaged(true);
            next.setVisible(true);

            setUpQuizUI(_gameManager.getQuizGame());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onNextButtonClick(ActionEvent actionEvent) {
        _gameManager.nextPage();
        setUpQuizUI(_gameManager.getQuizGame());
    }

}
