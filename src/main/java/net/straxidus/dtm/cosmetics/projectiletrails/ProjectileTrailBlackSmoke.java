package net.straxidus.dtm.cosmetics.projectiletrails;

import net.straxidus.dtm.cosmetics.types.ProjectileTrailType;
import org.bukkit.Effect;
import org.bukkit.entity.Projectile;

import java.util.UUID;

public class ProjectileTrailBlackSmoke extends ProjectileTrail {

    public ProjectileTrailBlackSmoke(Projectile projectile, UUID owner) {
        super(projectile, owner, ProjectileTrailType.BLACK_SMOKE);
    }

    @Override
    public void onUpdate() {
        getProjectile().getLocation().getWorld().playEffect(getProjectile().getLocation(), Effect.SMOKE, 10);

    }
}
