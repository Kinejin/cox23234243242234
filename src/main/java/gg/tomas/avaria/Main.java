package gg.tomas.avaria;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/**
 * Main plugin class for AutoridadDeLaAvaricia.
 */
public class Main extends JavaPlugin {
    public static Main instance;
    public static final NamespacedKey KEY_AUTHORITY = new NamespacedKey("avaricia", "hasAuthority");

    private AbilityManager abilityManager;
    private InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        instance = this;
        this.abilityManager = new AbilityManager(this);
        this.inventoryManager = new InventoryManager(this, abilityManager);

        Bukkit.getPluginManager().registerEvents(new PlayerListener(this, abilityManager, inventoryManager), this);
    }

    @Override
    public void onDisable() {
        abilityManager.cleanup();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Solo jugadores");
            return true;
        }
        Player p = (Player) sender;
        if (p.hasPermission("avaricia.admin")) {
            ItemStack gen = ItemManager.createWitchGene();
            p.getInventory().addItem(gen);
            p.sendMessage("Has recibido el Gen de la Bruja");
        }
        return true;
    }

    /**
     * Checks if the player currently owns the authority.
     */
    public static boolean hasAuthority(Player player) {
        PersistentDataContainer c = player.getPersistentDataContainer();
        return c.has(KEY_AUTHORITY, PersistentDataType.BYTE) && c.getByte(KEY_AUTHORITY, (byte)0) == 1;
    }

    /**
     * Marks the player as owner of the authority.
     */
    public static void giveAuthority(Player player) {
        player.getPersistentDataContainer().set(KEY_AUTHORITY, PersistentDataType.BYTE, (byte)1);
    }

    /**
     * Removes the authority tag from a player.
     */
    public static void removeAuthority(Player player) {
        player.getPersistentDataContainer().remove(KEY_AUTHORITY);
    }
}
