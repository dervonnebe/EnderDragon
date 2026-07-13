package org.feuchte.enderDragen;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.feuchte.enderDragen.utils.Registry;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import lombok.Getter;
import org.feuchte.enderDragen.utils.LanguageManager;

public final class EnderDragen extends JavaPlugin {

    private File statsFile;
    
    @Getter
    private FileConfiguration statsConfig;
    
    @Getter
    private LanguageManager languageManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        languageManager = new LanguageManager(this);
        loadStatsFile();
        
        new Registry(this).registerAll();
        
        getLogger().info("EnderDragen Plugin aktiviert!");
    }

    public void reloadPluginConfig() {
        reloadConfig();
        languageManager.loadLanguage();
        loadStatsFile();
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

    public void resetStats() {
        statsConfig.set("statistics.dragons-killed", 0);
        statsConfig.set("statistics.dragon-eggs-dropped", 0);
        statsConfig.set("statistics.dragon-heads-dropped", 0);
        statsConfig.set("statistics.elytras-dropped", 0);
        saveStatsFile();
    }

    public void incrementStat(String path) {
        int current = statsConfig.getInt(path, 0);
        statsConfig.set(path, current + 1);
        saveStatsFile();
    }
}
