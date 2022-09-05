package net.straxidus.dtm.util.interactableitem;


import org.bukkit.event.player.PlayerInteractEvent;

public abstract class InteractAction {
    public abstract void execute(PlayerInteractEvent e, InteractableItem item);
}
