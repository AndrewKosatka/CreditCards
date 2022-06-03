package it.myke.CreditCards.object;

import lombok.Getter;

public class CreditCard {

    @Getter
    private final int cardNumber, cardPin;
    @Getter
    private final String cardHolderUUID;
    @Getter
    private final String cardType;


    public CreditCard(int cardNumber, int cardPin, String cardHolderUUID, String cardType) {
        this.cardNumber = cardNumber;
        this.cardPin = cardPin;
        this.cardHolderUUID = cardHolderUUID;
        this.cardType = cardType;
    }














}
