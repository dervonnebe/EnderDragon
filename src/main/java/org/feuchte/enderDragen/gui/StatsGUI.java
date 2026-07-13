/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package org.feuchte.enderDragen.gui;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.feuchte.enderDragen.EnderDragen;

public class StatsGUI {
    private final EnderDragen plugin;

    public StatsGUI(EnderDragen plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        String title = this.plugin.getConfig().getString("gui.title", "\u00a76\u00a7lEnder Drachen Statistiken");
        Inventory inv = Bukkit.createInventory(null, (int)27, (String)title);
        int dragonsKilled = this.plugin.getStatsConfig().getInt("statistics.dragons-killed", 0);
        int dragonEggsDropped = this.plugin.getStatsConfig().getInt("statistics.dragon-eggs-dropped", 0);
        int headsDropped = this.plugin.getStatsConfig().getInt("statistics.dragon-heads-dropped", 0);
        int elytrasDropped = this.plugin.getStatsConfig().getInt("statistics.elytras-dropped", 0);
        double dragonEggChance = this.plugin.getConfig().getDouble("chances.dragon-egg", 0.05) * 100.0;
        double headChance = this.plugin.getConfig().getDouble("chances.dragon-head", 0.15) * 100.0;
        double elytraChance = this.plugin.getConfig().getDouble("chances.elytra", 0.2) * 100.0;
        inv.setItem(10, this.createItem(Material.DRAGON_EGG, "\u00a7d\u00a7lDrachen-Eier", Arrays.asList("\u00a77Gedroppt: \u00a7e" + dragonEggsDropped, "", "\u00a77Drop Chance: \u00a7a" + String.format("%.1f%%", dragonEggChance))));
        inv.setItem(12, this.createItem(Material.DRAGON_HEAD, "\u00a76\u00a7lDrachen get\u00f6tet", Arrays.asList("\u00a77Gesamt: \u00a7e" + dragonsKilled)));
        inv.setItem(14, this.createItem(Material.DRAGON_HEAD, "\u00a75\u00a7lDrachen-K\u00f6pfe", Arrays.asList("\u00a77Gedroppt: \u00a7e" + headsDropped, "", "\u00a77Drop Chance: \u00a7a" + String.format("%.1f%%", headChance))));
        inv.setItem(16, this.createItem(Material.ELYTRA, "\u00a7b\u00a7lElytras", Arrays.asList("\u00a77Gedroppt: \u00a7e" + elytrasDropped, "", "\u00a77Drop Chance: \u00a7a" + String.format("%.1f%%", elytraChance))));
        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        if (glassMeta != null) {
            glassMeta.setDisplayName(" ");
            glass.setItemMeta(glassMeta);
        }
        for (int i = 0; i < 27; ++i) {
            if (inv.getItem(i) != null) continue;
            inv.setItem(i, glass);
        }
        player.openInventory(inv);
    }

    private ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }
}

