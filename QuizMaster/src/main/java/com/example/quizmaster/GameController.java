package com.example.quizmaster;

import Models.*;
import factories.Answerable;
import factories.QuizViewBody;
import factories.QuizViewBodyFactory;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    public GameManager _gameManager = GameManager.getInstance();
    private List<Answerable> currentAnswerOptions = new ArrayList<>();
    private List<Element> elements;

    private Timeline timeline;

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
    @FXML
    private Label countdownLabel; // Add this to your FXML

    public void initialize(GameManager manager) {
        _gameManager = manager;
    }

    private void setUpQuizUI(QuizGame quizGame) {
        int pageNr = _gameManager.getCurrentPageNr();

        if (pageNr >= quizGame.getPages().size()) {
            System.out.println("No more pages - end of quiz");
            quizOptions.getChildren().clear();
            countdownLabel.setText("");
            next.setDisable(true);
            return;
        }

        Page currentPage = quizGame.getPages().get(pageNr);
        elements = currentPage.elements;

        quizName.setText(quizGame.title);
        if (!elements.isEmpty()) {
            questionTitle.setText(elements.get(0).getTitle());
        }

        populateQuizBody(quizGame, currentPage);
    }

    private void populateQuizBody(QuizGame quizGame, Page currentPage) {
        quizOptions.getChildren().clear();
        currentAnswerOptions.clear();

        QuizViewBodyFactory factory = new QuizViewBodyFactory();
        next.setDisable(true);

        // Countdown timer
        if (timeline != null) timeline.stop();
        int timeLimitSeconds = currentPage.getTimeLimit(); // Page must have getTimeLimit()
        final int[] timeLeft = {timeLimitSeconds};

        countdownLabel.setText("Time left: " + timeLeft[0] + "s");
        timeline = new Timeline(new KeyFrame(
                javafx.util.Duration.seconds(1),
                event -> {
                    countdownLabel.setText("Time left: " + timeLeft[0] + "s");
                    timeLeft[0]--;
                    if (timeLeft[0] < 0) {
                        timeline.stop();
                        next.setDisable(false); // auto-enable Next if timer runs out
                    }
                },
                new KeyValue[]{}));
        timeline.setCycleCount(timeLimitSeconds + 1);
        timeline.play();

        // Build UI for each element
        for (Element element : elements) {
            QuizViewBody elementViewBody = factory.createElementViewBody(element);
            VBox vbox = elementViewBody.buildViewBody(element, new VBox());
            quizOptions.getChildren().add(vbox);

            if (elementViewBody instanceof Answerable a) {
                currentAnswerOptions.add(a);
                // Enable Next button when any option is selected
                a.getRadioButtons().forEach(rb -> rb.setOnAction(e -> next.setDisable(false)));
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

            quizViewBodyBox.setManaged(true);
            quizViewBodyBox.setVisible(true);
            next.setManaged(true);
            next.setDisable(true);
            next.setVisible(true);

            setUpQuizUI(_gameManager.getQuizGame());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onNextButtonClick(ActionEvent actionEvent) {
        for (int i = 0; i < currentAnswerOptions.size(); i++) {
            Answerable answerView = currentAnswerOptions.get(i);
            Element element = elements.get(i);

            String userAnswer = answerView.getSelectedAnswer();
            System.out.println("User selected: " + userAnswer);

            _gameManager.registerAnswer(element, userAnswer);
        }

        _gameManager.nextPage();

        if (_gameManager.getCurrentPageNr() < _gameManager.getQuizGame().getPages().size()) {
            setUpQuizUI(_gameManager.getQuizGame());
        } else {
            // End of quiz
            System.out.println("Quiz finished!");
            quizOptions.getChildren().clear();
            countdownLabel.setText("");
            next.setDisable(true);
            // Optionally show results screen here
        }
    }
}
