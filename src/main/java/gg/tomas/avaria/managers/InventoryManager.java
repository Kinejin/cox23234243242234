package gg.tomas.avaria.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import gg.tomas.avaria.util.Items;

/**
 * Handles inventory swapping between normal and ability modes.
 */
public class InventoryManager {

    private final Map<UUID, ItemStack[]> normalInvs = new HashMap<>();
    private final AbilityManager abilityManager;

    public InventoryManager(AbilityManager abilityManager) {
        this.abilityManager = abilityManager;
    }

    public void enterAbilityMode(Player player) {
        PlayerInventory inv = player.getInventory();
        normalInvs.put(player.getUniqueId(), inv.getContents());
        inv.clear();
        inv.setItem(0, Items.getLionHeartItem());
        inv.setItem(1, Items.getWindSlashItem());
        inv.setItem(2, Items.getLionCoreItem());
    }

    public void exitAbilityMode(Player player) {
        PlayerInventory inv = player.getInventory();
        ItemStack[] saved = normalInvs.remove(player.getUniqueId());
        if (saved != null) {
            inv.clear();
            inv.setContents(saved);
        }
    }

    public boolean isInAbilityMode(Player player) {
        return normalInvs.containsKey(player.getUniqueId());
    }
}
