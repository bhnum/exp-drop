package com.github.devcyntrix.expdrop.view.update;

import com.github.devcyntrix.expdrop.ExpDropPlugin;
import org.bukkit.Bukkit;

import java.util.function.Consumer;

public class AdminNotificationView implements Consumer<String> {

    private final ExpDropPlugin plugin;

    public AdminNotificationView(ExpDropPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void accept(String version) {
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission("expdrop.update"))
                .forEach(player -> {
                    player.sendMessage(this.plugin.getPrefix() + "§7A new version §a" + version + " §7is out.");
                    player.sendMessage(this.plugin.getPrefix() + "§7Please update the plugin at §a" + plugin.getDescription().getWebsite());
                });
    }
}
