package it.myke.creditcards.command.subcommand;

import de.themoep.inventorygui.InventoryGui;
import it.myke.creditcards.Perms;
import it.myke.creditcards.Plugin;
import it.myke.creditcards.disk.Lang;
import it.myke.creditcards.object.Banknote;
import it.myke.creditcards.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SubBanknotes {

    // Subcommand of Banknote Commands
    public SubBanknotes(Player player, Plugin plugin) {
        InventoryGui inventoryGui = plugin.getInventoryManager().banknotesInventory;
        inventoryGui.getElement('g').setAction(click -> {
            String banknotename = ChatColor.stripColor(click.getElement().getItem(click.getWhoClicked(), click.getSlot()).getItemMeta().getDisplayName());
            Banknote banknote = plugin.getBanknoteUtils().parseBanknote(banknotename, plugin.getBanknoteConfig());
            if(banknote != null) {
                if(player.hasPermission(Perms.ATM_WITHDRAW)) {
                    if (new PlayerUtils().giveIfEmpty(player, banknote.getItemstack())) {
                        player.sendMessage(Lang.banknote_received.replace("%value%", String.valueOf(banknote.getValue())));
                        if(click.getEvent().getClick().isShiftClick()) {
                            inventoryGui.close();
                        }
                    }
                } else player.sendMessage(Lang.no_perms);
            } else player.sendMessage(Lang.banknote_doesnt_exists.replace("%name%", banknotename));
            return true;
        });
        inventoryGui.show(player);


    }


}
