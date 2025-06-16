package gg.tomas.avaria.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import gg.tomas.avaria.Main;

/**
 * Manages abilities and Lion's Heart state per player.
 */
public class AbilityManager {

    private final JavaPlugin plugin;
    private final Map<UUID, PlayerData> playerData = new HashMap<>();
    private BukkitRunnable task;

    public AbilityManager(JavaPlugin plugin) {
        this.plugin = plugin;
        startTask();
    }

    public void toggleLionHeart(Player player) {
        PlayerData data = playerData.computeIfAbsent(player.getUniqueId(), k -> new PlayerData());
        if (data.cooldown > 0) return;
        data.active = !data.active;
        if (data.active) {
            data.timer = data.linked != null ? Integer.MAX_VALUE : 100;
            player.addScoreboardTag("lionHeartActive");
        } else {
            player.removeScoreboardTag("lionHeartActive");
            data.cooldown = 60; // 3s
        }
    }

    public boolean isLionHeartActive(Player player) {
        PlayerData data = playerData.get(player.getUniqueId());
        return data != null && data.active;
    }

    public boolean canUseWindSlash(Player player) {
        PlayerData data = playerData.computeIfAbsent(player.getUniqueId(), k -> new PlayerData());
        return data.windCooldown <= 0;
    }

    public void triggerWindSlash(Player player) {
        PlayerData data = playerData.computeIfAbsent(player.getUniqueId(), k -> new PlayerData());
        data.windCooldown = isLionHeartActive(player) ? 100 : 140; // 5s vs 7s
    }

    public void setLink(Player player, LivingEntity target) {
        PlayerData data = playerData.computeIfAbsent(player.getUniqueId(), k -> new PlayerData());
        data.linked = target.getUniqueId();
        data.timer = Integer.MAX_VALUE;
    }

    public void removeLink(Player player) {
        PlayerData data = playerData.get(player.getUniqueId());
        if (data != null) {
            data.linked = null;
        }
    }

    public void cleanup() {
        if (task != null) task.cancel();
        playerData.clear();
    }

    private void startTask() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID id : playerData.keySet()) {
                    Player player = Bukkit.getPlayer(id);
                    if (player == null) continue;
                    PlayerData data = playerData.get(id);
                    if (data.active) {
                        data.timer--;
                        if (data.timer <= 0) {
                            double dmg = player.getAttribute(Attribute.MAX_HEALTH).getValue() / 2.0 + 0.5;
                            player.damage(dmg);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20, 1));
                            data.timer = data.linked != null ? Integer.MAX_VALUE : 100;
                            data.active = false;
                            player.removeScoreboardTag("lionHeartActive");
                        }
                    } else if (data.cooldown > 0) {
                        data.cooldown--;
                    }
                    if (data.windCooldown > 0) {
                        data.windCooldown--;
                    }
                    if (data.linked != null) {
                        LivingEntity linked = getLinkedEntity(data.linked);
                        if (linked == null || linked.isDead()) {
                            removeLink(player);
                            data.timer = 100;
                            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 0));
                        }
                    }
                }
            }
        };
        task.runTaskTimer(plugin, 1L, 1L);
    }

    private LivingEntity getLinkedEntity(UUID uuid) {
        Entity e = Bukkit.getEntity(uuid);
        return e instanceof LivingEntity ? (LivingEntity) e : null;
    }

    private static class PlayerData {
        boolean active = false;
        int timer = 100;
        int cooldown = 0;
        int windCooldown = 0;
        UUID linked = null;
    }
}
