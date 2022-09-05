package net.straxidus.dtm.game.listeners;

import net.straxidus.dtm.DTM;
import net.straxidus.dtm.game.Game;
import net.straxidus.dtm.game.GameState;
import net.straxidus.dtm.game.Team;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.UUID;

public class PlayerCombatListener implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Game game = DTM.dtm.getGameManager().getGamePlayerIn(p.getUniqueId());
        if (game != null) {
            EntityDamageEvent.DamageCause cause = p.getLastDamageCause().getCause();

        }
    }

    @EventHandler
    public void onDeath(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        Game game = DTM.dtm.getGameManager().getGamePlayerIn(p.getUniqueId());
        if (game != null) {
            e.setRespawnLocation(game.getMap().getTeamData(game.getPlayerTeam(p)).getSpawn());
            game.giveLoadout(p);
            p.setNoDamageTicks(80);
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if ((e.getEntity() instanceof Player)){ // the person attacked is a player
            UUID attacker;
            if (e.getDamager() instanceof Player) {
                attacker = ((Player)e.getDamager()).getUniqueId();
            } else if (e.getDamager() instanceof Projectile){
                ProjectileSource shooter = ((Projectile)e.getDamager()).getShooter();
                if (shooter instanceof Player) {
                    attacker = ((Player)shooter).getUniqueId();
                } else {
                    return;
                }
            } else {
                return;
            }
            UUID attacked = ((Player)e.getEntity()).getUniqueId();
            Game attackerGame = DTM.dtm.getGameManager().getGamePlayerIn(attacker);
            Game attackedGame = DTM.dtm.getGameManager().getGamePlayerIn(attacked);
            if (attackerGame != null && attackedGame != null && attackerGame.equals(attackedGame)) { // both players are in same existing game
                Team attackerTeam = attackerGame.getPlayerTeam(attacker);
                Team attackedTeam = attackerGame.getPlayerTeam(attacked);
                if (attackedTeam == null || attackerTeam == null || attackerTeam == attackedTeam) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onHit(EntityDamageEvent e) { // cancel all damage to a player if player is a spectator or if game isn't in progress
        if(e.getEntity() instanceof Player) {
            UUID damaged = ((Player) e.getEntity()).getUniqueId();
            Game game = DTM.dtm.getGameManager().getGamePlayerIn(damaged);
            if (game != null && (game.getState() != GameState.IN_PROGRESS || game.getSpectators().contains(damaged))) {
                e.setCancelled(true);
            }
        }
    }
}
