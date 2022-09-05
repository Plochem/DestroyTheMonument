package net.straxidus.dtm.util.menu;

import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class ClickMenuAction {

    public abstract void execute(InventoryClickEvent e, Menu menu);

}
