package com.github.devcyntrix.expdrop;

import org.bstats.bukkit.Metrics;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ExpDropPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        File dataFolder = getDataFolder();
        if(!dataFolder.isDirectory() && !dataFolder.mkdirs()) {
            throw new RuntimeException("Failed to create data folder");
        }

        File configFile = new File(dataFolder, "config.yml");
        if(!configFile.isFile()) {
            saveDefaultConfig();
            reloadConfig();
        }

        getServer().getPluginManager().registerEvents(this, this);

        new Metrics(this, 19941);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

    }
}
