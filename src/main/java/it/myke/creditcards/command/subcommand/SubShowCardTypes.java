package it.myke.creditcards.command.subcommand;

import de.themoep.inventorygui.InventoryGui;
import it.myke.creditcards.Perms;
import it.myke.creditcards.Plugin;
import it.myke.creditcards.disk.Lang;
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
