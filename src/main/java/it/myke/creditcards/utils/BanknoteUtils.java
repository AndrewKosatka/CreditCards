package it.myke.creditcards.utils;

import com.cryptomorin.xseries.XMaterial;
import it.myke.creditcards.Plugin;
import it.myke.creditcards.disk.DefNameSpacedKeys;
import it.myke.creditcards.disk.Lang;
import it.myke.creditcards.object.Banknote;
import it.myke.creditcards.object.CardTypeParams;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BanknoteUtils {
    String basicSection = "banknotes";

    @Getter
    private final List<Banknote> banknotes;
    private final Plugin plugin;

    public BanknoteUtils(Plugin plugin) {
        this.plugin = plugin;
        banknotes = loadBanknotes();
    }


    /**
     * "Get all the banknotes from the config file, and if there are any, validate them and return them."
     *
     * The first line gets the configuration section from the config file. The configuration section is the part of the
     * config file that contains all the banknotes
     *
     * @return A list of banknotes.
     */
    public List<Banknote> loadBanknotes() {
        ConfigurationSection section = plugin.getBanknoteConfig().getConfigurationSection(basicSection);
        return section != null ? validateBanknotes(section.getKeys(false), plugin.getBanknoteConfig()) : new ArrayList<>();
    }




    /**
     * It takes a set of keys, and a config file, and returns a list of banknotes
     *
     * @param keys The keys of the banknotes in the config.yml
     * @param config The FileConfiguration object of the config.yml file.
     * @return A list of banknotes.
     */
    private List<Banknote> validateBanknotes(@NotNull Set<String> keys, FileConfiguration config) {
        List<Banknote> banknotes = new ArrayList<>();
        for(String key : keys) {
            Material material = XMaterial.matchXMaterial(config.getString(basicSection + "." + key + ".material")).isPresent() ? XMaterial.matchXMaterial(config.getString(basicSection + "." + key + ".material")).get().parseMaterial() : null;
            String lore = config.getString(basicSection + "." + key + ".lore");
            String name = config.getString(basicSection + "." + key + ".name");
            int value = config.getInt(basicSection + "." + key + ".value");

            if(material != null)
            if(lore != null)
            if(name != null)
            if(value != 0)
                banknotes.add(new Banknote(name, lore, value, material, key));

        }
        return banknotes;
    }

    /**
     * It parses a banknote from a file configuration
     *
     * @param banknoteName The name of the banknote in the config.yml
     * @param banknotesConfig The FileConfiguration of the banknotes.yml file
     * @return A Banknote object.
     */
    public Banknote parseBanknote(String banknoteName, FileConfiguration banknotesConfig) {
        if(plugin.getBanknoteConfig().getString(basicSection + "." + banknoteName) != null) {
            Material material = XMaterial.matchXMaterial(banknotesConfig.getString(basicSection + "." + banknoteName + ".material")).isPresent() ? XMaterial.matchXMaterial(banknotesConfig.getString(basicSection + "." + banknoteName + ".material")).get().parseMaterial() : null;
            String lore = banknotesConfig.getString(basicSection + "." + banknoteName + ".lore");
            String name = banknotesConfig.getString(basicSection + "." + banknoteName + ".name");
            double value = banknotesConfig.getDouble(basicSection + "." + banknoteName + ".value");

            if(material != null)
                if(lore != null)
                    if(name != null)
                        if(value != 0) {
                            ItemStack banknoteItemStack = PlayerUtils.getItemStack(name, lore, material);
                            ItemMeta meta = banknoteItemStack.getItemMeta();
                            meta.getPersistentDataContainer().set(DefNameSpacedKeys.nsBanknoteValue, PersistentDataType.DOUBLE, value);
                            banknoteItemStack.setItemMeta(meta);
                            Banknote banknote = new Banknote(name, lore, value, material, banknoteName);
                            banknote.setItemstack(banknoteItemStack);
                            return banknote;
                        }
/*
                            ItemStack banknoteItemStack = PlayerUtils.getItemStack(name, lore, material);
                            ItemMeta meta =
 */
        }
        return null;

    }




    /**
     * It checks if the player has enough money to withdraw the banknote, if he has enough money it checks if he has
     * reached the maximum withdraw limit, if he hasn't reached the maximum withdraw limit it gives him the banknote and
     * withdraws the money from his balance
     *
     * @param slot The slot that was clicked
     * @param player The player who clicked the button
     * @param cardHolderUUID The UUID of the card holder.
     */
    public void getClickedBanknote(int slot, Player player, UUID cardHolderUUID) {
        int reqbanknote = slot-10;
        if(reqbanknote >=0 && reqbanknote < 7) {
            Banknote banknote = plugin.getBanknoteUtils().getBanknotes().get(reqbanknote);
            double holderbalance = plugin.getEconomy().getBalance(Bukkit.getPlayer(cardHolderUUID));
            CardTypeParams cardType = plugin.getCardTypesLoader().getCardTypes().get(new CardUtils().getCardTypeFromUUID(cardHolderUUID, plugin.getCreditcardConfig().getConfigurationSection("credit-cards")));
            if(holderbalance-banknote.getValue() > 0) {
                if(plugin.getWithdrawLimits().getOrDefault(cardHolderUUID, 0.0)+banknote.getValue() <= cardType.getMaxWithdraw()) {
                    ItemStack banknotePDC = banknote.getItemstack();
                    ItemMeta meta = banknotePDC.getItemMeta();
                    meta.getPersistentDataContainer().set(DefNameSpacedKeys.nsBanknoteValue, PersistentDataType.DOUBLE, banknote.getValue());
                    banknotePDC.setItemMeta(meta);
                    if(new PlayerUtils().giveIfEmpty(player, banknotePDC)) {
                        plugin.getEconomy().withdrawPlayer(Bukkit.getPlayer(cardHolderUUID), banknote.getValue());
                        player.sendMessage(Lang.money_withdrawn.replace("%value%", String.valueOf(banknote.getValue())));
                        plugin.getWithdrawLimits().put(cardHolderUUID, plugin.getWithdrawLimits().getOrDefault(cardHolderUUID, 0.0)+banknote.getValue());
                    }
                } else player.sendMessage(Lang.max_withdraw_reached.replace("%maxwithdraw%", String.valueOf(cardType.getMaxWithdraw())));
            } else player.sendMessage(Lang.not_enough_money);
        }

    }



}
