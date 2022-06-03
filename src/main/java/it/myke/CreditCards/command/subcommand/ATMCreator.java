package it.myke.CreditCards.command.subcommand;

import it.myke.CreditCards.Perms;
import it.myke.CreditCards.disk.DefNameSpacedKeys;
import it.myke.CreditCards.disk.ItemStore;
import it.myke.CreditCards.disk.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ATMCreator {

    // Subcommand of ATM Commands
    public ATMCreator(CommandSender sender) {
        Player player = (Player) sender;
        if (player.hasPermission(Perms.ATM_GET)) {
            ItemStack atm = ItemStore.atm;
            ItemMeta atmItemMeta = atm.getItemMeta();
            atmItemMeta.getPersistentDataContainer().set(DefNameSpacedKeys.nsATM, PersistentDataType.STRING, "ATM");
            atm.setItemMeta(atmItemMeta);
            player.getInventory().addItem(atm);
            player.sendMessage(Lang.atm_received);
        } else player.sendMessage(Lang.no_perms);
    }

}
