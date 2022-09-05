package net.straxidus.dtm;

import net.straxidus.dtm.util.ChatColorUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Messages implements Reloadable {
    public static String playerToRunCmd;
    public static String invalidArgsPlayCmd;
    public static String joinAnotherGame;
    public static String alreadyInGame;
    public static String alreadyOnTeam;
    public static String cantJoinTeam;
    public static String alreadyNotInGame;
    public static String joinedTeam;
    public static String leavingGame;
    public static String nowSpectator;
    public static String matchBegun;
    public static String matchStartsIn;
    public static String matchStartsInOne;
    public static String matchStartCanceled;
    public static String matchEnded;
    public static String gameCanceled;
    public static String playerToLobbyIn;
    public static String playerToLobbyInOne;
    public static String monDestroyed;
    public static String breakOwnMon;
    public static String teamWon;
    public static String teamsAreFull;
    public static String notEnoughMoney;
    public static String haveNotUnlocked;
    public static String alreadyBought;

    @Override
    public void load() {
        YamlConfiguration c = DTM.dtm.getConfig(null, "messages.yml");
        playerToRunCmd = getString("player-to-run-command", c);
        alreadyInGame = getString("already-in-game", c);
        invalidArgsPlayCmd = getString("invalid-args-play-cmd", c);
        alreadyNotInGame = getString("already-not-in-game", c);
        alreadyOnTeam = getString("already-on-team", c);
        cantJoinTeam = getString("cannot-join-team", c);
        joinedTeam = getString("joined-team", c);
        leavingGame = getString("leaving-game", c);
        nowSpectator = getString("now-spectator", c);
        matchBegun = getString("match-has-begun", c);
        matchStartsIn = getString("match-starts-in", c);
        matchStartsInOne = getString("match-starts-in-one", c);
        matchStartCanceled = getString("match-start-canceled", c);
        matchEnded = getString("match-has-ended", c);
        gameCanceled = getString("game-canceled", c);
        playerToLobbyIn = getString("sends-player-in", c);
        playerToLobbyInOne = getString("sends-player-in-one", c);
        monDestroyed = getString("mon-destroyed", c);
        breakOwnMon = getString("break-own-mon", c);
        teamWon = getString("team-won", c);
        teamsAreFull = getString("teams-are-full", c);
        joinAnotherGame = getString("join-another-game", c);
        notEnoughMoney = getString("not-enough-money", c);
        haveNotUnlocked = getString("have-not-unlocked", c);
        alreadyBought = getString("already-bought", c);
        DTM.dtm.getLogger().info("Loaded messages from config");
    }

    @Override
    public void reload() {
        load();
    }

    private static String getString(String path, YamlConfiguration c) {
        return ChatColorUtil.colorize(c.getString(path));
    }
}
