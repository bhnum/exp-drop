package com.github.devcyntrix.expdrop;

import com.github.devcyntrix.expdrop.command.ExpDropExecutor;
import com.github.devcyntrix.expdrop.controller.UpdateController;
import com.github.devcyntrix.expdrop.listener.ExpDropListener;
import com.github.devcyntrix.expdrop.listener.MendingProtectionListener;
import com.github.devcyntrix.expdrop.view.update.AdminJoinNotificationView;
import com.github.devcyntrix.expdrop.view.update.AdminNotificationView;
import com.github.devcyntrix.expdrop.view.update.ConsoleNotificationView;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class ExpDropPlugin extends JavaPlugin implements Listener {

    public static final String EXP_DROP_ORB = "expdrop-orb";
    @Getter
    private NamespacedKey namespacedKey;

    @Getter
    private ExpDropConfig expDropConfig;
    @Nullable
    private UpdateController updateController;

    @Override
    public void onDisable() {
        if (updateController != null) {
            updateController.close();
        }
    }

    @Override
    public void onEnable() {
        this.namespacedKey = new NamespacedKey(this, EXP_DROP_ORB);

        File dataFolder = getDataFolder();
        if (!dataFolder.isDirectory() && !dataFolder.mkdirs()) {
            throw new RuntimeException("Failed to create data folder");
        }

        File configFile = new File(dataFolder, "config.yml");
        if (!configFile.isFile()) {
            saveDefaultConfig();
        }
        reloadConfig();

        PluginCommand expDropCommand = getCommand("expdrop");
        if (expDropCommand != null) {
            expDropCommand.setUsage(getPrefix() + "§7/<command> reload");
            ExpDropExecutor executor = new ExpDropExecutor(this);
            expDropCommand.setExecutor(executor);
            expDropCommand.setTabCompleter(executor);
        }
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ExpDropListener(this), this);
        pluginManager.registerEvents(new MendingProtectionListener(this), this);

        if (expDropConfig.checkForUpdates()) {
            this.updateController = new UpdateController(this, 112907);
            this.updateController.subscribe(new AdminNotificationView(this));
            this.updateController.subscribe(new ConsoleNotificationView(this, getLogger()));
            getServer().getPluginManager().registerEvents(new AdminJoinNotificationView(this, updateController), this);
        }
        new Metrics(this, 19941);
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        this.expDropConfig = ExpDropConfig.parse(getConfig());
    }


    public String getPrefix() {
        return "§aExᴘᴅʀᴏᴘ §8» ";
    }

}
