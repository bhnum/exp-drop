package com.github.devcyntrix.expdrop.listener;

import org.bukkit.GameRule;
import com.github.devcyntrix.expdrop.ExpDropConfig;
import com.github.devcyntrix.expdrop.ExpDropPlugin;
import com.github.devcyntrix.expdrop.util.ExpUtil;
import org.bukkit.Material;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.github.devcyntrix.expdrop.ExpDropPlugin.EXP_DROP_ORB;

public class ExpDropListener implements Listener {

    private final ExpDropPlugin plugin;

    public ExpDropListener(ExpDropPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent event) {
        ExpDropConfig config = plugin.getExpDropConfig();
        Player player = event.getEntity();

        double exp = event.getDroppedExp();
        if (config.dropAllExp()) {
            exp = ExpUtil.getPlayerExp(player);
        }

        exp *= config.keepExpRate();

        if (!config.convertToBottles()) {
            int droppedExp = (int) Math.round(exp);
            event.setDroppedExp(droppedExp);

            if (player.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY)) {
                event.setNewExp(Math.max(0, event.getNewExp() - droppedExp));
            }

            if (event.getDroppedExp() > 0 && config.mendingProtection()) {
                player.getWorld().spawn(player.getLocation(), ExperienceOrb.class, experienceOrb -> {
                    experienceOrb.setExperience(event.getDroppedExp());
                    experienceOrb.setMetadata(EXP_DROP_ORB, new FixedMetadataValue(plugin, null));
                });
                event.setDroppedExp(0);
            }
            return;
        }

        // Converts the experience points to bottle o' enchanting
        int numberOfBottles = (int) (Math.round(exp) / 7.0); // Seven is the average of experience points which drops a Bottle o' Enchanting (https://minecraft.fandom.com/wiki/Bottle_o%27_Enchanting#Usage)
        do {
            int size = Math.min(numberOfBottles, 64);

            ItemStack e = new ItemStack(Material.EXPERIENCE_BOTTLE, size);
            if (config.mendingProtection()) {
                ItemMeta itemMeta = e.getItemMeta();
                PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
                pdc.set(plugin.getNamespacedKey(), PersistentDataType.BYTE, (byte) 1);
                e.setItemMeta(itemMeta);
            }

            event.getDrops().add(e);
            numberOfBottles -= size;
        } while (numberOfBottles > 0);
        event.setDroppedExp(0);
    }


}
