package it.myke.CreditCards.task;

import com.cryptomorin.xseries.XMaterial;
import it.myke.CreditCards.disk.ItemStore;
import it.myke.CreditCards.object.CardTypeParams;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Optional;

public class CardTypesLoader {
    @Getter
    private final HashMap<String, CardTypeParams> cardTypes;
    private final ConfigurationSection cardTypeSection;

    public CardTypesLoader(ConfigurationSection cardTypeSection) {
        cardTypes = new HashMap<>();
        this.cardTypeSection = cardTypeSection;
        for(String cardTypeKeys : cardTypeSection.getKeys(false)) {
            cardTypes.put(cardTypeKeys, new CardTypeParams(getMaxWithdraw(cardTypeKeys), getMaxCredit(cardTypeKeys), getCardCost(cardTypeKeys), getMaxPayments(cardTypeKeys), getMaxDeposit(cardTypeKeys), getGuiMaterial(cardTypeKeys)));
        }
    }


    private int getMaxCredit(String cardTypeKey) {
        return cardTypeSection.getInt(cardTypeKey + ".max-credit");
    }
    private int getMaxWithdraw(String cardTypeKey) {
        return cardTypeSection.getInt(cardTypeKey + ".max-withdraw");
    }
    private int getMaxPayments(String cardTypeKey) {
        return cardTypeSection.getInt(cardTypeKey + ".max-payment");
    }
    private int getCardCost(String cardTypeKey) {
        return cardTypeSection.getInt(cardTypeKey + ".cost");
    }
    private Material getGuiMaterial(String cardTypeKey) {
        String guiMaterialString = cardTypeSection.getString(cardTypeKey + ".gui-material");
        Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(guiMaterialString);
        return xMaterial.isPresent() && !guiMaterialString.equalsIgnoreCase("default") ? xMaterial.get().parseMaterial() : ItemStore.creditcard.getType();
    }
    private int getMaxDeposit(String cardTypeKey) {
        return cardTypeSection.getInt(cardTypeKey + ".max-deposit");
    }


}
