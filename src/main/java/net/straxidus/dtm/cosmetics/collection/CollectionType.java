package net.straxidus.dtm.cosmetics.collection;

import net.straxidus.dtm.cosmetics.types.CosmeticType;
import net.straxidus.dtm.cosmetics.types.ProjectileTrailType;

import java.util.List;

public enum CollectionType {
    PROJECTILE_TRAIL("Projectile Trails", ProjectileTrailType.values());
//    KILL_EFFECT:
//    DEATH_SOUND:
//    MONUMENT_DESTROYED_EFFECT:
//    TRAIL_EFFECT

    public List<? extends CosmeticType> values;
    private String menuTitle;

    CollectionType(String menuTitle, List<? extends CosmeticType> values) {
        this.menuTitle = menuTitle;
        this.values = values;
    }

    public List<? extends CosmeticType> getValues() {
        return values;
    }

    public String getMenuTitle() {
        return menuTitle;
    }
}
