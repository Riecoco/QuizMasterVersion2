package com.example.quizmaster;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Models.Element;
import Models.GameResult;
import Models.Page;
import Models.QuizGame;
import factories.Answerable;
import factories.QuizViewBody;
import factories.QuizViewBodyFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class GameController {

    public GameManager _gameManager = GameManager.getInstance();
    private final List<Answerable> currentAnswerOptions = new ArrayList<>();
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
        if (quizGame == null) {
            System.err.println("Error: Quiz game is null");
            quizOptions.getChildren().clear();
            return;
        }

        int pageNr = _gameManager.getCurrentPageNr();

        if (quizGame.getPages() == null || quizGame.getPages().isEmpty()) {
            System.err.println("Error: Quiz has no pages");
            quizOptions.getChildren().clear();
            return;
        }

        if (pageNr >= quizGame.getPages().size()) {
            System.out.println("No more pages - end of quiz");
            quizOptions.getChildren().clear();
            return;
        }

        if (pageNr < 0) {
            System.err.println("Error: Invalid page number: " + pageNr);
            quizOptions.getChildren().clear();
            return;
        }

        Page currentPage = quizGame.getPages().get(pageNr);
        if (currentPage == null) {
            System.err.println("Error: Page at index " + pageNr + " is null");
            quizOptions.getChildren().clear();
            return;
        }

        elements = currentPage.elements;
        if (elements == null || elements.isEmpty()) {
            System.err.println("Error: Page has no elements");
            quizOptions.getChildren().clear();
            return;
        }

        quizName.setText(quizGame.title != null ? quizGame.title : "Untitled Quiz");
        if (!elements.isEmpty() && elements.get(0) != null) {
            String title = elements.get(0).getTitle();
            questionTitle.setText(title != null ? title : "No title");
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
            System.err.println("Error in onEnterNameButtonClick: " + e.getMessage());
        }
    }

    public void processAnswerAndAdvance(ActionEvent actionEvent) {
        if (currentAnswerOptions == null || currentAnswerOptions.isEmpty()) {
            System.err.println("Error: No answer options available");
            return;
        }

        if (elements == null || elements.isEmpty()) {
            System.err.println("Error: No elements available");
            return;
        }

        if (currentAnswerOptions.size() != elements.size()) {
            System.err.println("Warning: Mismatch between answer options and elements");
        }

        for (int i = 0; i < currentAnswerOptions.size() && i < elements.size(); i++) {
            Answerable answerView = currentAnswerOptions.get(i);
            Element element = elements.get(i);

            if (answerView == null) {
                System.err.println("Warning: Answer view at index " + i + " is null");
                continue;
            }

            if (element == null) {
                System.err.println("Warning: Element at index " + i + " is null");
                continue;
            }

            try {
                String userAnswer = answerView.getSelectedAnswer();
                if (userAnswer == null) {
                    System.err.println("Warning: No answer selected for element " + i);
                    continue;
                }

                System.out.println("User selected: " + userAnswer);
                _gameManager.registerAnswer(element, userAnswer);
            } catch (Exception e) {
                System.err.println("Error processing answer for element " + i + ": " + e.getMessage());
            }
        }

        _gameManager.nextPage();
    }

    public void checkIfQuizIsFinished() {
        try {
            QuizGame quizGame = _gameManager.getQuizGame();
            if (quizGame == null) {
                System.err.println("Error: Quiz game is null");
                quizOptions.getChildren().clear();
                return;
            }

            if (quizGame.getPages() == null) {
                System.err.println("Error: Quiz pages are null");
                quizOptions.getChildren().clear();
                return;
            }

            if (_gameManager.getCurrentPageNr() < quizGame.getPages().size()) {
                setUpQuizUI(quizGame);
            } else {
                // End of quiz
                System.out.println("Quiz finished!");
                finishQuiz = new Button("Finish Quiz");
                quizViewBodyBox.getChildren().add(finishQuiz);
                finishQuiz.setOnAction(e -> {
                    try {
                        _gameManager.appendResultToFile();
                        showResultsScreen();
                    } catch (Exception ex) {
                        System.err.println("Error saving result: " + ex.getMessage());
                    }
                });
                if (timeline != null) {
                    timeline.stop();
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking if quiz is finished: " + e.getMessage());
            quizOptions.getChildren().clear();
        }
    }

    private void showResultsScreen() {
        try {
            quizViewBodyBox.setManaged(false);
            progressBarBox.setManaged(false);
            if (finishQuiz != null) {
                finishQuiz.setManaged(false);
                finishQuiz.setVisible(false);
            }

            List<GameResult> gameResults;
            try {
                gameResults = _gameManager.readResultsFromFile();
            } catch (Exception e) {
                System.err.println("Error loading results: " + e.getMessage());
                gameResults = new ArrayList<>();
                Label errorLabel = new Label("Could not load previous results: " + e.getMessage());
                resultsTable.getChildren().add(errorLabel);
                return;
            }

            if (gameResults == null) {
                gameResults = new ArrayList<>();
            }

            TableView<GameResult> resultsSummary = new TableView<>();
            resultsSummary.setItems(
                    FXCollections.observableArrayList(gameResults)
            );

            TableColumn<GameResult, String> playersCol
                    = new TableColumn<>("Players");
            playersCol.setCellValueFactory(
                    new PropertyValueFactory<>("playerName")
            );
            playersCol.setPrefWidth(30.0);
            TableColumn<GameResult, Integer> totalQsCol
                    = new TableColumn<>("Total Questions");
            totalQsCol.setCellValueFactory(
                    new PropertyValueFactory<>("totalQuestions")
            );
            totalQsCol.setPrefWidth(30.0);
            TableColumn<GameResult, Integer> totalCorrectCol
                    = new TableColumn<>("Total Correct");
            totalCorrectCol.setCellValueFactory(
                    new PropertyValueFactory<>("correctQuestions")
            );
            totalCorrectCol.setPrefWidth(30.0);
            TableColumn<GameResult, LocalDateTime> dateTimes
                    = new TableColumn<>("Timestamp");
            dateTimes.setCellValueFactory(
                    new PropertyValueFactory<>("dateTime")
            );

            resultsSummary.getColumns().setAll(
                    playersCol, totalQsCol, totalCorrectCol, dateTimes
            );

            resultsTable.getChildren().clear();
            resultsTable.getChildren().add(resultsSummary);
        } catch (Exception e) {
            System.err.println("Error showing results screen: " + e.getMessage());
        }
    }

}
