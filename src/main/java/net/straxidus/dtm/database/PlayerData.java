package net.straxidus.dtm.database;

import net.straxidus.dtm.cosmetics.collection.CollectionType;
import net.straxidus.dtm.cosmetics.types.CosmeticType;
import net.straxidus.dtm.cosmetics.types.ProjectileTrailType;
import net.straxidus.dtm.shop.Currency;

import java.util.Random;

public class PlayerData {
    private int coins;
    private int deaths;
    private int kills;
    private int tokens;
    private int wins;
    private int monumentsDestroyed;

    private ProjectileTrailType selectedProjectile;

    public PlayerData() {
        this((int)(Math.random() * ((1000 - 2) + 1)) + 2, (int)(Math.random() * ((1000 - 2) + 1)) + 2, (int)(Math.random() * ((1000 - 2) + 1)) + 2,
                10, null);
    }

    public PlayerData(int kills, int deaths, int coins, int tokens, ProjectileTrailType selectedProjectile) {
        this.coins = coins;
        this.deaths = deaths;
        this.kills = kills;
        this.tokens = tokens;
        this.selectedProjectile = selectedProjectile;
    }

    public void equipCosmetic(CosmeticType cosmetic) {
        if (cosmetic instanceof ProjectileTrailType)
            selectedProjectile = (ProjectileTrailType) cosmetic;
    }

    public void unequipCosmetic(CollectionType collectionType) {
        if (collectionType == CollectionType.PROJECTILE_TRAIL)
            selectedProjectile = null;
    }

    public boolean hasSpecificEquipped(CosmeticType cosmetic) {
        if (cosmetic instanceof ProjectileTrailType)
            return cosmetic.equals(selectedProjectile);
        return false;
    }

    public CosmeticType getCosmetic(CollectionType collectionType) {
        if (collectionType == CollectionType.PROJECTILE_TRAIL)
            return selectedProjectile;
        return null;
    }

    public int getCurrencyAmount(Currency currency) {
        if (currency == Currency.TOKENS)
            return getTokens();
        else if (currency == Currency.COINS)
            return getCoins();
        return -1;
    }

    public void subtractCurrencyAmount(Currency currency, int amount) {
        addCurrencyAmount(currency, amount * -1);
    }

    public void addCurrencyAmount(Currency currency, int amount) {
        if (currency == Currency.TOKENS)
            this.tokens += amount;
        else if (currency == Currency.COINS)
            this.coins += amount;
    }

    public int getCoins() {
        return coins;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getKills() {
        return kills;
    }

    public int getTokens() {
        return tokens;
    }

    public ProjectileTrailType getSelectedProjectile() {
        return selectedProjectile;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public void setSelectedProjectile(ProjectileTrailType selectedProjectile) {
        this.selectedProjectile = selectedProjectile;
    }
}
