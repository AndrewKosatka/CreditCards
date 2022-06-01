package it.myke.creditcards.task;

import it.myke.creditcards.Plugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class CardSaver {


    // Saving the credit card information to the creditcards.yml file.
    public CardSaver(Plugin plugin, FileConfiguration config, String configname, UUID cardHolderUUID, int pin, long cardNumber, String cardtype) {
        String cardSection = "credit-cards.";

        if(config.getString(cardSection + cardHolderUUID) != null) {
            String creditcard = config.getString(cardSection + cardHolderUUID + ".card-number");
            config.set(cardSection + creditcard, null);
            config.set(cardSection + cardHolderUUID, null);
        }

        config.set(cardSection + cardHolderUUID + ".card-number", cardNumber);
        config.set(cardSection + cardHolderUUID + ".card-pin", pin);
        config.set(cardSection + cardHolderUUID + ".card-type", cardtype);
        //REVERSE
        config.set(cardSection + cardNumber + ".card-holderUUID", cardHolderUUID.toString());
        config.set(cardSection + cardNumber + ".card-pin", pin);
        config.set(cardSection + cardNumber + ".card-type", cardtype);
        try {
            File configFile = new File(plugin.getDataFolder(), configname);
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
