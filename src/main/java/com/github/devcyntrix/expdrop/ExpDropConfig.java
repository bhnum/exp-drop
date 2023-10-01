package com.github.devcyntrix.expdrop;

import org.bukkit.configuration.ConfigurationSection;

public record ExpDropConfig(
        boolean dropAllExp,
        double keepExpRate,
        boolean convertToBottles
) {
    public static ExpDropConfig parse(ConfigurationSection configuration) {
        boolean dropAllExp = configuration.getBoolean("drop-all-exp", false);
        double keepPercentage = configuration.getDouble("keep-exp-percentage", 100.0);
        boolean convertToBottles = configuration.getBoolean("convert-to-bottles", false);
        return new ExpDropConfig(dropAllExp, keepPercentage / 100.0, convertToBottles);
    }
}
