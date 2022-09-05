package net.straxidus.dtm.shop;

import net.straxidus.dtm.util.menu.Menu;

import java.util.UUID;

public class Shop extends Menu {
    private ShopType type;

    public Shop(ShopType type, String title, int size) {
        super(title, size, true);
        this.type = type;
    }

    //todo shoptype: add str param for yml file to read from
    //                              yml needs to have 2 lores for each item
    //todo shopbutton extends menubutton:
}
