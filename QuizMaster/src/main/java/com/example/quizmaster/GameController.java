package com.example.quizmaster;

import Models.*;
import factories.QuizViewBodyFactory;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
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
    public VBox quizViewBodyBox;
    @FXML
    private Label quizName;
    @FXML
    private Label questionTitle;
    @FXML
    private Label quizScore;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private VBox progressBarBox;
    @FXML
    private VBox quizOptions;

    public void initialize(GameManager manager) {
        _gameManager = manager;
        StringBinding scoreBinding = Bindings.createStringBinding(() -> "Score: " + _gameManager.getScoreProperty().get(), _gameManager.getScoreProperty());
        quizScore.textProperty().bind(scoreBinding);
        progressBar.progressProperty().bind(
            Bindings.createDoubleBinding(
                () -> (double) _gameManager.getTimeLeftProperty().get() / _gameManager.getTimeLimit(),
                _gameManager.getTimeLeftProperty()
            )
        );
    }

    private void setUpQuizUI(QuizGame quizGame) {
        int pageNr = _gameManager.getCurrentPageNr();

        if (pageNr >= quizGame.getPages().size()) {
            System.out.println("No more pages - end of quiz");
            quizOptions.getChildren().clear();
            return;
        }

        // Set header and question
        quizName.setText(quizGame.title);
        questionTitle.setText(quizGame.getPages().get(pageNr).elements.getFirst().title);

        progressBarBox.setVisible(true);

        populateQuizBody();
    }

    private void countProgressBarDown(){
        // Countdown timer
        if (timeline != null) timeline.stop();
        
        // Initialize timeLeft with the time limit for current page
        int timeLimit = _gameManager.getTimeLimit();
        _gameManager.setTimeLeft(timeLimit);

        timeline = new Timeline(new KeyFrame(
                    Duration.seconds(1),
                    event -> {
                        int currentTime = _gameManager.getTimeLeftProperty().get();
                        if (currentTime > 0) {
                            _gameManager.setTimeLeft(currentTime - 1);
                        } else {
                            timeline.stop();
                            _gameManager.nextPage();
                            checkIfQuizIsFinished();
                        }
                    }
                )
            );
        timeline.setCycleCount(timeLimit + 1);
        timeline.play();
    }
    
    private void populateQuizBody() {
        quizOptions.getChildren().clear();
        currentAnswerOptions.clear();
        QuizViewBodyFactory factory = new QuizViewBodyFactory();
        buildElementUI(factory);
        countProgressBarDown();
    }

    private void buildElementUI(QuizViewBodyFactory factory){
        // Build UI for each element
        for (Element element : elements) {
            QuizViewBody elementViewBody = factory.createElementViewBody(element);
            VBox vbox = elementViewBody.buildViewBody(element, new VBox());
            quizOptions.getChildren().add(vbox);
        
            if (elementViewBody instanceof Answerable a) {
                currentAnswerOptions.add(a);
                // Process answer and advance when any option is selected
                a.getRadioButtons().forEach(rb -> rb.setOnAction(e -> {
                    rb.setDisable(true);
                    if (timeline != null) timeline.stop(); // Stop timer when answer is selected
                    processAnswerAndAdvance(e);
                    checkIfQuizIsFinished();
                }));
            }
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

            setUpQuizUI(_gameManager.getQuizGame());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processAnswerAndAdvance(ActionEvent actionEvent){
        for (int i = 0; i < currentAnswerOptions.size(); i++) {
            Answerable answerView = currentAnswerOptions.get(i);
            Element element = elements.get(i);

            String userAnswer = answerView.getSelectedAnswer();
            System.out.println("User selected: " + userAnswer);

            _gameManager.registerAnswer(element, userAnswer);
        }

        _gameManager.nextPage();
    }

    public void checkIfQuizIsFinished() {

        if (_gameManager.getCurrentPageNr() < _gameManager.getQuizGame().getPages().size()) {
            setUpQuizUI(_gameManager.getQuizGame());
        } else {
            // End of quiz
            System.out.println("Quiz finished!");
            quizOptions.getChildren().clear();
            // Optionally show results screen here
        }
    }

}
