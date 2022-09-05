package net.straxidus.dtm.util.menu;

import org.bukkit.event.inventory.InventoryCloseEvent;

public abstract class CloseMenuAction {
    public abstract void execute(InventoryCloseEvent e, Menu menu);
}
