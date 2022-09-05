package net.straxidus.dtm.game;

import net.straxidus.dtm.DTM;
import net.straxidus.dtm.game.countdowns.Countdown;
import net.straxidus.dtm.game.countdowns.EndCountdown;
import net.straxidus.dtm.game.countdowns.StartCountdown;
import net.straxidus.dtm.map.DTMMap;
import net.straxidus.dtm.map.Monument;
import net.straxidus.dtm.map.TeamMapData;
import net.straxidus.dtm.Messages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Game {
    private Scoreboard gameSb = Bukkit.getScoreboardManager().getNewScoreboard();
    private DTMMap map;
    private GameState state;
    private List<UUID> spectators = new ArrayList<>();
    private List<UUID> teamAPlayers = new ArrayList<>();
    private List<UUID> teamBPlayers = new ArrayList<>();
    private int time = 10;
    private Countdown cd;
    private int id;

    public Game(DTMMap map, int id) {
        this.map = map.createLiveMap();
        this.id = id;
        state = GameState.LOBBY;
        createScoreboard();
    }

    public Game(int id) {
        this(DTM.dtm.getMapManager().getRandomMap(), id);
    }

    public void addPlayer(Player p) {
        p.teleport(map.getSpawn());
        addSpectator(p);
        p.setScoreboard(gameSb);
        // todo scoreboard
    }

    /**
     * Removes player from the game to the lobby
     * @param p
     */
    public void removePlayer(Player p) {
        if (!removeSpectator(p)) {
            teamAPlayers.remove(p.getUniqueId());
            teamBPlayers.remove(p.getUniqueId());
            showSpectatorsToPlayer(p);
        } else {
            showSpectator(p);
        }

        DTM.dtm.getLobby().teleport(p);
        checkIfEnoughPlayers();
    }

    public void moveFromTeamToSpectator(Player p) {
        teamAPlayers.remove(p.getUniqueId());
        teamBPlayers.remove(p.getUniqueId());
        addSpectator(p);
        showSpectatorsToPlayer(p);
        checkIfEnoughPlayers();
    }

    public Team moveSpectatorToTeam(Player p) {
        if (spectators.remove(p.getUniqueId())) {
            Team team;
            if (teamAPlayers.size() < teamBPlayers.size()) {
                teamAPlayers.add(p.getUniqueId());
                team = Team.A;
            } else {
                teamBPlayers.add(p.getUniqueId());
                team = Team.B;
            }
            if (state == GameState.LOBBY) {
                if (getNumTeamPlayers() == map.getMinPlayers())
                    start();
            } else if (state == GameState.IN_PROGRESS) {
                initialPlayerSpawn(p);
            }
            return team;
        } else
            return null;
    }

    public void start() {
        state = GameState.STARTING;
        cd = ((Countdown) new StartCountdown(10, this));
        cd.runTaskTimer(DTM.dtm, 0, 20L);
        DTM.dtm.getLobby().updateGameList();
    }

    public void beginMatch() {
        state = GameState.IN_PROGRESS;
        sendMessage(Messages.matchBegun);
        playSound(Sound.ENTITY_ENDER_DRAGON_GROWL);
        for (UUID id : teamAPlayers) {
            initialPlayerSpawn(Bukkit.getPlayer(id));
        }
        for (UUID id : teamBPlayers) {
            initialPlayerSpawn(Bukkit.getPlayer(id));
        }
        DTM.dtm.getLobby().updateGameList();
    }

    public void endMatch() {
        state = GameState.ENDING;
        for (UUID id : spectators)
            showSpectator(Bukkit.getPlayer(id));
        for (UUID id : teamAPlayers) {
            addSpectator(Bukkit.getPlayer(id));
        }
        for (UUID id : teamBPlayers) {
            addSpectator(Bukkit.getPlayer(id));
        }
        cd = (Countdown) new EndCountdown(10, this);
        cd.runTaskTimer(DTM.dtm, 0, 20L);
        DTM.dtm.getLobby().updateGameList();
    }

    public void resetMap() {
        state = GameState.RESETTING;
        for (Player p : map.getWorld().getPlayers())
            DTM.dtm.getGameManager().removePlayerFromGame(p);
        map.delete();
        DTM.dtm.getGameManager().removeGame(id);
    }

    public void giveLoadout(Player p) {
        TeamMapData teamMapData = map.getTeamBData();
        if (getPlayerTeam(p) == Team.A)
            teamMapData = map.getTeamAData();
        for (Map.Entry<Integer, ItemStack> item : teamMapData.getLoadout().getItems().entrySet()) {
            ItemStack currItem = item.getValue();
            ItemMeta itemMeta = currItem.getItemMeta();
            int damage = ((Damageable) itemMeta).getDamage();
            ((Damageable) itemMeta).setDamage(0);
            currItem.setItemMeta(itemMeta);
            // tfw setting durability only works on items in the inventory
            p.getInventory().setItem(item.getKey(), item.getValue());
            if (damage != 0) {
                currItem = p.getInventory().getItem(item.getKey());
                itemMeta = currItem.getItemMeta();
                ((Damageable) itemMeta).setDamage(currItem.getType().getMaxDurability() - damage);
                currItem.setItemMeta(itemMeta);
            }
        }
    }

    public void removeCountdown() {
        cd.cancel();
        cd = null;
    }

    public boolean allTeamMonumentsBroken(Team t) {
        List<Monument> mons = map.getMonuments(t);
        for (Monument mon : mons) {
            if (!mon.isBroken()) return false;
        }
        return true;
    }

    private void addSpectator(Player p) {
        spectators.add(p.getUniqueId());
        p.setGameMode(GameMode.ADVENTURE);
        p.setCanPickupItems(false);
        p.setAllowFlight(true);
        p.setFlying(true);
        p.getInventory().clear();
        p.setHealth(20);
        p.setFoodLevel(20);
        if (state == GameState.IN_PROGRESS)
            hideSpectator(p);
        // TODO give spectator items
    }

    private boolean removeSpectator(Player p) {
        p.setCanPickupItems(true);
        p.setFlying(false);
        p.getInventory().clear();
        p.setGameMode(GameMode.SURVIVAL);
        p.setHealth(20);
        p.setFoodLevel(20);
        return spectators.remove(p.getUniqueId());
    }

    private void initialPlayerSpawn(Player p) {
        removeSpectator(p);
        p.setNoDamageTicks(40);
        p.teleport(map.getTeamData(getPlayerTeam(p)).getSpawn());
        hideSpectatorsFromPlayer(p);
        showSpectator(p);
        giveLoadout(p);
    }

    private void checkIfEnoughPlayers() {
        if (getNumTeamPlayers() < map.getMinPlayers()) {
            // start cd is active
            if (state == GameState.STARTING && cd instanceof StartCountdown) {
                sendMessage(Messages.matchStartCanceled);
                removeCountdown();
                state = GameState.LOBBY;
            } else if (state == GameState.IN_PROGRESS) {
                sendMessage(Messages.gameCanceled);
                endMatch();
            }
        }
    }

    private void hideSpectator(Player spectator) {
        for (UUID id : teamAPlayers) {
            Player p = Bukkit.getPlayer(id);
            p.hidePlayer(DTM.dtm, spectator);
        }
        for (UUID id : teamBPlayers) {
            Player p = Bukkit.getPlayer(id);
            p.hidePlayer(DTM.dtm, spectator);
        }
    }

    private void showSpectator(Player spectator) {
        for (UUID id : teamAPlayers) {
            Player p = Bukkit.getPlayer(id);
            p.showPlayer(DTM.dtm, spectator);
        }
        for (UUID id : teamBPlayers) {
            Player p = Bukkit.getPlayer(id);
            p.showPlayer(DTM.dtm, spectator);
        }
    }

    private void hideSpectatorsFromPlayer(Player p) {
        for (UUID id : spectators)
            p.hidePlayer(DTM.dtm, Bukkit.getPlayer(id));
    }

    private void showSpectatorsToPlayer(Player p) {
        for (UUID id : spectators)
            p.showPlayer(DTM.dtm, Bukkit.getPlayer(id));
    }

    public void updateScoreboard() {
        createScoreboard();
        for (Player p : map.getWorld().getPlayers()) {
            p.setScoreboard(gameSb);
        }
    }

    private void createScoreboard() {
        DateFormat df = new SimpleDateFormat("MM/dd/yy");
        Date date = new Date();
        int i = 1;
        gameSb =  Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = gameSb.registerNewObjective("GameInfo", "dummy", "GameInfo");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(map.getName());
        objective.getScore("§ewww.notawebsite.com").setScore(i);
        i++;
        objective.getScore("§c").setScore(i); // empty line
        i++;
        // display team b's monument status
        for(Monument mon : map.getTeamBData().getMonuments()){
            if(mon.isBroken()){
                objective.getScore("§e" + mon.getName() + " - §c✖").setScore(i);
            } else {
                objective.getScore("§e" + mon.getName() + " - §a✔").setScore(i);
            }
            i++;
        }

        // display team b's name in rgb on scoreboard
        org.bukkit.scoreboard.Team t = gameSb.getTeam(map.getTeamBData().getName()) == null ? gameSb.registerNewTeam(map.getTeamBData().getName()) : gameSb.getTeam(map.getTeamBData().getName());
        t.setSuffix(map.getTeamBData().getTeamChatColor() + map.getTeamBData().getName());
        t.addEntry("§b");
        objective.getScore("§b").setScore(i);
        i++;
        objective.getScore("§r§r").setScore(i); //empty line
        i++;
        // display team a's monument status
        for(Monument mon : map.getTeamAData().getMonuments()){
            if(mon.isBroken()){
                objective.getScore("§r§e" + mon.getName() + " - §c✖").setScore(i);
            } else {
                objective.getScore("§r§e" + mon.getName() + " - §a✔").setScore(i);
            }
            i++;
        }

        // display team a's name in rgb on scoreboard
        t = gameSb.getTeam(map.getTeamAData().getName()) == null ? gameSb.registerNewTeam(map.getTeamAData().getName()) : gameSb.getTeam(map.getTeamAData().getName());
        t.setSuffix(map.getTeamAData().getTeamChatColor() + map.getTeamAData().getName());
        t.addEntry("§a");
        objective.getScore("§a").setScore(i);
        i++;
        objective.getScore("§r").setScore(i); //empty line
        i++;
        objective.getScore("§7Casual DTM - " + df.format(date)).setScore(i); // the date
    }

    public void playSound(Sound sound) {
        for(Player p: map.getWorld().getPlayers()){
            p.playSound(p.getLocation(), sound, 1, 1);
        }
    }
    public void sendMessage(String msg){
        for(Player p: map.getWorld().getPlayers()){
            p.sendMessage(msg);
        }
    }

    public Team getPlayerTeam(Player p) {
        return getPlayerTeam(p.getUniqueId());
    }

    public Team getPlayerTeam(UUID id) {
        if (teamAPlayers.contains(id))
            return Team.A;
        else if (teamBPlayers.contains(id))
            return Team.B;
        return null;
    }

    public int getNumTeamPlayers() { return teamAPlayers.size() + teamBPlayers.size(); }

    public int getTotalNumPlayers() {
        return spectators.size() + getNumTeamPlayers();
    }

    public int getId() {
        return id;
    }

    public DTMMap getMap() {
        return map;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public List<UUID> getSpectators() {
        return spectators;
    }

    public List<UUID> getTeamAPlayers() {
        return teamAPlayers;
    }

    public List<UUID> getTeamBPlayers() {
        return teamBPlayers;
    }
}
