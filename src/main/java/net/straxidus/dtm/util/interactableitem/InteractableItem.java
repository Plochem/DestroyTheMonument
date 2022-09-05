package net.straxidus.dtm.util.interactableitem;

import net.straxidus.dtm.util.ChatColorUtil;
import net.straxidus.dtm.util.ItemBuilder;
import net.straxidus.dtm.util.NBTExplorer;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InteractableItem {
    public static Map<UUID, InteractableItem> items = new HashMap<>();
    private ItemStack item;
    private InteractAction leftClickAction;
    private InteractAction rightClickAction;

    public InteractableItem(InteractAction clickAction) {
        this(new ItemStack(Material.STONE), clickAction, clickAction);
    }

    public InteractableItem(ItemStack item, InteractAction clickAction) {
        this(item, clickAction, clickAction);
    }

    public InteractableItem(ItemStack item, InteractAction leftClickAction, InteractAction rightClickAction) {
        this.setItem(item);
        this.leftClickAction = leftClickAction;
        this.rightClickAction = rightClickAction;
        System.out.println("get generated id: " + getUUID().toString());
    }

    public UUID getUUID() {
        return UUID.fromString(NBTExplorer.getNBTString(item, "item_id"));
    }

    public void setItem(ItemStack item) {
        if (this.item != null)
            items.remove(getUUID());
        this.item = item;
        NBTExplorer.setNBTString(item, "item_id", UUID.randomUUID().toString());
        items.put(getUUID(), this);
    }

    public ItemStack getItem() {
        return item;
    }

    public InteractAction getLeftClickAction() {
        return leftClickAction;
    }

    public InteractAction getRightClickAction() {
        return rightClickAction;
    }

    public void setLeftClickAction(InteractAction leftClickAction) {
        this.leftClickAction = leftClickAction;
    }

    public void setRightClickAction(InteractAction rightClickAction) {
        this.rightClickAction = rightClickAction;
    }
}
