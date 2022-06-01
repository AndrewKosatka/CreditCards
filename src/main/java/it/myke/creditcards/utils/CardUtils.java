package it.myke.creditcards.utils;

import it.myke.creditcards.Plugin;
import it.myke.creditcards.disk.Lang;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.UUID;

public class CardUtils {
    private static transient final DecimalFormat MONEY_FORMAT = new DecimalFormat("###,###");



    /**
     * It gets the UUID of the card holder from the credit card number
     *
     * @param creditcardNumber The credit card number.
     * @param section The section of the config file you want to get the UUID from.
     * @return The UUID of the card holder.
     */
    public UUID getUUIDFromCreditCard(long creditcardNumber, ConfigurationSection section) {
        return UUID.fromString(Objects.requireNonNull(section.getString(creditcardNumber + ".card-holderUUID")));
    }

    /**
     * Get the pin number from the credit card number.
     *
     * @param creditcardNumber The credit card number
     * @param section The section of the config file you want to get the value from.
     * @return The pin number from the credit card.
     */
    public int getPinNumberFromCreditCard(long creditcardNumber, ConfigurationSection section) {
        return section.getInt(creditcardNumber + ".card-pin");
    }

    /**
     * It gets the card type from the UUID
     *
     * @param uuid The UUID of the player you want to get the card type from.
     * @param section The section of the config file you want to get the card type from.
     * @return The card type of the UUID.
     */
    public String getCardTypeFromUUID(UUID uuid, ConfigurationSection section) {
        return section.getString(uuid + ".card-type");
    }




    /**
     * It generates a random pin number.
     *
     * @return A random pin number
     */
    public static int getRandomPin() {
        StringBuilder pin = new StringBuilder();
        for(int i = 0; i <= 4; i++) {
            pin.append(getRandom());
        }
        return Integer.parseInt(pin.toString());


    }


    /**
     * It generates a random credit card number.
     *
     * @return A random card number
     */
    public static long getRandomCard() {
        StringBuilder cardNumber = new StringBuilder();

        for(int i = 0; i <= 9; i++) {
            cardNumber.append(getRandom());
        }
        return Long.parseLong(cardNumber.toString());
    }



    /**
     * It returns a random number between 1 and 9.
     *
     * @return A random number between 1 and 9
     */
    private static int getRandom() {
        return (int) Math.floor(Math.random()*(9-1+1)+1);
    }


    /**
     * It takes a string, makes the first character uppercase, and makes the rest of the characters lowercase
     *
     * @param name The name of the class to be created.
     * @return The first character of the string is being converted to uppercase and the rest of the string is being
     * converted to lowercase.
     */
    public static String firstCharUpper(String name) {
        return name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
    }


    /**
     * If the limit is -1, return "&aUnlimited", otherwise return the limit formatted as money
     *
     * @param limit The limit of the item.
     * @return The limit is being returned.
     */
    public static String fixLimit(double limit) {
        return limit == -1 ? Lang.unlimited : formatMoney(limit);
    }


    /**
     * Format a double as a dollar amount.
     *
     * @param money The money to be formatted.
     * @return A string that is the money value formatted as a dollar amount.
     */
    public static String formatMoney(double money) {
        return "$" + MONEY_FORMAT.format(money);
    }




    /**
     * If the player has a credit card, return the credit card number
     *
     * @param player The player you want to get the credit card number of.
     * @param plugin The plugin instance
     * @return The credit card number of the player.
     */
    public String getPlayerCreditCard(Player player, Plugin plugin) {
        if(plugin.getCreditcardConfig().isConfigurationSection("credit-cards." + player.getUniqueId())) {
            return plugin.getCreditcardConfig().getString("credit-cards." + player.getUniqueId() + ".card-number");
        }
        return "";
    }

    /**
     * This function returns the card type of the player
     *
     * @param player The player you want to get the card type of.
     * @param plugin The plugin instance
     * @return The card type of the player.
     */
    public String getPlayerCardType(Player player, Plugin plugin) {

        if(plugin.getCreditcardConfig().isConfigurationSection("credit-cards." + player.getUniqueId())) {
            return plugin.getCreditcardConfig().getString("credit-cards." + player.getUniqueId() + ".card-type");
        }
        return "";
    }



}
