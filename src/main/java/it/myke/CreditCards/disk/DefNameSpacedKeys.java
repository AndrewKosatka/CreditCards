package it.myke.CreditCards.disk;

import it.myke.CreditCards.Plugin;
import org.bukkit.NamespacedKey;

public class DefNameSpacedKeys {

    public static NamespacedKey nsCardNumber, nsATM, nsBanknoteValue;

    public DefNameSpacedKeys(Plugin plugin) {
        nsCardNumber = new NamespacedKey(plugin, "card-number");
        nsATM = new NamespacedKey(plugin, "atm");
        nsBanknoteValue = new NamespacedKey(plugin, "banknote-value");
    }



}
