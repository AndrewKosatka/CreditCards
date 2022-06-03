package it.myke.CreditCards.command.subcommand;

import de.themoep.inventorygui.InventoryGui;
import it.myke.CreditCards.Perms;
import it.myke.CreditCards.Plugin;
import it.myke.CreditCards.disk.Lang;
import org.bukkit.entity.Player;

public class SubShowCardTypes {


    // Subcommand of CreditCard Commands
    public SubShowCardTypes(Plugin plugin, Player player) {
        if(player.hasPermission(Perms.CREDITCARD_TYPES_SHOW)) {
            InventoryGui inventoryGui = plugin.getInventoryManager().cardTypesGUI;
            inventoryGui.show(player);
        } else player.sendMessage(Lang.no_perms);
    }

}
