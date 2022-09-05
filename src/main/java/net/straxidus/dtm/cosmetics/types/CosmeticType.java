package net.straxidus.dtm.cosmetics.types;

import net.straxidus.dtm.DTM;
import net.straxidus.dtm.Messages;
import net.straxidus.dtm.cosmetics.Cosmetic;
import net.straxidus.dtm.cosmetics.collection.CollectionItem;
import net.straxidus.dtm.cosmetics.collection.CollectionType;
import net.straxidus.dtm.shop.CosmeticShopItem;
import net.straxidus.dtm.util.ItemBuilder;
import net.straxidus.dtm.util.menu.Menu;
import net.straxidus.dtm.util.menu.MenuButton;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.permissions.Permission;

public abstract class CosmeticType {
    public final static String configDirectory = "cosmetic-configs";

    private CosmeticShopItem shopItem;
    private CollectionItem collectionItem;
    private MenuButton alreadyBoughtItem;
    private Class<? extends Cosmetic> clazz;
    private CollectionType category;
    private Permission permission;
    private String name;

    public CosmeticType(CollectionType category, Class<? extends Cosmetic> clazz, String configFile, String configSection, String name) {
        ConfigurationSection c = DTM.dtm.getConfig(configDirectory, configFile).getConfigurationSection(configSection);
        int cost = c.getInt("price");
        this.permission = new Permission(c.getString("permission"));
        this.shopItem = new CosmeticShopItem(new ItemBuilder(c.getConfigurationSection("shop-not-bought")).build(), cost, this);
        this.collectionItem = new CollectionItem(new ItemBuilder(c.getConfigurationSection("collection-selected")).build(),
                new ItemBuilder(c.getConfigurationSection("collection-not-selected")).build(),
                new ItemBuilder(c.getConfigurationSection("collection-locked")).build(), this);
        this.alreadyBoughtItem = new MenuButton(new ItemBuilder(c.getConfigurationSection("shop-bought")).build()) {
            @Override
            public void execute(InventoryClickEvent e, Menu menu) {
                e.getWhoClicked().sendMessage(Messages.alreadyBought);
            }
        };
        this.name = name;
        this.clazz = clazz;
        this.category = category;
    }

    public CosmeticShopItem getShopItem() {
        return shopItem;
    }

    public String getName() {
        return name;
    }

    public Permission getPermission() {
        return permission;
    }

    public CollectionItem getCollectionItem() {
        return collectionItem;
    }

    public MenuButton getAlreadyBoughtItem() {
        return alreadyBoughtItem;
    }

    public Class<? extends Cosmetic> getClazz() {
        return clazz;
    }

    public CollectionType getCategory() {
        return category;
    }

    //https://github.com/iSach/UltraCosmetics/blob/master/core/src/main/java/be/isach/ultracosmetics/cosmetics/type/CosmeticType.java
}
