package net.straxidus.dtm.shop;

public enum ShopType {
    PROJECTILE_TRAIL("projectile-trails.yml"),
    KILL_EFFECT("projectile-trails.yml"),
    DEATH_SOUND("projectile-trails.yml"),
    MONUMENT_DESTROYED_EFFECT("projectile-trails.yml"),
    TRAIL_EFFECT("projectile-trails.yml");

    private String path;

    ShopType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
