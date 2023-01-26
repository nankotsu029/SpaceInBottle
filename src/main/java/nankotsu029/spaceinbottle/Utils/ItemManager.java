package nankotsu029.spaceinbottle.Utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    public static ItemStack CreateCustomItem(Material material, int amount, String DisplayName, String lore1, String lore2, boolean IsEnchant, boolean IsUnbreakable) {
        ItemStack item = new ItemStack(material,amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(DisplayName);
        List<String> lore = new ArrayList<>();
        lore.add(lore1);
        lore.add(lore2);
        meta.setLore(lore);
        if (IsEnchant) {
            meta.addEnchant(Enchantment.LUCK,1,false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        meta.setUnbreakable(IsUnbreakable);
        item.setItemMeta(meta);
        return item;
    }
}
