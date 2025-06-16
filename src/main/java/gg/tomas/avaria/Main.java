package gg.tomas.avaria;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import gg.tomas.avaria.managers.AbilityManager;
import gg.tomas.avaria.managers.InventoryManager;
import gg.tomas.avaria.util.Items;
import gg.tomas.avaria.listeners.PlayerListener;

/**
 * Main plugin class for AutoridadDeLaAvaricia.
 */
public class Main extends JavaPlugin {

    private static Main instance;
    private NamespacedKey authorityKey;
    private FileConfiguration config;
    private AbilityManager abilityManager;
    private InventoryManager inventoryManager;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        config = getConfig();
        authorityKey = new NamespacedKey(this, "hasAuthority");
        abilityManager = new AbilityManager(this);
        inventoryManager = new InventoryManager(abilityManager);

        Bukkit.getPluginManager().registerEvents(new PlayerListener(this, abilityManager, inventoryManager), this);
    }

    @Override
    public void onDisable() {
        abilityManager.cleanup();
    }

    public NamespacedKey getAuthorityKey() {
        return authorityKey;
    }

    public double getDropChance() {
        return config.getDouble("drop-chance", 0.01);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("avaricia") && sender instanceof Player player) {
            ItemStack gene = Items.getWitchGene();
            player.getInventory().addItem(gene);
            return true;
        }
        return false;
    }
}
