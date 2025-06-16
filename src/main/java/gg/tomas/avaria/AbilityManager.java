package gg.tomas.avaria;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Handles active abilities for players.
 */
public class AbilityManager implements Listener {
    private final Main plugin;
    private final Map<UUID, Integer> heartTimer = new HashMap<>();
    private final Map<UUID, Boolean> lionActive = new HashMap<>();

    public AbilityManager(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        new TickTask().runTaskTimer(plugin, 1L, 1L);
    }

    /**
     * Activates Lion's Heart for player.
     */
    public void activateLion(Player p) {
        lionActive.put(p.getUniqueId(), true);
        heartTimer.put(p.getUniqueId(), 100);
        p.addScoreboardTag("lionHeartActive");
    }

    public void deactivateLion(Player p) {
        lionActive.remove(p.getUniqueId());
        p.removeScoreboardTag("lionHeartActive");
    }

    public boolean isLionActive(Player p) {
        return lionActive.getOrDefault(p.getUniqueId(), false);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player) {
            Player p = (Player) entity;
            if (isLionActive(p)) {
                e.setCancelled(true);
            }
        }
    }

    private class TickTask extends BukkitRunnable {
        @Override
        public void run() {
            for (UUID uuid : lionActive.keySet()) {
                int t = heartTimer.getOrDefault(uuid, 0) - 1;
                if (t <= 0) {
                    Player p = Bukkit.getPlayer(uuid);
                    if (p != null) {
                        double dmg = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() / 2.0 + 0.5;
                        p.damage(dmg);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 1));
                    }
                    t = 100;
                    lionActive.remove(uuid);
                    if (p != null) p.removeScoreboardTag("lionHeartActive");
                }
                heartTimer.put(uuid, t);
            }
        }
    }

    public void cleanup() {
        this.lionActive.clear();
        this.heartTimer.clear();
    }
}
