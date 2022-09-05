package net.straxidus.dtm.events;

import net.straxidus.dtm.game.Game;
import net.straxidus.dtm.map.Monument;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;

public class MonumentBreakEvent extends BlockBreakEvent {
    private static final HandlerList handlers = new HandlerList();
    private Monument monument;
    private Game game;
    private Player player;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public MonumentBreakEvent(Monument monument, Game game, Player player) {
        super(monument.getLoc().getBlock(), player);
        this.monument = monument;
        this.game = game;
        this.player = player;
    }

    public Monument getMonument() {
        return monument;
    }

    public void setMonument(Monument monument) {
        this.monument = monument;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
