package com.github.devcyntrix.expdrop.command;

import com.github.devcyntrix.expdrop.ExpDropPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExpDropExecutor implements TabExecutor {

    private final ExpDropPlugin plugin;

    public ExpDropExecutor(ExpDropPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1)
            return false;

        if ("reload".equalsIgnoreCase(args[0])) {
            try {
                plugin.reloadConfig();
                sender.sendMessage(plugin.getPrefix() + "§7The config reload was §asuccessful§7.");
            } catch (Exception e) {
                sender.sendMessage(plugin.getPrefix() + "§cSomething went wrong during the config reload.");
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
}
