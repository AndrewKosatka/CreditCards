package it.myke.CreditCards.inventory;

import de.themoep.inventorygui.InventoryGui;
import it.myke.CreditCards.Plugin;
import it.myke.CreditCards.inventory.inventories.ATMInventory;
import it.myke.CreditCards.inventory.inventories.BanknotesInventory;
import it.myke.CreditCards.inventory.inventories.CardTypeSelectorInventory;
import it.myke.CreditCards.inventory.inventories.CardTypesInventory;
import org.bukkit.inventory.Inventory;

public class InventoryManager {
    public Inventory atmInventory;
    public InventoryGui cardTypesGUI, cardTypeSelectorInventory, banknotesInventory;

    public InventoryManager(Plugin plugin) {
        this.atmInventory = new ATMInventory().getATMInventory(plugin);
        this.cardTypesGUI = new CardTypesInventory().getCardTypesInventoryGui(plugin);
        this.cardTypeSelectorInventory = new CardTypeSelectorInventory().getCardTypeSelector(plugin);
        this.banknotesInventory = new BanknotesInventory().getBanknotesInventory(plugin);
    }


}
