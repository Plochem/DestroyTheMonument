package net.straxidus.dtm.game.listeners;

import net.straxidus.dtm.DTM;
import net.straxidus.dtm.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class OtherPlayerListeners implements Listener {
    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        DTM.dtm.getGameManager().removePlayerFromGame(e.getPlayer());
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player)e.getEntity();
            Game g = DTM.dtm.getGameManager().getGamePlayerIn(p.getUniqueId());
            if (g != null && g.getSpectators().contains(p.getUniqueId()))
                e.setCancelled(true);
        }
    }
}
