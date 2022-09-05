package net.straxidus.dtm.map;

import net.straxidus.dtm.DTM;
import net.straxidus.dtm.Reloadable;
import net.straxidus.dtm.game.Loadout;

import net.straxidus.dtm.game.Team;
import net.straxidus.dtm.util.ChatColorUtil;
import net.straxidus.dtm.util.ItemBuilder;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class MapManager implements Reloadable {
    private File mapDir = new File(DTM.dtm.getDataFolder(), "map-configs"); // DestroyTheMonument/map-configs
    private List<DTMMap> maps = new ArrayList<>();

    @Override
    public void load() {
        loadSampleConfig();
        for (File f : mapDir.listFiles()) {
            YamlConfiguration c = YamlConfiguration.loadConfiguration(f);
            String mapName = c.getString("name");
            String worldName = c.getString("world_name");
            if (!worldFolderExists(worldName)) {
                DTM.dtm.getLogger().info(worldName + " world was not founded!. Failed to load map: " + mapName);
                return;
            }
            int maxPlayers = c.getInt("max_players");
            int minPlayers = c.getInt("min_players_to_start");
            List<UUID> authors = c.getStringList("author_uuids").stream().map(UUID::fromString).toList();
            Location spawn = new Location(null, c.getDouble("spawn.x"), c.getDouble("spawn.y"), c.getDouble("spawn.z"),
                    (float)c.getDouble("spawn.yaw"), (float)c.getDouble("spawn.pitch"));
            TeamMapData teamAData = getTeamMapData(c, c.getConfigurationSection("teamA"), Team.A, null);
            TeamMapData teamBData = getTeamMapData(c, c.getConfigurationSection("teamB"), Team.B, null);
            maps.add(new DTMMap(mapName, worldName,null, minPlayers, maxPlayers, authors, spawn, teamAData, teamBData));
            DTM.dtm.getLogger().info("Loaded map: " + mapName);
        }
    }

    private boolean worldFolderExists(String worldName) {
        for (File f : Bukkit.getWorldContainer().listFiles()) {
            DTM.dtm.getLogger().info(f.getName());
            if (f.getName().equals(worldName))
                return true;
        }
        return false;
    }

    @Override
    public void reload() {
        maps.clear();
        load();
        DTM.dtm.getGameManager().reloadGames();
    }

    public List<DTMMap> getMaps() { return maps; };

    public DTMMap getRandomMap() {
        return maps.get(ThreadLocalRandom.current().nextInt(maps.size()));
    }

    private TeamMapData getTeamMapData(YamlConfiguration c, ConfigurationSection teamSection, Team team, World mapWorld) {
        String teamName = teamSection.getString("name");
        Color teamColor = Color.fromRGB(teamSection.getInt("team_color.r"), teamSection.getInt("team_color.g"), teamSection.getInt("team_color.b"));
        Location spawn = new Location(mapWorld, teamSection.getDouble("spawn.x"), teamSection.getDouble("spawn.y"), teamSection.getDouble("spawn.z"),
                (float)teamSection.getDouble("spawn.yaw"), (float)teamSection.getDouble("spawn.pitch"));
        List<Monument> monuments = new ArrayList<>();
        List<Map<?, ?>> monsData = teamSection.getMapList("monuments");
        for (Map<?, ?> monInfo : monsData) {
            String monName = (String)monInfo.get("name");
            int monX = (int)monInfo.get("x");
            int monY = (int)monInfo.get("y");
            int monZ = (int)monInfo.get("z");
            monuments.add(new Monument(monName, new Location(mapWorld, monX, monY, monZ), team));
        }
        Loadout teamLoadout = getTeamLoadout(c, teamColor);
        return new TeamMapData(team, teamName, teamColor, spawn, teamLoadout, monuments);
    }

    //TODO tipped arrow
    private Loadout getTeamLoadout(YamlConfiguration c, Color teamColor) {
        Loadout loadout = new Loadout();
        List<Map<?, ?>> loadoutData = c.getMapList("loadout");
        for (Map<?, ?> itemInfo : loadoutData) {
            Material itemMat = Material.valueOf((String)itemInfo.get("material"));
            int amount = (int)itemInfo.get("amount");
            int slot = (int)itemInfo.get("slot");
            Integer durability = (Integer)itemInfo.get("durability");
            String itemName = (String)itemInfo.get("item_name");
            List<String> lore = (List<String>)itemInfo.get("lore");
            ItemStack loadoutItem = new ItemStack(itemMat, amount);
            ItemMeta loadoutMeta = loadoutItem.getItemMeta();
            if (itemName != null)
                loadoutMeta.setDisplayName(ChatColorUtil.colorize(itemName));
            if (lore != null)
                loadoutMeta.setLore(ChatColorUtil.colorize(lore));
            if (durability != null)
                ((Damageable)loadoutMeta).setDamage(durability);
            List<Map<String, Integer>> enchantInfo = (List<Map<String, Integer>>)itemInfo.get("enchants");
            if (enchantInfo != null) {
                for (Map<String, Integer> enchant : enchantInfo) {
                    Map.Entry<String, Integer> enchantEntry = enchant.entrySet().iterator().next();
                    loadoutMeta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft(enchantEntry.getKey())), enchantEntry.getValue(), true);
                }
            }
            if (itemMat == Material.POTION || itemMat == Material.LINGERING_POTION || itemMat == Material.SPLASH_POTION) {
                List<Map<?, ?>> effectInfo = (List<Map<?, ?>>)itemInfo.get("effect");
                if (effectInfo != null) {
                    for (Map<?, ?> effect : effectInfo) {
                        String potionType = (String) effect.get("type");
                        int duration = (int) effect.get("duration");
                        int amplifier = (int) effect.get("amplifier");
                        ((PotionMeta) loadoutMeta).addCustomEffect(new PotionEffect(PotionEffectType.getByName(potionType), duration, amplifier), false);
                    }
                }
            }
            loadoutMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE);
            loadoutItem.setItemMeta(loadoutMeta);
            loadout.addItem(loadoutItem, slot);
        }
        loadout.colorLeatherGear(teamColor);
        return loadout;
    }

    private void loadSampleConfig() {
        String fileName = "sample-map.yml";
        if (!mapDir.exists()) {
            mapDir.mkdir();
        }
        File file = new File(mapDir, fileName);
        if (!file.exists()) {
            DTM.dtm.saveResource(mapDir.getPath() + "/" + fileName, false);
        }
    }
}
