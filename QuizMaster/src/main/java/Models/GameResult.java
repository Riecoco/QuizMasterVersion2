package Models;

import java.util.Date;

public class GameResult {
    public String playerName;
    public Integer totalQuestions;
    public Integer correctQuestions;
    public Date date;

    public GameResult(String playerName, Integer totalQuestions, Integer correctQuestions, Date date) {
        this.playerName = playerName;
        this.totalQuestions = totalQuestions;
        this.correctQuestions = correctQuestions;
        this.date = date;
    }

    public GameResult() {}

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
