package org.feuchte.enderDragen.gui;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.feuchte.enderDragen.EnderDragen;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StatsGUI implements InventoryHolder {

    private final EnderDragen plugin;
    private Inventory inv;

    @Override
    public Inventory getInventory() {
        return inv;
    }

    public void open(Player player) {
        String titleString = plugin.getConfig().getString("gui.title", "§6§lEnder Drachen Statistiken");
        
        Component title;
        if (titleString.contains("<") && titleString.contains(">")) {
            title = MiniMessage.miniMessage().deserialize(titleString);
        } else {
            title = LegacyComponentSerializer.legacySection().deserialize(titleString);
        }
        
        inv = Bukkit.createInventory(this, 27, title);
        
        int dragonsKilled = plugin.getStatsConfig().getInt("statistics.dragons-killed", 0);
        int dragonEggsDropped = plugin.getStatsConfig().getInt("statistics.dragon-eggs-dropped", 0);
        int headsDropped = plugin.getStatsConfig().getInt("statistics.dragon-heads-dropped", 0);
        int elytrasDropped = plugin.getStatsConfig().getInt("statistics.elytras-dropped", 0);
        
        double dragonEggChance = plugin.getConfig().getDouble("chances.dragon-egg", 0.05) * 100.0;
        double headChance = plugin.getConfig().getDouble("chances.dragon-head", 0.15) * 100.0;
        double elytraChance = plugin.getConfig().getDouble("chances.elytra", 0.2) * 100.0;
        
        inv.setItem(10, createItem(Material.DRAGON_EGG, "<light_purple><bold>Drachen-Eier", Arrays.asList("<gray>Gedroppt: <yellow>" + dragonEggsDropped, "", "<gray>Drop Chance: <green>" + String.format("%.1f%%", dragonEggChance))));
        inv.setItem(12, createItem(Material.DRAGON_HEAD, "<gold><bold>Drachen getötet", Arrays.asList("<gray>Gesamt: <yellow>" + dragonsKilled)));
        inv.setItem(14, createItem(Material.DRAGON_HEAD, "<dark_purple><bold>Drachen-Köpfe", Arrays.asList("<gray>Gedroppt: <yellow>" + headsDropped, "", "<gray>Drop Chance: <green>" + String.format("%.1f%%", headChance))));
        inv.setItem(16, createItem(Material.ELYTRA, "<aqua><bold>Elytras", Arrays.asList("<gray>Gedroppt: <yellow>" + elytrasDropped, "", "<gray>Drop Chance: <green>" + String.format("%.1f%%", elytraChance))));
        
        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        if (glassMeta != null) {
            glassMeta.displayName(Component.empty());
            glass.setItemMeta(glassMeta);
        }
        
        for (int i = 0; i < 27; ++i) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, glass);
            }
        }
        
        player.openInventory(inv);
    }

    private ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(MiniMessage.miniMessage().deserialize(name).decoration(net.kyori.adventure.text.format.TextDecoration.ITALIC, false));
            List<Component> loreComponents = lore.stream()
                .map(line -> (Component) MiniMessage.miniMessage().deserialize(line).decoration(net.kyori.adventure.text.format.TextDecoration.ITALIC, false))
                .toList();
            meta.lore(loreComponents);
            item.setItemMeta(meta);
        }
        return item;
    }
}
