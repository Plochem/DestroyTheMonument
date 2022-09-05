package net.straxidus.dtm.util;

import net.straxidus.dtm.DTM;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class NBTExplorer {

    public static void setNBTString(ItemStack item, String key, String value) {
        ItemMeta im = item.getItemMeta();
        im.getPersistentDataContainer().set(new NamespacedKey(DTM.dtm, key), PersistentDataType.STRING, value);
        item.setItemMeta(im);
    }

    public static void setNBTInteger(ItemStack item, String key, int value) {
        ItemMeta im = item.getItemMeta();
        im.getPersistentDataContainer().set(new NamespacedKey(DTM.dtm, key), PersistentDataType.INTEGER, value);
        item.setItemMeta(im);
    }

    public static void setNBTBoolean(ItemStack item, String key, boolean value) {
        setNBTInteger(item, key, value == true ? 1 : 0);
    }

    public static String getNBTString(ItemStack item, String key) {
        return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(DTM.dtm, key), PersistentDataType.STRING);
    }

    public static Integer getNBTInteger(ItemStack item, String key) {
        return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(DTM.dtm, key), PersistentDataType.INTEGER);
    }

    public static Boolean getNBTBoolean(ItemStack item, String key) {
        int result = getNBTInteger(item, key);
        return result == 0 ? false : true;
    }

    public static Double getNBTDouble(ItemStack item, String key) {
        return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(DTM.dtm, key), PersistentDataType.DOUBLE);
    }
}
