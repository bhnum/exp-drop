package com.github.devcyntrix.expdrop;

import org.bukkit.configuration.ConfigurationSection;

public record ExpDropConfig(
        boolean checkForUpdates,
        boolean dropAllExp,
        double keepExpRate,
        boolean convertToBottles
) {
    public static ExpDropConfig parse(ConfigurationSection configuration) {
        boolean checkForUpdates = configuration.getBoolean("check-for-updates", true);
        boolean dropAllExp = configuration.getBoolean("drop-all-exp", false);
        double keepPercentage = configuration.getDouble("keep-exp-percentage", 100.0);
        boolean convertToBottles = configuration.getBoolean("convert-to-bottles", false);
        return new ExpDropConfig(checkForUpdates, dropAllExp, keepPercentage / 100.0, convertToBottles);
    }
}
