package com.github.devcyntrix.expdrop.listener;

import com.github.devcyntrix.expdrop.ExpDropPlugin;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

import static com.github.devcyntrix.expdrop.ExpDropPlugin.EXP_DROP_ORB;

public class MendingProtectionListener implements Listener {

    private final ExpDropPlugin plugin;

    public MendingProtectionListener(ExpDropPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onThrowExpBottle(ExpBottleEvent event) {
        ThrownExpBottle entity = event.getEntity();
        ItemStack item = entity.getItem();
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        Byte b = persistentDataContainer.get(plugin.getNamespacedKey(), PersistentDataType.BYTE);
        if (Objects.equals((byte) 1, b)) {
            int experience = event.getExperience();
            event.setExperience(0);
            entity.getWorld().spawn(entity.getLocation(), ExperienceOrb.class, experienceOrb -> {
                experienceOrb.setMetadata(EXP_DROP_ORB, new FixedMetadataValue(plugin, null));
                experienceOrb.setExperience(experience);
            });
        }
    }

    @EventHandler
    public void onExp(PlayerItemMendEvent event) {
        if (event.getExperienceOrb().hasMetadata(EXP_DROP_ORB)) {
            event.setCancelled(true);
        }
    }

}
