package com.github.devcyntrix.expdrop;

import com.github.devcyntrix.expdrop.util.ExpUtil;
import org.bstats.bukkit.Metrics;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExpDropPlugin extends JavaPlugin implements Listener {

    private ExpDropConfig config;

    @Override
    public void onEnable() {
        File dataFolder = getDataFolder();
        if (!dataFolder.isDirectory() && !dataFolder.mkdirs()) {
            throw new RuntimeException("Failed to create data folder");
        }

        File configFile = new File(dataFolder, "config.yml");
        if (!configFile.isFile()) {
            saveDefaultConfig();
            reloadConfig();
        }
        this.config = ExpDropConfig.parse(getConfig());

        getServer().getPluginManager().registerEvents(this, this);

        new Metrics(this, 19941);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1)
            return false;

        if ("reload".equalsIgnoreCase(args[0])) {
            try {
                reloadConfig();
                this.config = ExpDropConfig.parse(getConfig());
                sender.sendMessage("§aExᴘᴅʀᴏᴘ §8» §7The config reload was §asuccessful§7.");
            } catch (Exception e) {
                sender.sendMessage("§aExᴘᴅʀᴏᴘ §8» §cSomething went wrong during the config reload.");
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return StringUtil.copyPartialMatches(args[0], Collections.singleton("reload"), new ArrayList<>());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        double exp = event.getDroppedExp();
        if (config.dropAllExp()) {
            exp = ExpUtil.getPlayerExp(event.getEntity());
        }

        exp *= config.keepExpRate();

        if (config.convertToBottles()) {
            int numberOfBottles = (int) (Math.round(exp) / 7.0); // Seven is the average of experience points which drops a Bottle o' Enchanting (https://minecraft.fandom.com/wiki/Bottle_o%27_Enchanting#Usage)
            do {
                int size = Math.min(numberOfBottles, 64);
                event.getDrops().add(new ItemStack(Material.EXPERIENCE_BOTTLE, size));
                numberOfBottles -= size;
            } while (numberOfBottles > 0);
            event.setDroppedExp(0);
        } else {
            event.setDroppedExp((int) Math.round(exp));
        }
    }
}
