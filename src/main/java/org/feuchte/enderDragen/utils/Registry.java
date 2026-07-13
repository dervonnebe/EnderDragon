package org.feuchte.enderDragen.utils;

import org.feuchte.enderDragen.EnderDragen;
import org.feuchte.enderDragen.commands.DragonCommand;
import org.feuchte.enderDragen.listeners.DragonDeathListener;
import org.feuchte.enderDragen.listeners.InventoryListener;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Registry {

    private final EnderDragen plugin;

    public void registerAll() {
        registerListeners();
        registerCommands();
    }

    private void registerListeners() {
        plugin.getServer().getPluginManager().registerEvents(new DragonDeathListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new InventoryListener(), plugin);
    }

    private void registerCommands() {
        DragonCommand dragonCommand = new DragonCommand(plugin);
        plugin.getCommand("dragon").setExecutor(dragonCommand);
        plugin.getCommand("dragon").setTabCompleter(dragonCommand);
    }
}
