package gg.tomas.avaria;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages inventory switching for the Heart of Greed.
 */
public class InventoryManager implements Listener {
    private final Main plugin;
    private final AbilityManager abilityManager;
    private final Map<UUID, ItemStack[]> normalInv = new HashMap<>();
    private final Map<UUID, Boolean> abilityMode = new HashMap<>();

    public InventoryManager(Main plugin, AbilityManager abilityManager) {
        this.plugin = plugin;
        this.abilityManager = abilityManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Toggles inventory mode.
     */
    public void toggle(Player player) {
        if (abilityMode.getOrDefault(player.getUniqueId(), false)) {
            restore(player);
        } else {
            enterAbility(player);
        }
    }

    private void enterAbility(Player player) {
        normalInv.put(player.getUniqueId(), player.getInventory().getContents());
        player.getInventory().clear();
        player.getInventory().setItem(0, ItemManager.lionBook());
        player.getInventory().setItem(1, ItemManager.windSlash());
        player.getInventory().setItem(2, ItemManager.lionHeartItem());
        abilityMode.put(player.getUniqueId(), true);
    }

    private void restore(Player player) {
        ItemStack[] items = normalInv.remove(player.getUniqueId());
        if (items != null) {
            player.getInventory().clear();
            player.getInventory().setContents(items);
        }
        abilityMode.remove(player.getUniqueId());
    }

    public boolean inAbility(Player p) {
        return abilityMode.getOrDefault(p.getUniqueId(), false);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (inAbility(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player p && inAbility(p)) {
            e.setCancelled(true);
        }
    }
}
