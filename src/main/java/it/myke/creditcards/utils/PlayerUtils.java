package it.myke.creditcards.utils;

import com.cryptomorin.xseries.XItemStack;
import it.myke.creditcards.disk.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class PlayerUtils {



    /**
     * If the player has an empty slot in their inventory, give them the item
     *
     * @param player The player to give the item to.
     * @param itemStack The item stack to give to the player.
     * @return A boolean value.
     */

    public boolean giveIfEmpty(Player player, ItemStack itemStack) {
        int emptySlot = XItemStack.firstEmpty(player.getInventory(), 0);
        if(emptySlot != -1) {
            player.getInventory().addItem(itemStack);
            return true;
        } else {
            player.sendMessage(Lang.inventory_full);
            return false;
        }
    }

    /**
     * It returns an ItemStack with the name and lore you specify, and the material you specify
     *
     * @param name The name of the item
     * @param lore The lore of the item
     * @param material The material of the item.
     * @return An ItemStack
     */
    public static ItemStack getItemStack(String name, String lore, Material material) {
        ItemStack i = new ItemStack(material);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(Colors.colorParser(name));
        meta.setLore(Collections.singletonList(Colors.colorParser(lore)));
        i.setItemMeta(meta);
        return i;
    }


    /**
     * For every slot in the inventory, if the slot is empty, set the slot to the item stack.
     *
     * @param inventory The inventory you want to fill
     * @param itemStack The item you want to fill the inventory with.
     */
    public static void fillEmptySlots(Inventory inventory, ItemStack itemStack) {
        for(int i = 0; i < inventory.getSize(); i++) {
            if(inventory.getItem(i) == null) {
                inventory.setItem(i, itemStack);
            }
        }
    }



    /**
     * Returns true if the distance between the two players is less than or equal to the radius squared.
     *
     * @param radius The radius of the circle.
     * @param player1 The player you want to check the distance from.
     * @param player2 The player you want to check the distance to.
     * @return A boolean value.
     */
    public static boolean checkDistance(int radius, Player player1, Player player2) {
        return player1.getLocation().distanceSquared(player2.getLocation()) <= radius * radius;
    }






}
