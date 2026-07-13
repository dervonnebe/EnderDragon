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
        Component title = plugin.getLanguageManager().getMessage("gui.title");
        inv = Bukkit.createInventory(this, 27, title);
        
        int dragonsKilled = plugin.getStatsConfig().getInt("statistics.dragons-killed", 0);
        int dragonEggsDropped = plugin.getStatsConfig().getInt("statistics.dragon-eggs-dropped", 0);
        int headsDropped = plugin.getStatsConfig().getInt("statistics.dragon-heads-dropped", 0);
        int elytrasDropped = plugin.getStatsConfig().getInt("statistics.elytras-dropped", 0);
        
        double dragonEggChance = plugin.getConfig().getDouble("chances.dragon-egg", 0.05) * 100.0;
        double headChance = plugin.getConfig().getDouble("chances.dragon-head", 0.15) * 100.0;
        double elytraChance = plugin.getConfig().getDouble("chances.elytra", 0.2) * 100.0;
        
        String formatDropped = plugin.getLanguageManager().getRawMessage("gui.lore.dropped", "%d");
        String formatChance = plugin.getLanguageManager().getRawMessage("gui.lore.chance", "%.1f%%");
        String formatTotal = plugin.getLanguageManager().getRawMessage("gui.lore.total", "%d");
        
        inv.setItem(10, createItem(Material.DRAGON_EGG, plugin.getLanguageManager().getRawMessage("gui.items.dragon-egg"), Arrays.asList(String.format(formatDropped, dragonEggsDropped), "", String.format(formatChance, dragonEggChance))));
        inv.setItem(12, createItem(Material.DRAGON_HEAD, plugin.getLanguageManager().getRawMessage("gui.items.dragons-killed"), Arrays.asList(String.format(formatTotal, dragonsKilled))));
        inv.setItem(14, createItem(Material.DRAGON_HEAD, plugin.getLanguageManager().getRawMessage("gui.items.dragon-head"), Arrays.asList(String.format(formatDropped, headsDropped), "", String.format(formatChance, headChance))));
        inv.setItem(16, createItem(Material.ELYTRA, plugin.getLanguageManager().getRawMessage("gui.items.elytra"), Arrays.asList(String.format(formatDropped, elytrasDropped), "", String.format(formatChance, elytraChance))));
        
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
