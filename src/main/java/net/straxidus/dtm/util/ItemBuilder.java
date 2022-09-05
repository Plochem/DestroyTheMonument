package net.straxidus.dtm.util;

import net.straxidus.dtm.DTM;
import net.straxidus.dtm.util.interactableitem.InteractableItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ItemBuilder {
    private ItemStack item;
    private ItemMeta meta;
    private Material material = Material.STONE;
    private int amount = 1;
    private short damage = 0;
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private String displayname;
    private List<String> lore = new ArrayList<>();
    private Set<ItemFlag> flags = new HashSet<>();

    private UUID playerSkin;

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(ConfigurationSection sec) {
        this(Material.getMaterial(sec.getString("material")));
        this.displayname(ChatColorUtil.colorize(sec.getString("item-name"))).lore(ChatColorUtil.colorize(sec.getStringList("lore")));
    }

    public ItemBuilder(Material material, int amount) {
        if(material == null) material = Material.STONE;
        this.amount = amount;
        this.item = new ItemStack(material, amount);
        this.material = material;
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
        this.material = item.getType();
        this.amount = item.getAmount();
        this.enchantments = item.getEnchantments();
        if(item.hasItemMeta()) {
            this.meta = item.getItemMeta();
            this.displayname = item.getItemMeta().getDisplayName();
            this.lore = item.getItemMeta().getLore();
            this.flags = item.getItemMeta().getItemFlags();
        }
    }

    public ItemBuilder durability(short damage) {
        this.damage = damage;
        return this;
    }

    public ItemBuilder material(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder meta(ItemMeta meta) {
        this.meta = meta;
        return this;
    }

    public ItemBuilder enchant(Enchantment enchant, int level) {
        enchantments.put(enchant, level);
        return this;
    }

    public ItemBuilder enchant(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public ItemBuilder displayname(String displayname) {
        this.displayname = displayname;
        return this;
    }

    public ItemBuilder lore(String... lines) {
        for (String line : lines) {
            lore.add(line);
        }
        return this;
    }

    public ItemBuilder lore(List<String> lines) {
        for (String line : lines) {
            lore.add(line);
        }
        return this;
    }

    public ItemBuilder lore(String line, int index) {
        lore.set(index, line);
        return this;
    }

    public String getDisplayname() {
        return displayname;
    }

    public String getDisplaynameUnformatted() {
        return ChatColor.stripColor(displayname);
    }

    public int getAmount() {
        return amount;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public short getDurability() {
        return damage;
    }

    public List<String> getLore() {
        return lore;
    }

    public Material getMaterial() {
        return material;
    }

    public ItemMeta getMeta() {
        return meta;
    }

    public ItemBuilder hideAttributes() {
        flags.add(ItemFlag.HIDE_ATTRIBUTES);
        return this;
    }

    /**
     * Only use if material is skull_item & damage is 3
     */
    public ItemBuilder setPlayerSkin(UUID id) {
        this.playerSkin = id;
        return this;
    }

    public ItemStack build() {
        item.setType(material);
        item.setAmount(amount);
        meta = item.hasItemMeta() ? item.getItemMeta() : Bukkit.getItemFactory().getItemMeta(item.getType());
        if(displayname != null) {
            meta.setDisplayName(displayname);
        }
        if(lore.size() > 0) {
            meta.setLore(lore);
        }

        if(flags.size() > 0) {
            for(ItemFlag flag : flags)
                meta.addItemFlags(flag);
        }

        if(meta instanceof SkullMeta)
            ((SkullMeta)meta).setOwningPlayer(Bukkit.getOfflinePlayer(playerSkin));

        item.setItemMeta(meta);

        if(enchantments.size() > 0) {
            item.addUnsafeEnchantments(enchantments);
        }
        return item;
    }

    public ItemBuilder glow() {
        enchant(ItemGlow.getGlow(), 1);
        return this;
    }

    public ItemBuilder setNBTString(String key, String value) {
        NBTExplorer.setNBTString(item, key, value);
        return this;
    }

    public ItemBuilder setNBTInteger(String key, int value) {
        NBTExplorer.setNBTInteger(item, key, value);
        return this;
    }

    public ItemBuilder setNBTBoolean(String key, boolean value) {
        return setNBTInteger(key, value == true ? 1 : 0);
    }

    public ItemBuilder setNBTStringArray(String key, String[] value) {
        setNBTInteger(key + "Length", value.length);
        for(int i = 0; i < value.length; i++) {
            setNBTString(key + i, value[i]);
        }
        return this;
    }

}
