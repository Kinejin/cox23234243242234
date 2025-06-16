package gg.tomas.avaria.listeners;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.Particle;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import gg.tomas.avaria.Main;
import gg.tomas.avaria.managers.AbilityManager;
import gg.tomas.avaria.managers.InventoryManager;
import gg.tomas.avaria.util.Items;

/**
 * Handles player events for the authority.
 */
public class PlayerListener implements Listener {

    private final Main plugin;
    private final AbilityManager abilityManager;
    private final InventoryManager invManager;
    private final Random random = new Random();

    public PlayerListener(Main plugin, AbilityManager abilityManager, InventoryManager invManager) {
        this.plugin = plugin;
        this.abilityManager = abilityManager;
        this.invManager = invManager;
    }

    @EventHandler
    public void onUseGene(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.NETHER_STAR) return;
        if (!ChatColor.stripColor(item.getItemMeta().getDisplayName()).equals("Gen de la Bruja")) return;
        Player player = event.getPlayer();
        NamespacedKey key = plugin.getAuthorityKey();
        if (player.getPersistentDataContainer().has(key, PersistentDataType.BYTE)) return;
        player.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte)1);
        ItemStack heart = Items.getHeartOfGreed();
        player.getInventory().addItem(heart);
        item.setAmount(item.getAmount()-1);
    }

    @EventHandler
    public void onHeartToggle(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.CLOCK) return;
        if (!ChatColor.stripColor(item.getItemMeta().getDisplayName()).equals("Coraz\u00f3n de la Avaricia")) return;
        event.setCancelled(true);
        Player player = event.getPlayer();
        if (invManager.isInAbilityMode(player)) {
            invManager.exitAbilityMode(player);
        } else {
            invManager.enterAbilityMode(player);
        }
    }

    @EventHandler
    public void onSwing(PlayerAnimationEvent event) {
        if (event.getAnimationType() != PlayerAnimationType.ARM_SWING) return;
        Player player = event.getPlayer();
        if (!invManager.isInAbilityMode(player)) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.IRON_SWORD) return;
        if (!ChatColor.stripColor(item.getItemMeta().getDisplayName()).equals("Corte de Viento")) return;
        if (!abilityManager.canUseWindSlash(player)) return;
        abilityManager.triggerWindSlash(player);
        player.getWorld().playSound(player.getLocation(), "entity.ender_dragon.flap", 1f, 1f);
        // simplified beam
        for (int i = 0; i < (abilityManager.isLionHeartActive(player) ? 60 : 40); i++) {
            player.getWorld().spawnParticle(org.bukkit.Particle.SWEEP_ATTACK, player.getEyeLocation().add(player.getLocation().getDirection().multiply(i)), 1);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (item.getType() == Material.CLOCK) {
            String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
            if ("Coraz\u00f3n de la Avaricia".equals(name)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onMoveItem(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item != null && item.getType() == Material.CLOCK) {
            String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
            if ("Coraz\u00f3n de la Avaricia".equals(name)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (abilityManager.isLionHeartActive(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        NamespacedKey key = plugin.getAuthorityKey();
        boolean hadAuthority = player.getPersistentDataContainer().has(key, PersistentDataType.BYTE);
        if (hadAuthority) {
            player.getPersistentDataContainer().remove(key);
            if (random.nextDouble() < plugin.getDropChance()) {
                event.getDrops().add(Items.getWitchGene());
            }
        }
        if (invManager.isInAbilityMode(player)) {
            invManager.exitAbilityMode(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        abilityManager.removeLink(event.getPlayer());
    }
}
