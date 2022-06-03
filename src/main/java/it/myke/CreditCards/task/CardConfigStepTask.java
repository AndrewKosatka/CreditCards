package it.myke.CreditCards.task;

import it.myke.CreditCards.Plugin;
import it.myke.CreditCards.disk.DefNameSpacedKeys;
import it.myke.CreditCards.disk.Lang;
import it.myke.CreditCards.object.CardConfigStep;
import it.myke.CreditCards.object.CardTypeParams;
import it.myke.CreditCards.utils.CardUtils;
import it.myke.CreditCards.utils.Colors;
import it.myke.CreditCards.utils.PlayerUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.upperlevel.spigot.book.BookUtil;

public class CardConfigStepTask {


    // This is the task that is run when the player has confirmed the card creation.
    public CardConfigStepTask(CardConfigStep configStep, String randomString, CommandSender sender, Plugin plugin) {
        if (configStep != null) {
            Player banker = configStep.getBanker();
            Player player = configStep.getSender();
            CardTypeParams typeParams = configStep.getCardTypeParams();
            String cardType = configStep.getCardType();

            if (sender == player) {
                if (plugin.getEconomy().has(player, typeParams.getTypeCost())) {
                    plugin.getEconomy().withdrawPlayer(player, typeParams.getTypeCost());
                    int randomPin = CardUtils.getRandomPin();
                    long randomNumber = CardUtils.getRandomCard();
                    ItemStack creditStack = plugin.getItemStore().getPlayerCreditCard(plugin.getItemStore().getCreditCard(), player.getName(), String.valueOf(randomNumber));
                    ItemMeta meta = creditStack.getItemMeta();
                    meta.getPersistentDataContainer().set(DefNameSpacedKeys.nsCardNumber, PersistentDataType.LONG, randomNumber);
                    creditStack.setItemMeta(meta);
                    if (new PlayerUtils().giveIfEmpty(player, creditStack)) {
                        player.sendMessage(Lang.withdraw_cardtype_cost.replace("%cardtypecost%", String.valueOf(typeParams.getTypeCost())).replace("%cardtype%", cardType));
                        new CardSaver(plugin, plugin.getCreditcardConfig(), "creditcards.yml", player.getUniqueId(), randomPin, randomNumber, cardType);
                        givePinBook(Bukkit.getPlayer(player.getName()), plugin, randomPin);
                        banker.sendMessage(Lang.card_created_successfully);
                        plugin.getCardConfigStep().remove(randomString);
                        plugin.getCardConfigPlayers().remove(player.getName());
                    }

                } else {
                    plugin.getCardConfigStep().remove(randomString);
                    plugin.getCardConfigPlayers().remove(player.getName());
                    banker.sendMessage(Lang.not_enough_money_cardtype.replace("%player%", player.getName()));
                    banker.sendMessage(Lang.cardcreation_process_failed);
                    player.sendMessage(Lang.not_enough_money);
                    player.sendMessage(Lang.cardcreation_process_failed);


                }
            }
        }

    }

    /**
     * It gives a player a book with a pin in it
     *
     * @param player The player you want to give the book to.
     * @param plugin The plugin instance
     * @param pin The pin that the player will receive
     */
    public void givePinBook(Player player, Plugin plugin, int pin) {

        ItemStack book = BookUtil.writtenBook().author(plugin.getConfig().getString("pin-book.author")).title(Colors.colorParser(plugin.getConfig().getString("pin-book.title"))).pages(new BaseComponent[]{
                new TextComponent(Colors.colorParser(plugin.getConfig().getString("pin-book.text").replace("%pin%", String.valueOf(pin)))),}).build();
        if(!new PlayerUtils().giveIfEmpty(player, book))
            player.sendMessage(Lang.pinbook_failed_sending_viamessage.replace("%pin%", String.valueOf(pin)));



    }

}
