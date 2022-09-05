package net.straxidus.dtm.listeners;

import net.straxidus.dtm.DTM;
import net.straxidus.dtm.database.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(DTM.dtm, new Runnable() {
            @Override
            public void run() {
                if(e.getPlayer().isDead()) {
                    e.getPlayer().spigot().respawn();
                }
                DTM.dtm.getCachedPlayerData().put(e.getPlayer().getUniqueId(), new PlayerData()); // test
                DTM.dtm.getLobby().teleport(e.getPlayer());
            }
        }, 10L);
    }
}
