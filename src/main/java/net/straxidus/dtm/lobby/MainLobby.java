package net.straxidus.dtm.lobby;

import net.straxidus.dtm.DTM;
import net.straxidus.dtm.Messages;
import net.straxidus.dtm.Reloadable;
import net.straxidus.dtm.cosmetics.collection.CollectionMenu;
import net.straxidus.dtm.cosmetics.collection.CollectionType;
import net.straxidus.dtm.cosmetics.types.ProjectileTrailType;
import net.straxidus.dtm.database.PlayerData;
import net.straxidus.dtm.game.Game;
import net.straxidus.dtm.game.GameState;
import net.straxidus.dtm.util.ItemBuilder;
import net.straxidus.dtm.util.interactableitem.InteractAction;
import net.straxidus.dtm.util.interactableitem.InteractableItem;
import net.straxidus.dtm.util.menu.Menu;
import net.straxidus.dtm.util.menu.MenuButton;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

public class MainLobby implements Reloadable {
    private Location loc;
    private Menu gameList;
    private Menu shop;
    private Menu collectionMainMenu;
    private InteractableItem collectionItem;
    private int collectionSlot;
    private InteractableItem gameListItem;
    private int gameListSlot;
    private InteractableItem shopItem;
    private int shopSlot;

    public MainLobby() {
        gameList = new Menu("Click to join a game", 45, false);
        shop = new Menu("Shop", 54, false);
        collectionMainMenu = new Menu("Collection", 54, false);
        shopItem = new InteractableItem(new InteractAction() {
            @Override
            public void execute(PlayerInteractEvent e, InteractableItem item) {
                shop.show(e.getPlayer());
            }
        });
        gameListItem = new InteractableItem(new InteractAction() {
            @Override
            public void execute(PlayerInteractEvent e, InteractableItem item) {
                gameList.show(e.getPlayer());
            }
        });
        collectionItem = new InteractableItem(new InteractAction() {
            @Override
            public void execute(PlayerInteractEvent e, InteractableItem item) {
                collectionMainMenu.show(e.getPlayer());
            }
        });
    }

    @Override
    public void load() {
        createMainLobbyItems();
        createCollectionMainMenu();
        createDailyShopMenu();
        DTM.dtm.getLogger().info("Loaded main lobby from config");
    }

    private void createMainLobbyItems() {
        YamlConfiguration c = DTM.dtm.getConfig(null, "main-lobby-config.yml");
        ConfigurationSection spawnLocSec = c.getConfigurationSection("spawn-location");
        ConfigurationSection shopSec = c.getConfigurationSection("shop-item");
        ConfigurationSection gameListSec = c.getConfigurationSection("gamelist-item");
        ConfigurationSection collectionSec = c.getConfigurationSection("collection-item");
        this.loc = new Location(Bukkit.getWorld(spawnLocSec.getString("world-name")), spawnLocSec.getDouble("x"), spawnLocSec.getDouble("y"), spawnLocSec.getDouble("z"), (float)spawnLocSec.getDouble("yaw"), (float)spawnLocSec.getDouble("pitch"));
        shopItem.setItem(new ItemBuilder(shopSec).build());
        shopSlot = shopSec.getInt("slot");
        gameListItem.setItem(new ItemBuilder(gameListSec).build());
        gameListSlot = gameListSec.getInt("slot");
        collectionItem.setItem(new ItemBuilder(collectionSec).build());
        collectionSlot = collectionSec.getInt("slot");
    }

    private void createCollectionMainMenu() {
        YamlConfiguration collectionLayoutConfig = DTM.dtm.getConfig(null, "collection-main-menu-layout.yml");
        for (String key : collectionLayoutConfig.getKeys(false)) {
            ConfigurationSection sec = collectionLayoutConfig.getConfigurationSection(key);
            collectionMainMenu.addButton(new MenuButton(new ItemBuilder(sec).build()) {
                @Override
                public void execute(InventoryClickEvent e, Menu menu) {
                    new CollectionMenu(CollectionType.valueOf(key), (Player)e.getWhoClicked()).build().show();
                    e.getWhoClicked().sendMessage("gonna open " + key);
                }
            }, sec.getInt("slot"));
        }
    }

    public void createDailyShopMenu() {
        shop.clear();
        shop.addButton(ProjectileTrailType.BLACK_SMOKE.getShopItem(), 11);
    }

    @Override
    public void reload() {
        load();
        for (Player p : loc.getWorld().getPlayers()) {
            p.getInventory().clear();
            teleport(p);
        }
    }

    public void teleport(Player p) {
        p.teleport(loc);
        p.setGameMode(GameMode.ADVENTURE);
        p.setHealth(20);
        p.setFoodLevel(20);
        showScoreboard(p);
        p.getInventory().setItem(gameListSlot, gameListItem.getItem());
        p.getInventory().setItem(shopSlot, shopItem.getItem());
        p.getInventory().setItem(collectionSlot, collectionItem.getItem());
    }

    public void updateGameList() {
        gameList.clear();
        int i = 0;
        for (Game g : DTM.dtm.getGameManager().getActiveCasualGames().values()) {
            ItemBuilder item;
            List<String> lore = new ArrayList<>();
            lore.add("");
            if (g.getState() == GameState.LOBBY || g.getState() == GameState.STARTING) {
                item = new ItemBuilder(Material.LIME_WOOL).displayname("§a" + g.getMap().getName());
                lore.add("§aWaiting for players...");
                lore.add("");
                lore.add("§eClick to join!");
            } else if (g.getState() == GameState.IN_PROGRESS) {
                item = new ItemBuilder(Material.YELLOW_WOOL).displayname("§e" + g.getMap().getName());
                lore.add("§eIn progress...");
                lore.add("");
                lore.add("§eClick to join!");
            } else { // ending/resetting
                item = new ItemBuilder(Material.RED_WOOL).displayname("§c" + g.getMap().getName());
                lore.add("§cEnding...");
            }
            item.lore(lore);
            gameList.addButton(new MenuButton(item.build()) {
                @Override
                public void execute(InventoryClickEvent e, Menu menu) {
                    if (g.getState() == GameState.ENDING || g.getState() == GameState.RESETTING) {
                        e.getWhoClicked().sendMessage(Messages.joinAnotherGame);
                    } else {
                        DTM.dtm.getGameManager().addPlayerToCasual((Player)e.getWhoClicked(), g.getId());
                    }
                }
            }, i);
        }
    }

    public void showScoreboard(Player p) {
        PlayerData pdata = DTM.dtm.getPlayerData(p.getUniqueId());
        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = sb.registerNewObjective("Lobby", "dummy", "Lobby");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§e§lDestroy the Monument");
        objective.getScore("§a").setScore(12);
        objective.getScore("Current level: §3" + 135).setScore(11);
        objective.getScore("§b").setScore(10);
        objective.getScore("Progress: §3" + 892 + "§7/§a" + 2000).setScore(9);
        objective.getScore("§c").setScore(8);
        objective.getScore("Kills: §a" + pdata.getKills()).setScore(7);
        objective.getScore("Destroyed Monuments: §a" + 3).setScore(6);
        objective.getScore("Wins: §a" + 10).setScore(5);
        objective.getScore("§d").setScore(4);
        objective.getScore("Tokens: §2" + pdata.getTokens()).setScore(3);
        objective.getScore("§e").setScore(2);
        objective.getScore("§ewww.notawebsite.com").setScore(1);
        p.setScoreboard(sb);
    }


    public Location getLoc() {
        return loc;
    }

    public void setLobby(Location loc) {
        this.loc = loc;
    }

    public Menu getGameList() {
        return gameList;
    }

    public Menu getShop() {
        return shop;
    }

    public Menu getCollectionMainMenu() {
        return collectionMainMenu;
    }
}
