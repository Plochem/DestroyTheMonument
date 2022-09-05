package net.straxidus.dtm.game;

import net.straxidus.dtm.DTM;
import org.bukkit.entity.Player;

import java.util.*;

public class GameManager {
    private Map<UUID, Game> playerToGame = new HashMap<>();
    private Map<Integer, Game> activeCasualGames = new HashMap<>();
    private Map<Integer, RankedGame> activeRankedGames = new HashMap<>();
    private int gameId = 1;

    public Game createGame() {
        Game newGame = new Game(gameId);
        activeCasualGames.put(gameId, newGame);
        gameId++;
        DTM.dtm.getLobby().updateGameList();
        return newGame;
    }

    public void removeGame(int id) {
        activeCasualGames.remove(id);
        createGame();
    }

    public RankedGame createRankedGame() {
        RankedGame newGame = new RankedGame(gameId);
        activeRankedGames.put(gameId, newGame);
        gameId++;
        return newGame;
    }

    public Game removePlayerFromGame(Player p) {
        Game game = getGamePlayerIn(p.getUniqueId());
        if (game == null) {
            return null;
        }
        playerToGame.remove(p.getUniqueId());
        game.removePlayer(p);
        return game;
    }

    public Game addPlayerToCasual(Player p, int id) {
        Game game = activeCasualGames.get(id);
        if (game == null) return null;
        if (getGamePlayerIn(p.getUniqueId()) != null && getGamePlayerIn(p.getUniqueId()).getId() == id) return null; // joining game player is already in
        game.addPlayer(p);
        playerToGame.put(p.getUniqueId(), game);
        return game;
    }
    // another with jsut id
    public Game stopGame(Player p) {
        Game game = getGamePlayerIn(p.getUniqueId());
        if (game == null)
            return null;
        game.endMatch();
        return game;
    }

    //TODO find game w/ similar rank
    public Game findRankedGame() {
        Game curr = null;
        int max = -1;
        for (RankedGame game : activeRankedGames.values()) {
            if ((game.getState() == GameState.LOBBY || game.getState() == GameState.STARTING) && game.getTotalNumPlayers() > max) {
                curr = game;
                max = game.getTotalNumPlayers();
            }
        }
        if (curr != null) return curr;
        return createRankedGame();
    }

    public void reloadGames() {

    }

    public Game  getGamePlayerIn(UUID id) {
        return playerToGame.get(id);
    }

    public int getGameId() {
        return gameId;
    }

    public Map<Integer, Game> getActiveCasualGames() {
        return activeCasualGames;
    }

    public Map<Integer, RankedGame> getActiveRankedGames() {
        return activeRankedGames;
    }
}
