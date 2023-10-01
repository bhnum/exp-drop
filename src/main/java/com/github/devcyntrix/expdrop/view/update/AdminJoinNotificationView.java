package com.github.devcyntrix.expdrop.view.update;

import com.github.devcyntrix.expdrop.ExpDropPlugin;
import com.github.devcyntrix.expdrop.controller.UpdateController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class AdminJoinNotificationView implements Listener {

    private final ExpDropPlugin plugin;
    private final UpdateController controller;

    public AdminJoinNotificationView(ExpDropPlugin plugin, UpdateController controller) {
        this.plugin = plugin;
        this.controller = controller;
    }

    @EventHandler
    public void onNotifyUpdate(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (controller.getNewestVersion() == null)
            return;
        if (!player.hasPermission("expdrop.update"))
            return;
        player.sendMessage(plugin.getPrefix() + "§7A new version §a" + controller.getNewestVersion() + " §7is out.");
        player.sendMessage(plugin.getPrefix() + "§7Please update the plugin at §a" + plugin.getDescription().getWebsite());
    }

}
