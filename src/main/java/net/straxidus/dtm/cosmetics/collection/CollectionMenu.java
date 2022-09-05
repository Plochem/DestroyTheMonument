package net.straxidus.dtm.cosmetics.collection;

import net.straxidus.dtm.DTM;
import net.straxidus.dtm.cosmetics.types.CosmeticType;
import net.straxidus.dtm.util.menu.MenuButton;
import net.straxidus.dtm.util.menu.PaginatedMenu;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CollectionMenu extends PaginatedMenu {

    private Player owner;
    private CollectionType type;

    private int[] validPos = {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43};

    public CollectionMenu(CollectionType type, Player owner) {
        super(type.getMenuTitle(), 54, true, 45, 53);
        this.owner = owner;
        this.type = type;
    }

    public CollectionMenu build() {
        List<MenuButton> buttons = new ArrayList<>();
        for (CosmeticType cosmeticType : type.getValues()) {
            if (owner.hasPermission(cosmeticType.getPermission())) {
                if (DTM.dtm.getPlayerData(owner.getUniqueId()).hasSpecificEquipped(cosmeticType))
                    buttons.add(cosmeticType.getCollectionItem().getSelected());
                else
                    buttons.add(cosmeticType.getCollectionItem().getNotSelected());
            } else {
                buttons.add(cosmeticType.getCollectionItem().getLocked());
            }

        }
        for (int i = 0; i < 100; i++) {
            buttons.add(type.getValues().get(0).getCollectionItem().getLocked());
        }
        createAllPages(validPos, buttons);
        return this;
    }

    public void show() {
        this.show(owner);
    }
}
