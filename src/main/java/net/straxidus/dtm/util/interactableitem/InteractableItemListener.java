package net.straxidus.dtm.util.interactableitem;

import net.straxidus.dtm.util.NBTExplorer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class InteractableItemListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        String uuidStr = NBTExplorer.getNBTString(e.getItem(), "item_id");
        System.out.println(uuidStr);
        if (uuidStr == null) return;
        UUID itemId = UUID.fromString(uuidStr);
        InteractableItem item = InteractableItem.items.get(itemId);
        if (item == null) return;
        if ((e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) && item.getLeftClickAction() != null) {
            item.getLeftClickAction().execute(e, item);
        }
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && item.getRightClickAction() != null) {
            item.getRightClickAction().execute(e, item);
        }
    }
}
