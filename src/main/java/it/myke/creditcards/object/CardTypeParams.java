package it.myke.creditcards.object;

import lombok.Getter;
import org.bukkit.Material;

public class CardTypeParams {

    @Getter
    private final int maxWithdraw, maxCredit, typeCost, maxPayments, maxDeposit;
    @Getter
    private final Material guiMaterial;

    public CardTypeParams(int maxWithdraw, int maxCredit, int typeCost, int maxPayments, int maxDeposit, Material guiMaterial) {
        this.maxCredit = maxCredit;
        this.maxWithdraw = maxWithdraw;
        this.typeCost = typeCost;
        this.maxPayments = maxPayments;
        this.maxDeposit = maxDeposit;
        this.guiMaterial = guiMaterial;
    }





}
