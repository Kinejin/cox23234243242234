package gg.tomas.avaria.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Helper class to create plugin items.
 */
public class Items {

    public static ItemStack getWitchGene() {
        ItemStack gene = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = gene.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Gen de la Bruja");
        gene.setItemMeta(meta);
        return gene;
    }

    public static ItemStack getHeartOfGreed() {
        ItemStack heart = new ItemStack(Material.CLOCK);
        ItemMeta meta = heart.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Coraz\u00f3n de la Avaricia");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        heart.setItemMeta(meta);
        return heart;
    }

    public static ItemStack getLionHeartItem() {
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Detener Cuerpo");
        book.setItemMeta(meta);
        return book;
    }

    public static ItemStack getWindSlashItem() {
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Corte de Viento");
        sword.setItemMeta(meta);
        return sword;
    }

    public static ItemStack getLionCoreItem() {
        ItemStack heart = new ItemStack(Material.HEART_OF_THE_SEA);
        ItemMeta meta = heart.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Coraz\u00f3n de Le\u00f3n");
        heart.setItemMeta(meta);
        return heart;
    }
}
