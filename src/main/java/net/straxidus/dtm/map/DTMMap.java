package net.straxidus.dtm.map;

import net.straxidus.dtm.DTM;
import net.straxidus.dtm.game.Team;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.*;
import java.util.List;
import java.util.UUID;

public class DTMMap {
    private String name;
    private String worldName;
    private World world;
    private int maxPlayers;
    private int minPlayers;
    private List<UUID> authors;
    private Location spawn;
    private TeamMapData teamA;
    private TeamMapData teamB;

    public DTMMap(String name, String worldName, World world, int minPlayers, int maxPlayers, List<UUID> authors, Location spawn, TeamMapData teamA, TeamMapData teamB) {
        this.name = name;
        this.worldName = worldName;
        this.world = world;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
        this.authors = authors;
        this.spawn = spawn;
        this.teamA = teamA;
        this.teamB = teamB;
    }

    public DTMMap createLiveMap() {
        String newWorldName = copyWorld(worldName, worldName + DTM.dtm.getGameManager().getGameId());
        if (newWorldName == null)
            return null;
        World copyWorld = loadWorld(newWorldName);
        Location copySpawn = spawn.clone();
        copySpawn.setWorld(copyWorld);
        DTMMap liveMap = new DTMMap(name, newWorldName, copyWorld, minPlayers, maxPlayers, authors, copySpawn, teamA.getCopyWithNewWorld(copyWorld), teamB.getCopyWithNewWorld(copyWorld));
        return liveMap;
    }

    public void delete() {
        Bukkit.getServer().unloadWorld(worldName, false);
        try {
            FileUtils.deleteDirectory(new File(Bukkit.getWorldContainer(), worldName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String copyWorld(String oldWorld, String newLocation) {
        try {
            File newWorldFolder = new File(Bukkit.getWorldContainer(), newLocation);
            FileUtils.copyDirectory(new File(Bukkit.getWorldContainer(), oldWorld), newWorldFolder);
            for (File f : newWorldFolder.listFiles()) {
                if (f.getName().equals("session.lock") || f.getName().equals("uid.dat"))
                    f.delete();
            }
            return newLocation;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private World loadWorld(String worldName) {
        WorldCreator worldCreator = new WorldCreator(worldName);
        World temp = Bukkit.getServer().createWorld(worldCreator);
        return temp;
    }

    private void updateWorld(World newWorld) {
        this.world = newWorld;
        spawn.setWorld(newWorld);
        teamA.updateWorld(newWorld);
        teamB.updateWorld(newWorld);
    }

    public List<Monument> getMonuments(Team t) {
        if (t == Team.A)
            return teamA.getMonuments();
        else if (t == Team.B)
            return teamB.getMonuments();
        return null;
    }

    public TeamMapData getTeamData(Team t) {
        if (t == Team.A)
            return teamA;
        else if (t == Team.B)
            return teamB;
        return null;
    }

    public Monument getMonument(Location loc) {
        Monument m = teamA.getMonument(loc);
        if (m == null) return teamB.getMonument(loc);
        return m;
    }

    public String getName() {
        return name;
    }
    public World getWorld() {
        return world;
    }
    public int getMinPlayers() {
        return minPlayers;
    }
    public int getMaxPlayers() {
        return maxPlayers;
    }
    public Location getSpawn() { return spawn; }
    public TeamMapData getTeamAData() {
        return teamA;
    }
    public TeamMapData getTeamBData() {
        return teamB;
    }
}
