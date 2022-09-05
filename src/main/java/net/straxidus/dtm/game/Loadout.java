package net.straxidus.dtm.game;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.HashMap;
import java.util.Map;

public class Loadout {
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;

    private Map<Integer, ItemStack> items = new HashMap<>();
    private final int helmetSlot = 39;
    private final int chestplateSlot = 38;
    private final int leggingsSlot = 37;
    private final int bootsSlot = 36;

    public void addItem(ItemStack item, int slot) {
        items.put(slot, item);
        if (slot == helmetSlot)
            helmet = item;
        else if (slot == chestplateSlot)
            chestplate = item;
        else if (slot == leggingsSlot)
            leggings = item;
        else if (slot == bootsSlot)
            boots = item;
    }

    public void colorLeatherGear(Color color) {
        for (ItemStack item : getArmor()) {
            if (item != null && item.getItemMeta() instanceof LeatherArmorMeta) {
                LeatherArmorMeta meta = (LeatherArmorMeta)item.getItemMeta();
                meta.setColor(color);
                item.setItemMeta(meta);
            }
        }
    }

    public ItemStack[] getArmor() {
        return new ItemStack[]{helmet, chestplate, leggings, boots};
    }
    public ItemStack getHelmet() {
        return helmet;
    }
    public ItemStack getChestplate() {
        return chestplate;
    }
    public ItemStack getLeggings() {
        return leggings;
    }
    public ItemStack getBoots() {
        return boots;
    }
    public Map<Integer, ItemStack> getItems() {
        return items;
    }
}
