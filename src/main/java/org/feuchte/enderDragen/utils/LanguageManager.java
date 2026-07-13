package org.feuchte.enderDragen.utils;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.feuchte.enderDragen.EnderDragen;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class LanguageManager {
    private final EnderDragen plugin;
    private FileConfiguration langConfig;

    public LanguageManager(EnderDragen plugin) {
        this.plugin = plugin;
        loadLanguage();
    }

    public void loadLanguage() {
        String lang = plugin.getConfig().getString("language", "de");
        File langFile = new File(plugin.getDataFolder(), "messages_" + lang + ".yml");
        
        if (!langFile.exists()) {
            try {
                plugin.saveResource("messages_" + lang + ".yml", false);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Keine Standard-Sprachdatei fuer " + lang + " gefunden. Erstelle leere Datei.");
            }
        }
        
        langConfig = YamlConfiguration.loadConfiguration(langFile);
        
        InputStream defaultStream = plugin.getResource("messages_" + lang + ".yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream, StandardCharsets.UTF_8));
            langConfig.setDefaults(defaultConfig);
        }
    }

    public Component getMessage(String key, Object... args) {
        String msg = langConfig.getString(key, "<red>Missing message: " + key);
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        return MiniMessage.miniMessage().deserialize(msg);
    }
    
    public String getRawMessage(String key, Object... args) {
        String msg = langConfig.getString(key, "Missing message: " + key);
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        return msg;
    }
}
