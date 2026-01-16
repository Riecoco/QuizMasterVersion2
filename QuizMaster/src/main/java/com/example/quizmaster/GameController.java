package com.example.quizmaster;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import Models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import factories.Answerable;
import factories.QuizViewBody;
import factories.QuizViewBodyFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class GameController {

    public GameManager _gameManager = GameManager.getInstance();
    private List<Answerable> currentAnswerOptions = new ArrayList<>();
    private List<Element> elements;
    private Button finishQuiz;

    private Timeline timeline;

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
    @FXML
    private VBox resultsTable;

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
        _gameManager.startNewQuiz();
    }

    private void setUpQuizUI(QuizGame quizGame) {
        int pageNr = _gameManager.getCurrentPageNr();

        if (pageNr >= quizGame.getPages().size()) {
            System.out.println("No more pages - end of quiz");
            quizOptions.getChildren().clear();
            return;
        }
        Page currentPage = quizGame.getPages().get(pageNr);
        elements = currentPage.elements;
        quizName.setText(quizGame.title);
        if (!elements.isEmpty()) {
            questionTitle.setText(elements.get(0).getTitle());
        }
        progressBarBox.setVisible(true);
        populateQuizBody();
    }

    private void countProgressBarDown() {
        // Countdown timer
        if (timeline != null) {
            timeline.stop();
        }

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

    private void buildElementUI(QuizViewBodyFactory factory) {
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
                    if (timeline != null) {
                        timeline.stop(); // Stop timer when answer is selected
                    }
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

            _gameManager.savePlayerName(playerName.getText());

            quizViewBodyBox.setManaged(true);
            quizViewBodyBox.setVisible(true);

            setUpQuizUI(_gameManager.getQuizGame());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processAnswerAndAdvance(ActionEvent actionEvent) {
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
            finishQuiz = new Button("Finish Quiz");
            quizViewBodyBox.getChildren().add(finishQuiz);
            finishQuiz.setOnAction(e -> {
                _gameManager.appendResultToFile();
                showResultsScreen();
            });
            if (timeline != null) {
                timeline.stop();
            }
        }
    }

    private void showResultsScreen() {
        quizViewBodyBox.setManaged(false);
        progressBarBox.setManaged(false);
        finishQuiz.setManaged(false);
        finishQuiz.setVisible(false);

        List<GameResult> gameResults =
                _gameManager.readResultsFromFile();

        TableView<GameResult> resultsSummary = new TableView<>();
        resultsSummary.setItems(
                FXCollections.observableArrayList(gameResults)
        );

        TableColumn<GameResult, String> playersCol =
                new TableColumn<>("Players");
        playersCol.setCellValueFactory(
                new PropertyValueFactory<>("playerName")
        );
        playersCol.setPrefWidth(30.0);
        TableColumn<GameResult, Integer> totalQsCol =
                new TableColumn<>("Total Questions");
        totalQsCol.setCellValueFactory(
                new PropertyValueFactory<>("totalQuestions")
        );
        totalQsCol.setPrefWidth(30.0);
        TableColumn<GameResult, Integer> totalCorrectCol =
                new TableColumn<>("Total Correct");
        totalCorrectCol.setCellValueFactory(
                new PropertyValueFactory<>("correctQuestions")
        );
        totalCorrectCol.setPrefWidth(30.0);
        TableColumn<GameResult, LocalDateTime> dateTimes =
                new TableColumn<>("Timestamp");
        dateTimes.setCellValueFactory(
                new PropertyValueFactory<>("dateTime")
        );

        resultsSummary.getColumns().setAll(
                playersCol, totalQsCol, totalCorrectCol, dateTimes
        );

        resultsTable.getChildren().add(resultsSummary);
    }

}
