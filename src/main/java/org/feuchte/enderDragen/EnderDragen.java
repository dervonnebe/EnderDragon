/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.EnderDragon
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package org.feuchte.enderDragen;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.feuchte.enderDragen.commands.DragonCommand;

public final class EnderDragen
extends JavaPlugin
implements Listener {
    private final Random random = new Random();
    private File statsFile;
    private FileConfiguration statsConfig;

    public void onEnable() {
        this.saveDefaultConfig();
        this.loadStatsFile();
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        DragonCommand dragonCommand = new DragonCommand(this);
        this.getCommand("dragon").setExecutor((CommandExecutor)dragonCommand);
        this.getCommand("dragon").setTabCompleter((TabCompleter)dragonCommand);
        this.getLogger().info("EnderDragen Plugin aktiviert!");
    }

    public void onDisable() {
        this.saveStatsFile();
        this.getLogger().info("EnderDragen Plugin deaktiviert!");
    }

    private void loadStatsFile() {
        this.statsFile = new File(this.getDataFolder(), "stats.yml");
        if (!this.statsFile.exists()) {
            this.saveResource("stats.yml", false);
        }
        this.statsConfig = YamlConfiguration.loadConfiguration((File)this.statsFile);
    }

    public void saveStatsFile() {
        try {
            this.statsConfig.save(this.statsFile);
        }
        catch (IOException e) {
            this.getLogger().severe("Konnte stats.yml nicht speichern: " + e.getMessage());
        }
    }

    public FileConfiguration getStatsConfig() {
        return this.statsConfig;
    }

    public void reloadPluginConfig() {
        this.reloadConfig();
        this.loadStatsFile();
    }

    public void resetStats() {
        this.statsConfig.set("statistics.dragons-killed", (Object)0);
        this.statsConfig.set("statistics.dragon-eggs-dropped", (Object)0);
        this.statsConfig.set("statistics.dragon-heads-dropped", (Object)0);
        this.statsConfig.set("statistics.elytras-dropped", (Object)0);
        this.saveStatsFile();
    }

    private void incrementStat(String path) {
        int current = this.statsConfig.getInt(path, 0);
        this.statsConfig.set(path, (Object)(current + 1));
        this.saveStatsFile();
    }

    @EventHandler
    public void onEnderDragonDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof EnderDragon)) {
            return;
        }
        EnderDragon dragon = (EnderDragon)event.getEntity();
        Location deathLocation = dragon.getLocation();
        World world = dragon.getWorld();
        this.incrementStat("statistics.dragons-killed");
        double dragonEggChance = this.getConfig().getDouble("chances.dragon-egg", 0.05);
        double headChance = this.getConfig().getDouble("chances.dragon-head", 0.15);
        double elytraChance = this.getConfig().getDouble("chances.elytra", 0.2);
        if (this.random.nextDouble() < dragonEggChance) {
            ItemStack dragonEgg = new ItemStack(Material.DRAGON_EGG, 1);
            world.dropItemNaturally(deathLocation, dragonEgg);
            this.incrementStat("statistics.dragon-eggs-dropped");
            this.getLogger().info("Drachen-Ei gedroppt!");
        }
        if (this.random.nextDouble() < headChance) {
            ItemStack dragonHead = new ItemStack(Material.DRAGON_HEAD, 1);
            world.dropItemNaturally(deathLocation, dragonHead);
            this.incrementStat("statistics.dragon-heads-dropped");
            this.getLogger().info("Drachen-Kopf gedroppt!");
        }
        if (this.random.nextDouble() < elytraChance) {
            ItemStack elytra = new ItemStack(Material.ELYTRA, 1);
            world.dropItemNaturally(deathLocation, elytra);
            this.incrementStat("statistics.elytras-dropped");
            this.getLogger().info("Elytra gedroppt!");
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = this.getConfig().getString("gui.title", "\u00a76\u00a7lEnder Drachen Statistiken");
        if (event.getView().getTitle().equals(title)) {
            event.setCancelled(true);
        }
    }
}

