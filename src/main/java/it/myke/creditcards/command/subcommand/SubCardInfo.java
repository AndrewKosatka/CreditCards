package it.myke.creditcards.command.subcommand;

import it.myke.creditcards.Perms;
import it.myke.creditcards.Plugin;
import it.myke.creditcards.disk.Lang;
import it.myke.creditcards.utils.CardUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SubCardInfo {

    // Subcommand of CreditCard Commands
    public SubCardInfo(Plugin plugin, Player player, String arg) {

        if(Bukkit.getPlayer(arg) != null) {
            Player processedPlayer = Bukkit.getPlayer(arg);
            if(player.hasPermission(Perms.CREDITCARD_INFO_OTHERS)) {
                sendCardInfo(plugin, player, processedPlayer);
            }
        } else if(player.hasPermission(Perms.CREDITCARD_INFO)) {
            sendCardInfo(plugin, player, player);
        } else player.sendMessage(Lang.no_perms);
    }


    public void sendCardInfo(Plugin plugin, Player player, Player processedPlayer) {
        UUID processedUUID = processedPlayer.getUniqueId();
        if (plugin.getCreditcardConfig().isConfigurationSection("credit-cards") && plugin.getCreditcardConfig().getConfigurationSection("credit-cards").getKeys(false).size() > 0) {
            if (plugin.getCreditcardConfig().getConfigurationSection("credit-cards").contains(String.valueOf(processedUUID))) {
                String balance = CardUtils.fixLimit(plugin.getEconomy().getBalance(processedPlayer.getName()));
                String cardType = CardUtils.firstCharUpper(plugin.getCreditcardConfig().getString("credit-cards." + processedUUID + ".card-type"));
                String cardNumber = CardUtils.firstCharUpper(plugin.getCreditcardConfig().getString("credit-cards." + processedUUID + ".card-number"));
                String cardHolder = processedPlayer.getName();
                player.sendMessage(Lang.card_info_message.replace("%balance%", balance).replace("%creditcardnumber%", cardNumber).replace("%creditcardtype%", cardType).replace("%cardholder%", cardHolder));
            } else player.sendMessage(Lang.card_info_nocard);
        } else player.sendMessage(Lang.card_info_nocards_exists);
    }





}
