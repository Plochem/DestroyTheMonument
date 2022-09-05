package net.straxidus.dtm.map;

import net.md_5.bungee.api.ChatColor;
import net.straxidus.dtm.game.Loadout;
import net.straxidus.dtm.game.Team;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class TeamMapData {
    private Team team;
    private String name;
    private Color teamColor;
    private Location spawn;
    private Loadout loadout;
    private List<Monument> monuments;

    public TeamMapData(Team team, String name, Color teamColor, Location spawn, Loadout loadout, List<Monument> monuments) {
        this.team = team;
        this.name = name;
        this.teamColor = teamColor;
        this.spawn = spawn;
        this.loadout = loadout;
        this.monuments = monuments;
    }

    public Team getTeam() {
        return team;
    }

    public String getName() {
        return name;
    }

    public Color getTeamColor() {
        return teamColor;
    }

    public ChatColor getTeamChatColor() {
        return ChatColor.of(String.format("#%06x", 0xFFFFFF & teamColor.asRGB()));
    }

    public Location getSpawn() {
        return spawn;
    }

    public Loadout getLoadout() {
        return loadout;
    }

    public List<Monument> getMonuments() {
        return monuments;
    }

    public TeamMapData getCopyWithNewWorld(World copyWorld) {
        Location copySpawn = spawn.clone();
        copySpawn.setWorld(copyWorld);
        List<Monument> copyMonuments = new ArrayList<>();
        for (Monument mon : monuments) {
            Location copyMonLoc = mon.getLoc().clone();
            copyMonLoc.setWorld(copyWorld);
            copyMonuments.add(new Monument(mon.getName(), copyMonLoc, mon.getTeam()));
        }
        return new TeamMapData(team, name, teamColor, copySpawn, loadout, copyMonuments);
    }

    public void updateWorld(World newWorld) {
        spawn.setWorld(newWorld);
        for (Monument mon : monuments) {
            mon.getLoc().setWorld(newWorld);
        }
    }

    public Monument getMonument(Location loc) {
        for (Monument mon : monuments) {
            if (mon.getLoc().equals(loc))
                return mon;
        }
        return null;
    }
}
