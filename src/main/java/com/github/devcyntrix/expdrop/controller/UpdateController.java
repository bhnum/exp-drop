package com.github.devcyntrix.expdrop.controller;

import com.github.devcyntrix.expdrop.ExpDropPlugin;
import com.github.devcyntrix.expdrop.util.UpdateChecker;
import com.google.inject.Inject;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Checks for the newest version by using the SpigotMC API
 */
@Getter
public class UpdateController implements Closeable {

    private String newestVersion;
    private final BukkitTask updateScheduler;

    private final List<Consumer<String>> subscriberList = new ArrayList<>();

    @Inject
    public UpdateController(ExpDropPlugin plugin, int resourceId) {
        UpdateChecker checker = new UpdateChecker(plugin, resourceId);
        updateScheduler = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            String version = checker.getPublishedVersion();
            if (version == null)
                return;
            if (plugin.getDescription().getVersion().equals(version))
                return;
            this.newestVersion = version;
            subscriberList.forEach(subscriber -> subscriber.accept(newestVersion));
        }, 0, 20 * 60 * 30); // Every 30 minutes

    }

    public void subscribe(@NotNull Consumer<String> subscriber) {
        subscriberList.add(subscriber);
    }

    @Override
    public void close() {
        if (updateScheduler != null) {
            updateScheduler.cancel();
        }
    }
}
