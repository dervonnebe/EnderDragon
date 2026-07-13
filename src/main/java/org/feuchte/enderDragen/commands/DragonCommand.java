package org.feuchte.enderDragen.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.feuchte.enderDragen.EnderDragen;
import org.feuchte.enderDragen.gui.StatsGUI;

import net.kyori.adventure.text.minimessage.MiniMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DragonCommand implements CommandExecutor, TabCompleter {
    
    private final EnderDragen plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "stats":
                if (!(sender instanceof Player player)) {
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Dieser Befehl kann nur von Spielern ausgeführt werden!"));
                    return true;
                }
                new StatsGUI(plugin).open(player);
                return true;
                
            case "setchance":
                if (!sender.hasPermission("enderdragon.admin")) {
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Keine Berechtigung!"));
                    return true;
                }
                if (args.length != 3) {
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Verwendung: /dragon setchance <egg|head|elytra> <chance>"));
                    return true;
                }
                return setChance(sender, args[1], args[2]);
                
            case "resetstats":
                if (!sender.hasPermission("enderdragon.admin")) {
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Keine Berechtigung!"));
                    return true;
                }
                plugin.resetStats();
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>Statistiken wurden zurückgesetzt!"));
                return true;
                
            case "reload":
                if (!sender.hasPermission("enderdragon.admin")) {
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Keine Berechtigung!"));
                    return true;
                }
                plugin.reloadPluginConfig();
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>Konfiguration neu geladen!"));
                return true;
                
            default:
                sendHelp(sender);
                return true;
        }
    }

    private boolean setChance(CommandSender sender, String type, String valueStr) {
        try {
            double value = Double.parseDouble(valueStr);
            if (value < 0.0 || value > 1.0) {
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Die Chance muss zwischen 0.0 und 1.0 liegen!"));
                return true;
            }
            
            String configPath;
            String displayName;
            
            switch (type.toLowerCase()) {
                case "egg":
                case "dragonegg":
                    configPath = "chances.dragon-egg";
                    displayName = "Drachen-Ei";
                    break;
                case "head":
                    configPath = "chances.dragon-head";
                    displayName = "Drachen-Kopf";
                    break;
                case "elytra":
                    configPath = "chances.elytra";
                    displayName = "Elytra";
                    break;
                default:
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Ungültiger Typ! Verwende: egg, head oder elytra"));
                    return true;
            }
            
            plugin.getConfig().set(configPath, value);
            plugin.saveConfig();
            plugin.reloadPluginConfig();
            
            sender.sendMessage(MiniMessage.miniMessage().deserialize(String.format("<green>%s Chance wurde auf %.1f%% gesetzt!", displayName, value * 100.0)));
            return true;
        } catch (NumberFormatException e) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Ungültige Zahl!"));
            return true;
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(MiniMessage.miniMessage().deserialize("<gold><bold>=== EnderDragen Befehle ==="));
        sender.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>/dragon stats <gray>- Zeigt Statistiken GUI"));
        if (sender.hasPermission("enderdragon.admin")) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>/dragon setchance <egg|head|elytra> <chance> <gray>- Setzt Chancen"));
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>/dragon resetstats <gray>- Setzt Statistiken zurück"));
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>/dragon reload <gray>- Lädt Config neu"));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            completions.add("stats");
            if (sender.hasPermission("enderdragon.admin")) {
                completions.addAll(Arrays.asList("setchance", "resetstats", "reload"));
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("setchance")) {
            completions.addAll(Arrays.asList("egg", "head", "elytra"));
        } else if (args.length == 3 && args[0].equalsIgnoreCase("setchance")) {
            completions.addAll(Arrays.asList("0.05", "0.10", "0.15", "0.20", "0.50"));
        }
        
        return completions;
    }
}
