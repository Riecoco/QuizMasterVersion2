package Models;

import java.time.LocalDateTime;

public class GameResult {

    public String playerName;
    public Integer totalQuestions;
    public Integer correctQuestions;
    public LocalDateTime dateTime;

    public GameResult(String playerName, Integer totalQuestions, Integer correctQuestions, LocalDateTime dateTime) {
        this.playerName = playerName;
        this.totalQuestions = totalQuestions;
        this.correctQuestions = correctQuestions;
        this.dateTime = dateTime;
    }

    public GameResult() {
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getCorrectQuestions() {
        return correctQuestions;
    }

    public void setCorrectQuestions(Integer correctQuestions) {
        this.correctQuestions = correctQuestions;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
