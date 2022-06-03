package it.myke.CreditCards.listener;

import com.cryptomorin.xseries.XMaterial;
import it.myke.CreditCards.Perms;
import it.myke.CreditCards.Plugin;
import it.myke.CreditCards.disk.DefNameSpacedKeys;
import it.myke.CreditCards.disk.ItemStore;
import it.myke.CreditCards.disk.Lang;
import it.myke.CreditCards.object.ATMLocation;
import it.myke.CreditCards.object.CardTypeParams;
import it.myke.CreditCards.utils.CardUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ATMListener implements @NotNull Listener {
    private final Plugin plugin;

    public ATMListener(Plugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onATMInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getHand() == EquipmentSlot.HAND) {
            Player player = event.getPlayer();
            ItemMeta playerMainMeta = player.getInventory().getItemInMainHand().getItemMeta();
            if (playerMainMeta != null && playerMainMeta.getPersistentDataContainer().has(DefNameSpacedKeys.nsCardNumber, PersistentDataType.LONG)) {

                // Withdraw
                // Checking if the block the player is interacting with is an ATM, if it is, it will check if the card is
                // valid, if it is, it will check the pin, if it is, it will open the ATM inventory.
                if (plugin.getAtmUtils().isAtm(event.getClickedBlock())) {
                    ConfigurationSection cardsection = plugin.getCreditcardConfig().getConfigurationSection("credit-cards");
                    long cardnumber = playerMainMeta.getPersistentDataContainer().get(DefNameSpacedKeys.nsCardNumber, PersistentDataType.LONG);
                    if (cardsection.contains(String.valueOf(cardnumber))) {
                        int pin = new CardUtils().getPinNumberFromCreditCard(cardnumber, cardsection);
                        plugin.getAtmUtils().pinCheck(plugin, player, String.valueOf(pin), new CardUtils().getUUIDFromCreditCard(cardnumber, cardsection));
                    } else player.sendMessage(Lang.card_not_valid);
                } else player.sendMessage(Lang.atm_not_valid);

                // Deposit
                // Checking if the player is holding a banknote, if it is, it will check if the player is interacting with an
                // ATM, if it is, it will check if the player has a credit card, if it is, it will check if the player has
                // reached the max credit, if it is, it will check if the player has reached the max deposit, if it is, it will
                // deposit the money.
            } else if (playerMainMeta != null && playerMainMeta.getPersistentDataContainer().has(DefNameSpacedKeys.nsBanknoteValue, PersistentDataType.DOUBLE)) {
                double banknoteValue = playerMainMeta.getPersistentDataContainer().get(DefNameSpacedKeys.nsBanknoteValue, PersistentDataType.DOUBLE);
                if (banknoteValue > 0 && plugin.getAtmUtils().isAtm(event.getClickedBlock())) {
                        try {
                            CardTypeParams cardType = plugin.getCardTypesLoader().getCardTypes().get(new CardUtils().getCardTypeFromUUID(player.getUniqueId(), plugin.getCreditcardConfig().getConfigurationSection("credit-cards")));
                            if (cardType.getMaxCredit() == -1 || !(banknoteValue + plugin.getEconomy().getBalance(player) > cardType.getMaxCredit())) {
                                if(!(plugin.getDepositLimits().getOrDefault(player.getUniqueId(), 0.0)+banknoteValue > cardType.getMaxDeposit())) {
                                    player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                                    player.updateInventory();
                                    player.sendMessage(Lang.money_deposited.replace("%value%", String.valueOf(banknoteValue)));
                                    plugin.getEconomy().depositPlayer(player, banknoteValue);
                                } else player.sendMessage(Lang.deposit_limit_reached.replace("%maxdeposit%", String.valueOf(cardType.getMaxDeposit())));
                            } else
                                player.sendMessage(Lang.max_credit_reached.replace("%maxcredit%", String.valueOf(cardType.getMaxCredit())));
                        } catch (NullPointerException e) {
                            player.sendMessage(Lang.you_dont_have_a_card);
                        }
                    } else player.sendMessage(Lang.atm_not_valid);
                }
            }
        }


    @EventHandler
    public void onATMWithdrawInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null && ChatColor.stripColor(event.getWhoClicked().getOpenInventory().getTitle()).equals(plugin.getConfig().getString("inventories.atm.title"))) {
            event.setCancelled(true);
            UUID cardHolder = new CardUtils().getUUIDFromCreditCard(event.getWhoClicked().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(DefNameSpacedKeys.nsCardNumber, PersistentDataType.LONG), plugin.getCreditcardConfig().getConfigurationSection("credit-cards"));
            if (event.getClickedInventory().getItem(event.getSlot()).getType() != XMaterial.GRAY_STAINED_GLASS_PANE.parseMaterial()) {
                plugin.getBanknoteUtils().getClickedBanknote(event.getSlot(), (Player) event.getWhoClicked(), cardHolder);
                plugin.getAtmUtils().refreshBalance(plugin, event.getWhoClicked().getOpenInventory().getTopInventory(), (Player) event.getWhoClicked(), Bukkit.getPlayer(cardHolder));
            }
        }
    }


    @EventHandler
    public void onATMPlace(BlockPlaceEvent event) {
        if (!event.isCancelled()) {
            event.setCancelled(true);
            if(event.getItemInHand().getItemMeta() != null && event.getItemInHand().getItemMeta().getPersistentDataContainer().has(DefNameSpacedKeys.nsATM, PersistentDataType.STRING)) {
                if (event.getPlayer().hasPermission(Perms.ATM_PLACE)) {
                    if (event.getItemInHand().getItemMeta().getPersistentDataContainer().has(DefNameSpacedKeys.nsATM, PersistentDataType.STRING)) {
                        Block b = event.getBlock();
                        ATMLocation.serializeATMLocation(b.getLocation(), "ATM-" + (plugin.getAtmUtils().getATMSize() + 1), plugin);
                        plugin.getAtmUtils().loadATMsLocation(plugin);
                        event.getPlayer().sendMessage(Lang.atm_placed);
                        event.setCancelled(false);
                    }
                } else event.getPlayer().sendMessage(Lang.no_perms);
            } else event.setCancelled(false);



        }

    }

    @EventHandler
    public void onATMBreak(BlockBreakEvent event) {
        if (!event.isCancelled() && event.getPlayer().hasPermission(Perms.ATM_BREAK)) {
            if (event.getBlock().getType() == ItemStore.atm.getType()) {
                if (plugin.getAtmUtils().isAtm(event.getBlock())) {
                    plugin.getAtmUtils().removeATM(event.getBlock(), plugin);
                    event.getPlayer().sendMessage(Lang.atm_removed);
                    plugin.getAtmUtils().loadATMsLocation(plugin);
                }
            }
        }
    }


}













