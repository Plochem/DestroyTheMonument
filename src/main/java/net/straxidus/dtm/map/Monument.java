package net.straxidus.dtm.map;

import net.straxidus.dtm.game.Team;
import org.bukkit.Location;

public class Monument {
    private String name;
    private Location loc;
    private boolean isBroken;
    private Team team;

    public Monument(String name, Location loc, Team team) {
        this.loc = loc;
        this.name = name;
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }
    public Location getLoc() {
        return loc;
    }
    public void setLoc(Location loc) {
        this.loc = loc;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isBroken() {
        return isBroken;
    }
    public void setBroken(boolean broken) {
        isBroken = broken;
    }
}
