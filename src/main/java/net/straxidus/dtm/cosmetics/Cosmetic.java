package net.straxidus.dtm.cosmetics;

import net.straxidus.dtm.cosmetics.types.CosmeticType;

import java.util.UUID;

public class Cosmetic {

    private UUID owner;
    private CosmeticType type;

    public Cosmetic(UUID owner, CosmeticType type) {
        this.owner = owner;
        this.type = type;
    }

    public UUID getOwner() {
        return owner;
    }

    public CosmeticType getType() {
        return type;
    }
}
