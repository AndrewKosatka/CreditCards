package it.myke.creditcards.disk;

import com.cryptomorin.xseries.XMaterial;
import it.myke.creditcards.Plugin;
import it.myke.creditcards.utils.Colors;
import it.myke.creditcards.utils.PlayerUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemStore {
    public static ItemStack atm, creditcard;
    private final Plugin plugin;


    public ItemStore(Plugin plugin) {
        this.plugin = plugin;
        atm = getAtm();
        creditcard = getCreditCard();
    }

    public ItemStack getAtm() {
        return deserialize(plugin.getConfig().getConfigurationSection("atm.options"));
    }

    public ItemStack getCreditCard() {
        return deserialize(plugin.getConfig().getConfigurationSection("creditcard.options"));
    }

    public ItemStack getBalanceItem(String player, String cardholder, double balance) {
        ItemStack basic = deserialize(plugin.getConfig().getConfigurationSection("balance-item.options"));
        ItemMeta basicmeta = basic.getItemMeta();
        basicmeta.setDisplayName(basicmeta.getDisplayName().replace("%player%", player));
        basicmeta.setDisplayName(basicmeta.getDisplayName().replace("%cardholder%", cardholder));
        basicmeta.setDisplayName(basicmeta.getDisplayName().replace("%balance%", String.valueOf(balance)));

        List<String> loreList = new ArrayList<>();
        for(String loreLine : basicmeta.getLore()) {
            loreList.add(loreLine.replace("%player%", player).replace("%cardholder%", cardholder).replace("%cardbalance%", String.valueOf(balance)));
        }
        basicmeta.setLore(loreList);
        basic.setItemMeta(basicmeta);
        return basic;
    }


    public ItemStack getPlayerCreditCard(ItemStack basic, String cardholder, String cardNumber) {
        ItemMeta basicmeta = basic.getItemMeta();
        basicmeta.setDisplayName(basicmeta.getDisplayName().replace("%player%", cardholder));
        basicmeta.setDisplayName(basicmeta.getDisplayName().replace("%cardnumber%", cardNumber));

        List<String> loreList = new ArrayList<>();
        for(String loreLine : basicmeta.getLore()) {
            loreList.add(loreLine.replace("%player%", cardholder).replace("%cardnumber%", cardNumber));
        }
        basicmeta.setLore(loreList);
        basic.setItemMeta(basicmeta);
        return basic;
    }


    public ItemStack deserialize(ConfigurationSection section) {
        return PlayerUtils.getItemStack(Colors.colorParser(section.getString("name")), Colors.colorParser(section.getString("lore")), XMaterial.matchXMaterial(section.getString("material")).get().parseMaterial());
    }

}
