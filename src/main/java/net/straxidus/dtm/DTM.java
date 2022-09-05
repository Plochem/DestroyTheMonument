package net.straxidus.dtm;

import net.straxidus.dtm.commands.JoinCommand;
import net.straxidus.dtm.commands.LeaveCommand;
import net.straxidus.dtm.commands.ReloadCommands;
import net.straxidus.dtm.database.PlayerData;
import net.straxidus.dtm.game.GameManager;
import net.straxidus.dtm.game.listeners.BlockInteractListener;
import net.straxidus.dtm.game.listeners.MonumentBreakListener;
import net.straxidus.dtm.game.listeners.PlayerCombatListener;
import net.straxidus.dtm.game.listeners.OtherPlayerListeners;
import net.straxidus.dtm.listeners.PlayerJoinListener;
import net.straxidus.dtm.lobby.MainLobby;
import net.straxidus.dtm.lobby.MainLobbyListeners;
import net.straxidus.dtm.map.MapManager;
import net.straxidus.dtm.util.interactableitem.InteractableItemListener;
import net.straxidus.dtm.util.menu.MenuListener;
import net.straxidus.dtm.util.menu.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DTM extends JavaPlugin {
    public static DTM dtm;
    private File mapConfigFolder = new File(getDataFolder(), "map-configs");
    private MapManager mapManager;
    private GameManager gameManager;
    private MenuManager menuManager;
    private Messages messages;
    private MainLobby lobby;
    private Map<UUID, PlayerData> cachedPlayerData = new HashMap<>();


    @Override
    public void onEnable() {
        dtm = this;
        messages = new Messages();
        mapManager = new MapManager();
        gameManager = new GameManager();
        menuManager = new MenuManager();
        lobby = new MainLobby();
        messages.load();
        registerThings();
        new BukkitRunnable() {
            @Override
            public void run() {
                mapManager.load();
                lobby.reload();
                gameManager.createGame();
            }
        }.runTaskLater(this, 1);

    }

    @Override
    public void onDisable() {

    }

    public YamlConfiguration getConfig(String dirPath, String filename) {
        File dir = DTM.dtm.getDataFolder();
        if (dirPath != null)
            dir = new File(DTM.dtm.getDataFolder(), dirPath); // DestroyTheMonument/map-configs
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dir, filename);
        System.out.println(file.getPath());
        if (!file.exists()) {
            if (dirPath == null)
                DTM.dtm.saveResource(filename, false);
            else
                DTM.dtm.saveResource(dirPath + "/" + filename, false);
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    private void registerThings() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        getCommand("join").setExecutor(new JoinCommand());
        getCommand("leave").setExecutor(new LeaveCommand());
        getCommand("dtmreload").setExecutor(new ReloadCommands());
        pm.registerEvents(new BlockInteractListener(), this);
        pm.registerEvents(new MonumentBreakListener(), this);
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new PlayerCombatListener(), this);
        pm.registerEvents(new OtherPlayerListeners(), this);
        pm.registerEvents(new MenuListener(), this);
        pm.registerEvents(new InteractableItemListener(), this);
        pm.registerEvents(new MainLobbyListeners(), this);
    }


    public MapManager getMapManager() { return mapManager; }
    public GameManager getGameManager() { return gameManager; }
    public MainLobby getLobby() { return lobby; }
    public Messages getMessages() { return messages; }
    public MenuManager getMenuManager() { return menuManager; }

    public Map<UUID, PlayerData> getCachedPlayerData() {
        return cachedPlayerData;
    }

    public PlayerData getPlayerData(UUID id) {
        return cachedPlayerData.get(id);
    }
}
