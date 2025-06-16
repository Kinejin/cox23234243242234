package gg.tomas.avaria;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Utility to generate custom items used in the plugin.
 */
public final class ItemManager {

    private ItemManager() {}

    /**
     * Creates the Witch Gene item.
     */
    public static ItemStack createWitchGene() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Gen de la Bruja");
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Creates the Heart of Greed toggle item.
     */
    public static ItemStack createHeartToggle() {
        ItemStack item = new ItemStack(Material.CLOCK);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.AQUA + "Coraz\u00f3n de la Avaricia");
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Ability items.
     */
    public static ItemStack lionBook() {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Detener Cuerpo");
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack windSlash() {
        ItemStack item = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "Corte de Viento");
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack lionHeartItem() {
        ItemStack item = new ItemStack(Material.HEART_OF_THE_SEA);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Coraz\u00f3n de Le\u00f3n");
            item.setItemMeta(meta);
        }
        return item;
    }
}
