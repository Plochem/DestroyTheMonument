package net.straxidus.dtm.cosmetics.types;

import net.straxidus.dtm.cosmetics.collection.CollectionType;
import net.straxidus.dtm.cosmetics.projectiletrails.ProjectileTrail;
import net.straxidus.dtm.cosmetics.projectiletrails.ProjectileTrailBlackSmoke;

import java.util.ArrayList;
import java.util.List;

public class ProjectileTrailType extends CosmeticType {
    private final static String configFile = "projectile-trails.yml";
    public static List<ProjectileTrailType> VALUES = new ArrayList<>();
    public static ProjectileTrailType BLACK_SMOKE = new ProjectileTrailType(ProjectileTrailBlackSmoke.class, "BLACK_SMOKE", "black smoke");;

    private ProjectileTrailType(Class<? extends ProjectileTrail> clazz, String configSection, String name) {
        super(CollectionType.PROJECTILE_TRAIL, clazz, configFile, configSection, name);
        VALUES.add(this);
    }

    public static List<ProjectileTrailType> values() {
        return VALUES;
    }

    //https://github.com/iSach/UltraCosmetics/blob/master/core/src/main/java/be/isach/ultracosmetics/cosmetics/type/ParticleEffectType.java
}
