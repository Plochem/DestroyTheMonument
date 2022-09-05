package net.straxidus.dtm.util.menu;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

public class InventoryUUIDHolder implements InventoryHolder {
    private UUID invID;

    public InventoryUUIDHolder(UUID invID) {
        this.invID = invID;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    public UUID getInvID() {
        return invID;
    }
}
