package net.straxidus.dtm.cosmetics.projectiletrails;


import net.straxidus.dtm.cosmetics.Cosmetic;
import net.straxidus.dtm.cosmetics.Updatable;
import net.straxidus.dtm.cosmetics.types.ProjectileTrailType;
import org.bukkit.entity.Projectile;

import java.util.UUID;

public abstract class ProjectileTrail extends Cosmetic implements Updatable {

    private Projectile projectile;

    public ProjectileTrail(Projectile projectile, UUID owner, ProjectileTrailType type) {
        super(owner, type);
        this.projectile = projectile;
    }

    public Projectile getProjectile() {
        return projectile;
    }
}
