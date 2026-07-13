package org.feuchte.enderDragen;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.feuchte.enderDragen.commands.DragonCommand;

import lombok.Getter;

public final class EnderDragen extends JavaPlugin implements Listener {

    private final Random random = new Random();
    private File statsFile;
    
    @Getter
    private FileConfiguration statsConfig;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadStatsFile();
        getServer().getPluginManager().registerEvents(this, this);
        
        DragonCommand dragonCommand = new DragonCommand(this);
        getCommand("dragon").setExecutor(dragonCommand);
        getCommand("dragon").setTabCompleter(dragonCommand);
        
        getLogger().info("EnderDragen Plugin aktiviert!");
    }

    @Override
    public void onDisable() {
        saveStatsFile();
        getLogger().info("EnderDragen Plugin deaktiviert!");
    }

    private void loadStatsFile() {
        statsFile = new File(getDataFolder(), "stats.yml");
        if (!statsFile.exists()) {
            saveResource("stats.yml", false);
        }
        statsConfig = YamlConfiguration.loadConfiguration(statsFile);
    }

    public void saveStatsFile() {
        try {
            statsConfig.save(statsFile);
        } catch (IOException e) {
            getLogger().severe("Konnte stats.yml nicht speichern: " + e.getMessage());
        }
    }

    public void reloadPluginConfig() {
        reloadConfig();
        loadStatsFile();
    }

    public void resetStats() {
        statsConfig.set("statistics.dragons-killed", 0);
        statsConfig.set("statistics.dragon-eggs-dropped", 0);
        statsConfig.set("statistics.dragon-heads-dropped", 0);
        statsConfig.set("statistics.elytras-dropped", 0);
        saveStatsFile();
    }

    private void incrementStat(String path) {
        int current = statsConfig.getInt(path, 0);
        statsConfig.set(path, current + 1);
        saveStatsFile();
    }

    @EventHandler
    public void onEnderDragonDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof EnderDragon dragon)) {
            return;
        }
        
        Location deathLocation = dragon.getLocation();
        World world = dragon.getWorld();
        
        incrementStat("statistics.dragons-killed");
        
        double dragonEggChance = getConfig().getDouble("chances.dragon-egg", 0.05);
        double headChance = getConfig().getDouble("chances.dragon-head", 0.15);
        double elytraChance = getConfig().getDouble("chances.elytra", 0.2);
        
        if (random.nextDouble() < dragonEggChance) {
            world.dropItemNaturally(deathLocation, new ItemStack(Material.DRAGON_EGG));
            incrementStat("statistics.dragon-eggs-dropped");
            getLogger().info("Drachen-Ei gedroppt!");
        }
        
        if (random.nextDouble() < headChance) {
            world.dropItemNaturally(deathLocation, new ItemStack(Material.DRAGON_HEAD));
            incrementStat("statistics.dragon-heads-dropped");
            getLogger().info("Drachen-Kopf gedroppt!");
        }
        
        if (random.nextDouble() < elytraChance) {
            world.dropItemNaturally(deathLocation, new ItemStack(Material.ELYTRA));
            incrementStat("statistics.elytras-dropped");
            getLogger().info("Elytra gedroppt!");
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof org.feuchte.enderDragen.gui.StatsGUI) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof org.feuchte.enderDragen.gui.StatsGUI) {
            event.setCancelled(true);
        }
    }
}
