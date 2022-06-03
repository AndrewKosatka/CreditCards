package it.myke.CreditCards.inventory.inventories;

import com.cryptomorin.xseries.XMaterial;
import it.myke.CreditCards.Plugin;
import it.myke.CreditCards.object.Banknote;
import it.myke.CreditCards.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class ATMInventory {

    public Inventory getATMInventory(Plugin plugin) {
        Inventory inventory = Bukkit.createInventory(null, 27, plugin.getConfig().getString("inventories.atm.title"));
        int i = 10;
        for(Banknote banknote : plugin.getBanknoteUtils().getBanknotes()) {
            if(i + 1 < 18) {
                inventory.setItem(i, banknote.getItemstack());
                i++;
            }
        }
        PlayerUtils.fillEmptySlots(inventory, XMaterial.GRAY_STAINED_GLASS_PANE.parseItem());
        return inventory;
    }


}
