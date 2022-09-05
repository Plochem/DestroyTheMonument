package net.straxidus.dtm.shop;

import net.straxidus.dtm.DTM;
import net.straxidus.dtm.Messages;
import net.straxidus.dtm.database.PlayerData;
import net.straxidus.dtm.util.menu.Menu;
import net.straxidus.dtm.util.menu.MenuButton;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public abstract class ShopItem extends MenuButton {
    private Currency currency;
    private int cost;

    public ShopItem(ItemStack itemNotBought, Currency currency, int cost) {
        super(itemNotBought);
        this.currency = currency;
        this.cost = cost;
    }

    @Override
    public void execute(InventoryClickEvent e, Menu menu) {
        PlayerData pdata = DTM.dtm.getPlayerData(e.getWhoClicked().getUniqueId());
        if (pdata.getCurrencyAmount(currency) >= cost) {
            handleValidPurchase(e.getWhoClicked().getUniqueId());
            pdata.subtractCurrencyAmount(currency, cost);
        }
        else
            e.getWhoClicked().sendMessage(Messages.notEnoughMoney);
    }

    public abstract void handleValidPurchase(UUID purchaser);

    public Currency getCurrency() {
        return currency;
    }

    public int getCost() {
        return cost;
    }
}
