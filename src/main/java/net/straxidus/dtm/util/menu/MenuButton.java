package net.straxidus.dtm.util.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class MenuButton {
    private ItemStack item;
    private int slot;

    public MenuButton(ItemStack item) {
        this.item = item;
    }

    public MenuButton(ItemStack item, int slot) {
        this.item = item;
        this.slot = slot;
    }

    public abstract void execute(InventoryClickEvent e, Menu menu);

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }
}
