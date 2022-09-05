package net.straxidus.dtm.shop;

import net.straxidus.dtm.cosmetics.types.CosmeticType;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

import java.util.UUID;

public class CosmeticShopItem extends ShopItem {

    private CosmeticType cosmeticType;


    public CosmeticShopItem(ItemStack itemNotBought, int cost, CosmeticType cosmeticType) {
        super(itemNotBought, Currency.TOKENS, cost);
        this.cosmeticType = cosmeticType;
    }

    @Override
    public void handleValidPurchase(UUID purchaser) {
        System.out.println("purchased");
        // give permission
    }

    public CosmeticType getCosmeticType() {
        return cosmeticType;
    }
}
