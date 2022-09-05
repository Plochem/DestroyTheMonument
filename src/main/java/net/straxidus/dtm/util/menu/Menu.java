package net.straxidus.dtm.util.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.straxidus.dtm.DTM;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Menu {
    private List<MenuButton> buttons = new ArrayList<>();
    private List<ItemStack> movingItems = new ArrayList<>();
    private Inventory inv;
    private ClickMenuAction clickAction;
    private CloseMenuAction closeAction;
    private boolean deleteOnClose;

    public Menu(String title, int size, boolean deleteOnClose) {
        UUID id = UUID.randomUUID();
        inv = Bukkit.createInventory(new InventoryUUIDHolder(id), size, title);
        this.deleteOnClose = deleteOnClose;
        if (deleteOnClose) {
            this.setCloseAction(new CloseMenuAction() {
                @Override
                public void execute(InventoryCloseEvent e, Menu menu) {
                    menu.delete();
                }
            });
        }
        DTM.dtm.getMenuManager().registerCustomMenu(this);
    }

    public void updateButtons() {
        for(MenuButton button : buttons) {
            inv.setItem(button.getSlot(), button.getItem());
        }
    }

    public void show(Player p) {
        p.closeInventory();
        p.openInventory(inv);
    }

    public void clear() {
        inv.clear();
        buttons.clear();
        movingItems.clear();
    }

    public void delete() {
        UUID id = getId();
        for (HumanEntity viewer : inv.getViewers())
            viewer.closeInventory();
        clear();
        DTM.dtm.getMenuManager().deleteCustomMenu(id);
    }

    public void addButton(MenuButton button, int slot) {
        button.setSlot(slot);
        buttons.add(button);
        inv.setItem(slot, button.getItem());
    }

    public void addStationaryItem(ItemStack item, int index) {
        inv.setItem(index, item);
    }

    public void addMoveableItem(ItemStack item, int index) {
        movingItems.add(item);
        inv.setItem(index, item);
    }

    public boolean isDeleteOnClose() {
        return deleteOnClose;
    }

    public int getSize() { return inv.getSize(); }

    public UUID getId() {
        return ((InventoryUUIDHolder)inv.getHolder()).getInvID();
    }

    public Inventory getInventory() {
        return inv;
    }

    public List<MenuButton> getButtons(){
        return buttons;
    }

    public List<ItemStack> getMovingItems(){
        return movingItems;
    }

    public void setCloseAction(CloseMenuAction closeAction) {
        this.closeAction = closeAction;
    }

    public CloseMenuAction getCloseAction() {
        return closeAction;
    }

    public void setClickAction(ClickMenuAction clickAction) {
        this.clickAction = clickAction;
    }

    public ClickMenuAction getClickAction() {
        return clickAction;
    }

    public void fillEmptySpots(ItemStack item) {
        for(int i = 0; i < inv.getSize(); i++) {
            if(inv.getItem(i) == null) {
                addStationaryItem(item, i);
            }
        }

    }

}

