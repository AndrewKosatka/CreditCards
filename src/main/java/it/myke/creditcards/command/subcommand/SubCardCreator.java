package it.myke.creditcards.command.subcommand;

import de.themoep.inventorygui.InventoryGui;
import it.myke.creditcards.Plugin;
import it.myke.creditcards.disk.Lang;
import it.myke.creditcards.object.CardConfigStep;
import it.myke.creditcards.object.CardTypeParams;
import it.myke.creditcards.timer.ConfirmTimer;
import it.myke.creditcards.utils.PlayerUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;


public class SubCardCreator {



    // Subcommand of CreditCard Commands
    public SubCardCreator(Plugin plugin, Player sender, String player) {
        Player cardHolder = Bukkit.getPlayer(player);
        if (cardHolder != sender) {
            if (cardHolder != null && cardHolder.isOnline()) {
                int cmdradious = plugin.getConfig().getInt("radious-cmd-creditcard");
                if(cmdradious != -1) {
                    if(!PlayerUtils.checkDistance(cmdradious, sender, cardHolder)) {
                        sender.sendMessage(Lang.creditcard_command_distance.replace("%distance%", String.valueOf(cmdradious)));
                        return;
                    }

                }
                InventoryGui cardTypeSelector = plugin.getInventoryManager().cardTypeSelectorInventory;
                cardTypeSelector.getElement('g').setAction(click -> {
                    String cardType = ChatColor.stripColor(click.getElement().getItem(click.getWhoClicked(), click.getSlot()).getItemMeta().getDisplayName()).toLowerCase();
                    cardTypeSelector.close();
                    HashMap<String, CardTypeParams> cardTypes = plugin.getCardTypesLoader().getCardTypes();
                    if (cardTypes.containsKey(cardType)) {
                        CardTypeParams typeParams = cardTypes.get(cardType);
                        if(!plugin.getCardConfigPlayers().contains(cardHolder.getName())) {
                            CardConfigStep cardConfigStep = new CardConfigStep(typeParams, sender, cardHolder, cardType);
                            String randomString = RandomStringUtils.randomAlphanumeric(10);
                            TextComponent textComponent = new TextComponent(Lang.confirm_card_message);
                            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/creditcard confirm " + randomString));
                            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Lang.confirm_card_message_hover).create()));
                            plugin.getCardConfigPlayers().add(player);
                            plugin.getCardConfigStep().put(randomString, cardConfigStep);
                            new ConfirmTimer(randomString, plugin).runTaskLater(plugin, plugin.getConfig().getInt("confirm-cancelled-tickage"));
                            cardHolder.sendMessage(textComponent);

                            sender.sendMessage(Lang.card_creating.replace("%player%", player));
                        } else sender.sendMessage(Lang.already_in_creation_process);
                    } else sender.sendMessage(Lang.cardtype_doesnt_exists.replace("%input%", cardType).replace("%cardtypes%", String.valueOf(plugin.getCardTypesLoader().getCardTypes().keySet()).replace("[", "").replace("]", "")));
                    return true;
                });
                cardTypeSelector.show(sender);

            } else sender.sendMessage(Lang.player_doesnt_exists.replace("%player%", player));


        } else sender.sendMessage(Lang.cant_card_yourself);
    }




}
