package net.straxidus.dtm.util.menu;

import net.straxidus.dtm.DTM;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class MenuListener implements Listener {
    @EventHandler
    public void invClick(InventoryClickEvent e) {
        if(!(e.getInventory().getHolder() instanceof InventoryUUIDHolder) || e.getClickedInventory() == null || e.getCurrentItem() == null) return;
        UUID menuId = ((InventoryUUIDHolder)e.getInventory().getHolder()).getInvID();
        Menu menu = DTM.dtm.getMenuManager().getCustomMenus().get(menuId);
        if(menu == null) return;
        if(e.getInventory().equals(menu.getInventory())) {
            e.setCancelled(true);
            if(e.getClickedInventory().getType() == InventoryType.PLAYER && menu.getClickAction() != null) {
                menu.getClickAction().execute(e, menu);
            }
            for(MenuButton button : menu.getButtons()) { // possibly remove the slot check
                if(e.getClickedInventory().equals(menu.getInventory()) && e.getCurrentItem().equals(button.getItem()) && e.getSlot() == button.getSlot()) {
                    button.execute(e, menu);
                    ((Player)e.getWhoClicked()).updateInventory();
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if(!(e.getInventory().getHolder() instanceof InventoryUUIDHolder)) return;
        UUID menuId = ((InventoryUUIDHolder)e.getInventory().getHolder()).getInvID();
        Menu menu = DTM.dtm.getMenuManager().getCustomMenus().get(menuId);
        if(menu == null || menu.getCloseAction() == null) return;
        if (menu.isDeleteOnClose() && menu instanceof MenuPage) {
            MenuPage curr = (MenuPage)menu;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!curr.getParentMenu().hasViewerOnAnyPage()) {
                        curr.getParentMenu().delete();
                    }
                }
            }.runTaskLater(DTM.dtm, 1);
            return;
        }
        menu.getCloseAction().execute(e, menu);
    }
}
