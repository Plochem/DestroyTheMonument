package net.straxidus.dtm.lobby;

import net.straxidus.dtm.DTM;
import net.straxidus.dtm.game.Game;
import net.straxidus.dtm.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class MainLobbyListeners implements Listener {
    @EventHandler
    public void onDamage(EntityDamageEvent e) { // cancel lobby damage
        if(e.getEntity() instanceof Player) {
            if (DTM.dtm.getLobby().getLoc().getWorld().getPlayers().contains((Player)e.getEntity()))
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if (DTM.dtm.getLobby().getLoc().getWorld().getPlayers().contains(e.getPlayer()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (DTM.dtm.getLobby().getLoc().getWorld().getPlayers().contains((Player) e.getWhoClicked()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        if(e.getEntity() instanceof Player) {
            if (DTM.dtm.getLobby().getLoc().getWorld().getPlayers().contains((Player) e.getEntity()))
                e.setCancelled(true);
        }
    }
}
