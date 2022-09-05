package net.straxidus.dtm.cosmetics.collection;

import net.straxidus.dtm.DTM;
import net.straxidus.dtm.Messages;
import net.straxidus.dtm.cosmetics.types.CosmeticType;
import net.straxidus.dtm.database.PlayerData;
import net.straxidus.dtm.util.menu.Menu;
import net.straxidus.dtm.util.menu.MenuButton;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CollectionItem {

    private MenuButton notSelected;
    private MenuButton selected;
    private MenuButton locked;
    private CosmeticType cosmeticType;

    public CollectionItem(ItemStack selectedItem, ItemStack notSelectedItem, ItemStack lockedItem, CosmeticType cosmeticType) {
        this.cosmeticType = cosmeticType;
        this.notSelected = new MenuButton(notSelectedItem) {
            @Override
            public void execute(InventoryClickEvent e, Menu menu) {
                PlayerData pdata = DTM.dtm.getPlayerData(e.getWhoClicked().getUniqueId());
                pdata.equipCosmetic(cosmeticType);
                menu.addButton(selected, this.getSlot());
                DTM.dtm.getLobby().getCollectionMainMenu().show((Player)e.getWhoClicked());
            }
        };
        this.selected = new MenuButton(selectedItem) {
            @Override
            public void execute(InventoryClickEvent e, Menu menu) {
                System.out.println("unequipping " + cosmeticType.getName());
                DTM.dtm.getPlayerData(e.getWhoClicked().getUniqueId()).unequipCosmetic(cosmeticType.getCategory());
                menu.addButton(notSelected, this.getSlot());
            }
        };
        this.locked = new MenuButton(lockedItem) {
            @Override
            public void execute(InventoryClickEvent e, Menu menu) {
                e.getWhoClicked().sendMessage(Messages.haveNotUnlocked);
            }
        };
    }

    public MenuButton getNotSelected() {
        return notSelected;
    }

    public MenuButton getSelected() {
        return selected;
    }

    public MenuButton getLocked() {
        return locked;
    }

    public CosmeticType getCosmeticType() {
        return cosmeticType;
    }
}
