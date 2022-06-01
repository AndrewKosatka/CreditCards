package it.myke.creditcards.inventory;

import de.themoep.inventorygui.InventoryGui;
import it.myke.creditcards.Plugin;
import it.myke.creditcards.inventory.inventories.ATMInventory;
import it.myke.creditcards.inventory.inventories.BanknotesInventory;
import it.myke.creditcards.inventory.inventories.CardTypeSelectorInventory;
import it.myke.creditcards.inventory.inventories.CardTypesInventory;
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
