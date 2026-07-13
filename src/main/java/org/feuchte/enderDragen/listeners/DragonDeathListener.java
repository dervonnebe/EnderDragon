package org.feuchte.enderDragen.listeners;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.feuchte.enderDragen.EnderDragen;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DragonDeathListener implements Listener {

    private final EnderDragen plugin;
    private final Random random = new Random();

    @EventHandler
    public void onEnderDragonDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof EnderDragon dragon)) {
            return;
        }
        
        Location deathLocation = dragon.getLocation();
        World world = dragon.getWorld();
        
        plugin.incrementStat("statistics.dragons-killed");
        
        double dragonEggChance = plugin.getConfig().getDouble("chances.dragon-egg", 0.05);
        double headChance = plugin.getConfig().getDouble("chances.dragon-head", 0.15);
        double elytraChance = plugin.getConfig().getDouble("chances.elytra", 0.2);
        
        if (random.nextDouble() < dragonEggChance) {
            world.dropItemNaturally(deathLocation, new ItemStack(Material.DRAGON_EGG));
            plugin.incrementStat("statistics.dragon-eggs-dropped");
            plugin.getLogger().info(plugin.getLanguageManager().getRawMessage("log.egg-dropped"));
        }
        
        if (random.nextDouble() < headChance) {
            world.dropItemNaturally(deathLocation, new ItemStack(Material.DRAGON_HEAD));
            plugin.incrementStat("statistics.dragon-heads-dropped");
            plugin.getLogger().info(plugin.getLanguageManager().getRawMessage("log.head-dropped"));
        }
        
        if (random.nextDouble() < elytraChance) {
            world.dropItemNaturally(deathLocation, new ItemStack(Material.ELYTRA));
            plugin.incrementStat("statistics.elytras-dropped");
            plugin.getLogger().info(plugin.getLanguageManager().getRawMessage("log.elytra-dropped"));
        }
    }
}
