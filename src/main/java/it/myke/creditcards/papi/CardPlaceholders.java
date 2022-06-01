package it.myke.creditcards.papi;

import it.myke.creditcards.Plugin;
import it.myke.creditcards.utils.CardUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CardPlaceholders extends PlaceholderExpansion {
    private final Plugin plugin;


    public CardPlaceholders(Plugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public @NotNull String getIdentifier() {
        return "creditcards";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Drago903";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }



    /**
     * It returns the value of the placeholder you requested
     *
     * @param player The player who is requesting the placeholder.
     * @param params The parameter that is being requested.
     * @return The placeholder value.
     */
    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player != null) {
            if (params.startsWith("cardtype_")) {
                String req = params.replace("cardtype_", "");
                switch (req) {
                    case "maxwithdraw":
                        return String.valueOf(plugin.getCardTypesLoader().getCardTypes().get(new CardUtils().getPlayerCardType(player, plugin)).getMaxWithdraw());
                    case "maxpayments":
                        return String.valueOf(plugin.getCardTypesLoader().getCardTypes().get(new CardUtils().getPlayerCardType(player, plugin)).getMaxPayments());
                    case "maxdeposit":
                        return String.valueOf(plugin.getCardTypesLoader().getCardTypes().get(new CardUtils().getPlayerCardType(player, plugin)).getMaxDeposit());
                    case "maxcredit":
                        return String.valueOf(plugin.getCardTypesLoader().getCardTypes().get(new CardUtils().getPlayerCardType(player, plugin)).getMaxCredit());
                }
            } else {
                switch (params) {
                    case "cardnumber":
                        return new CardUtils().getPlayerCreditCard(player, plugin);
                    case "cardtype":
                        return new CardUtils().getPlayerCardType(player, plugin);
                }
            }
        }
        return "";
    }
}
