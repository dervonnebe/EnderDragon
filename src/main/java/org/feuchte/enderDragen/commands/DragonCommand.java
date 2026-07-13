/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.entity.Player
 */
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

public class DragonCommand
implements CommandExecutor,
TabCompleter {
    private final EnderDragen plugin;

    public DragonCommand(EnderDragen plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            this.sendHelp(sender);
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "stats": {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("\u00a7cDieser Befehl kann nur von Spielern ausgef\u00fchrt werden!");
                    return true;
                }
                Player player = (Player)sender;
                new StatsGUI(this.plugin).open(player);
                return true;
            }
            case "setchance": {
                if (!sender.hasPermission("enderdragon.admin")) {
                    sender.sendMessage("\u00a7cKeine Berechtigung!");
                    return true;
                }
                if (args.length != 3) {
                    sender.sendMessage("\u00a7cVerwendung: /dragon setchance <egg|head|elytra> <chance>");
                    return true;
                }
                return this.setChance(sender, args[1], args[2]);
            }
            case "resetstats": {
                if (!sender.hasPermission("enderdragon.admin")) {
                    sender.sendMessage("\u00a7cKeine Berechtigung!");
                    return true;
                }
                this.plugin.resetStats();
                sender.sendMessage("\u00a7aStatistiken wurden zur\u00fcckgesetzt!");
                return true;
            }
            case "reload": {
                if (!sender.hasPermission("enderdragon.admin")) {
                    sender.sendMessage("\u00a7cKeine Berechtigung!");
                    return true;
                }
                this.plugin.reloadPluginConfig();
                sender.sendMessage("\u00a7aKonfiguration neu geladen!");
                return true;
            }
        }
        this.sendHelp(sender);
        return true;
    }

    private boolean setChance(CommandSender sender, String type, String valueStr) {
        try {
            String displayName;
            String configPath;
            double value = Double.parseDouble(valueStr);
            if (value < 0.0 || value > 1.0) {
                sender.sendMessage("\u00a7cDie Chance muss zwischen 0.0 und 1.0 liegen!");
                return true;
            }
            switch (type.toLowerCase()) {
                case "egg": 
                case "dragonegg": {
                    configPath = "chances.dragon-egg";
                    displayName = "Drachen-Ei";
                    break;
                }
                case "head": {
                    configPath = "chances.dragon-head";
                    displayName = "Drachen-Kopf";
                    break;
                }
                case "elytra": {
                    configPath = "chances.elytra";
                    displayName = "Elytra";
                    break;
                }
                default: {
                    sender.sendMessage("\u00a7cUng\u00fcltiger Typ! Verwende: egg, head oder elytra");
                    return true;
                }
            }
            this.plugin.getConfig().set(configPath, (Object)value);
            this.plugin.saveConfig();
            this.plugin.reloadPluginConfig();
            sender.sendMessage(String.format("\u00a7a%s Chance wurde auf %.1f%% gesetzt!", displayName, value * 100.0));
            return true;
        }
        catch (NumberFormatException e) {
            sender.sendMessage("\u00a7cUng\u00fcltige Zahl!");
            return true;
        }
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("\u00a76\u00a7l=== EnderDragen Befehle ===");
        sender.sendMessage("\u00a7e/dragon stats \u00a77- Zeigt Statistiken GUI");
        if (sender.hasPermission("enderdragon.admin")) {
            sender.sendMessage("\u00a7e/dragon setchance <egg|head|elytra> <chance> \u00a77- Setzt Chancen");
            sender.sendMessage("\u00a7e/dragon resetstats \u00a77- Setzt Statistiken zur\u00fcck");
            sender.sendMessage("\u00a7e/dragon reload \u00a77- L\u00e4dt Config neu");
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
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

