package Models;

import java.util.List;

public class Result {

    private String quizName;
    private List<GameResult> gameResults;

    public Result() {
    }

    public Result(String quizName, List<GameResult> gameResults) {
        this.quizName = quizName;
        this.gameResults = gameResults;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public List<GameResult> getGameResults() {
        return gameResults;
    }

    public void setGameResults(List<GameResult> gameResults) {
        this.gameResults = gameResults;
    }
}
