package Models;

import java.util.List;

public class Result {
    public String playerName;
    public List<GameResult> gameResults;

    public Result() {}

    public Result(String playerName, List<GameResult> gameResults) {
        this.playerName = playerName;
        this.gameResults = gameResults;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public List<GameResult> getGameResults() {
        return gameResults;
    }

    public void setGameResults(List<GameResult> gameResults) {
        this.gameResults = gameResults;
    }
}
