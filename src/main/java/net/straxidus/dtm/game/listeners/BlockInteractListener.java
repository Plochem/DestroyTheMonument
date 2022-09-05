package net.straxidus.dtm.game.listeners;

import net.straxidus.dtm.DTM;
import net.straxidus.dtm.events.MonumentBreakEvent;
import net.straxidus.dtm.game.Game;
import net.straxidus.dtm.game.GameState;
import net.straxidus.dtm.map.Monument;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockInteractListener implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Game game = DTM.dtm.getGameManager().getGamePlayerIn(e.getPlayer().getUniqueId());
        if (game != null) { // block break in a game
            Player p = e.getPlayer();
            if (game.getSpectators().contains(p.getUniqueId()) || game.getState() != GameState.IN_PROGRESS) {
                e.setCancelled(true);
                return;
            }
            Monument mon = game.getMap().getMonument(e.getBlock().getLocation());
            if (mon != null && !mon.isBroken()) {
                MonumentBreakEvent mbe = new MonumentBreakEvent(mon, game, p);
                Bukkit.getPluginManager().callEvent(mbe);
                if (mbe.isCancelled()) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Game game = DTM.dtm.getGameManager().getGamePlayerIn(e.getPlayer().getUniqueId());
        if (game != null) { // block place in a game
            if (game.getSpectators().contains(e.getPlayer().getUniqueId()) || game.getState() != GameState.IN_PROGRESS) e.setCancelled(true);
        }

    }


}
