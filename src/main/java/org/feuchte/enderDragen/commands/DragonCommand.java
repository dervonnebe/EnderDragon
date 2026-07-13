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
                    sender.sendMessage("§cDieser Befehl kann nur von Spielern ausgeführt werden!");
                    return true;
                }
                new StatsGUI(plugin).open(player);
                return true;
                
            case "setchance":
                if (!sender.hasPermission("enderdragon.admin")) {
                    sender.sendMessage("§cKeine Berechtigung!");
                    return true;
                }
                if (args.length != 3) {
                    sender.sendMessage("§cVerwendung: /dragon setchance <egg|head|elytra> <chance>");
                    return true;
                }
                return setChance(sender, args[1], args[2]);
                
            case "resetstats":
                if (!sender.hasPermission("enderdragon.admin")) {
                    sender.sendMessage("§cKeine Berechtigung!");
                    return true;
                }
                plugin.resetStats();
                sender.sendMessage("§aStatistiken wurden zurückgesetzt!");
                return true;
                
            case "reload":
                if (!sender.hasPermission("enderdragon.admin")) {
                    sender.sendMessage("§cKeine Berechtigung!");
                    return true;
                }
                plugin.reloadPluginConfig();
                sender.sendMessage("§aKonfiguration neu geladen!");
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
                sender.sendMessage("§cDie Chance muss zwischen 0.0 und 1.0 liegen!");
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
                    sender.sendMessage("§cUngültiger Typ! Verwende: egg, head oder elytra");
                    return true;
            }
            
            plugin.getConfig().set(configPath, value);
            plugin.saveConfig();
            plugin.reloadPluginConfig();
            
            sender.sendMessage(String.format("§a%s Chance wurde auf %.1f%% gesetzt!", displayName, value * 100.0));
            return true;
        } catch (NumberFormatException e) {
            sender.sendMessage("§cUngültige Zahl!");
            return true;
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6§l=== EnderDragen Befehle ===");
        sender.sendMessage("§e/dragon stats §7- Zeigt Statistiken GUI");
        if (sender.hasPermission("enderdragon.admin")) {
            sender.sendMessage("§e/dragon setchance <egg|head|elytra> <chance> §7- Setzt Chancen");
            sender.sendMessage("§e/dragon resetstats §7- Setzt Statistiken zurück");
            sender.sendMessage("§e/dragon reload §7- Lädt Config neu");
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
