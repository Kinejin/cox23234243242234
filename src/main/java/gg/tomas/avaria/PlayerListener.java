package gg.tomas.avaria;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * Listener handling player events for the authority.
 */
public class PlayerListener implements Listener {
    private final Main plugin;
    private final AbilityManager abilityManager;
    private final InventoryManager inventoryManager;
    private final Random random = new Random();

    public PlayerListener(Main plugin, AbilityManager abilityManager, InventoryManager inventoryManager) {
        this.plugin = plugin;
        this.abilityManager = abilityManager;
        this.inventoryManager = inventoryManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (Main.hasAuthority(e.getPlayer())) {
            ensureHeart(e.getPlayer());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getHand() != EquipmentSlot.HAND) return;
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        if (item == null) return;

        if (item.isSimilar(ItemManager.createWitchGene())) {
            e.setCancelled(true);
            item.setAmount(item.getAmount() - 1);
            Main.giveAuthority(p);
            giveHeart(p);
        } else if (item.isSimilar(ItemManager.createHeartToggle())) {
            e.setCancelled(true);
            inventoryManager.toggle(p);
        } else if (inventoryManager.inAbility(p)) {
            if (item.isSimilar(ItemManager.lionBook())) {
                if (abilityManager.isLionActive(p)) abilityManager.deactivateLion(p); else abilityManager.activateLion(p);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (Main.hasAuthority(p)) {
            Main.removeAuthority(p);
            if (inventoryManager.inAbility(p)) {
                inventoryManager.toggle(p);
            }
            if (random.nextInt(100) < 1) {
                Location loc = p.getLocation();
                loc.getWorld().dropItemNaturally(loc, ItemManager.createWitchGene());
            }
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player p && inventoryManager.inAbility(p)) {
            e.setCancelled(true);
        }
    }

    private void giveHeart(Player p) {
        ItemStack heart = ItemManager.createHeartToggle();
        if (p.getInventory().firstEmpty() != -1) {
            p.getInventory().addItem(heart);
        } else {
            p.getWorld().dropItemNaturally(p.getLocation(), heart);
        }
    }

    private void ensureHeart(Player p) {
        for (ItemStack i : p.getInventory().getContents()) {
            if (i != null && i.isSimilar(ItemManager.createHeartToggle())) {
                return;
            }
        }
        giveHeart(p);
    }
}
