package it.myke.creditcards.object;

import lombok.Getter;
import org.bukkit.entity.Player;

public class CardConfigStep {
    @Getter final private CardTypeParams cardTypeParams;
    @Getter final private Player banker, sender;
    @Getter final private String cardType;

    public CardConfigStep(CardTypeParams cardTypeParams, Player banker, Player sender, String cardType) {
        this.cardTypeParams = cardTypeParams;
        this.banker = banker;
        this.sender = sender;
        this.cardType = cardType;
    }



}
