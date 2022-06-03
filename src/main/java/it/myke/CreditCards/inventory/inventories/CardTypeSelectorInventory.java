package it.myke.CreditCards.inventory.inventories;

import com.cryptomorin.xseries.XMaterial;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import it.myke.CreditCards.Plugin;
import it.myke.CreditCards.disk.Lang;
import it.myke.CreditCards.object.CardTypeParams;
import it.myke.CreditCards.utils.Colors;

import static it.myke.CreditCards.utils.CardUtils.firstCharUpper;
import static it.myke.CreditCards.utils.CardUtils.fixLimit;

public class CardTypeSelectorInventory {

    /**
     * It creates a new InventoryGui object, adds a GuiElementGroup to it, adds a GuiPageElement to it, adds a
     * StaticGuiElement to it, and returns the InventoryGui object
     *
     * @param plugin The plugin instance
     * @return An InventoryGui object.
     */
    public InventoryGui getCardTypeSelector(Plugin plugin) {
        String[] guiSetup = {
                "         ",
                "agggggggb",
                "    c    "
        };
        InventoryGui inventoryGui = new InventoryGui(plugin, null, Colors.colorParser(plugin.getConfig().getString("inventories.cardtype-selector.title")), guiSetup);
        inventoryGui.setFiller(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem());

        GuiElementGroup group = new GuiElementGroup('g');

        for (String cardTypes : plugin.getCardTypesLoader().getCardTypes().keySet()) {
            CardTypeParams cardTypeParams = plugin.getCardTypesLoader().getCardTypes().get(cardTypes);
            group.addElement((new StaticGuiElement('g', XMaterial.matchXMaterial(cardTypeParams.getGuiMaterial()).parseItem(), Colors.colorParser(
                    Lang.cardtype_name_color + firstCharUpper(cardTypes)),
                    Lang.credit_max.replace("%value%", fixLimit(cardTypeParams.getMaxCredit())),
                    Lang.payments_max.replace("%value%", fixLimit(cardTypeParams.getMaxPayments())),
                    Lang.withdraw_max.replace("%value%", fixLimit(cardTypeParams.getMaxWithdraw())),
                    Lang.deposit_max.replace("%value%", fixLimit(cardTypeParams.getMaxCredit())),
                    Lang.cardtype_cost.replace("%value%", fixLimit(cardTypeParams.getTypeCost())))));
        }

        inventoryGui.addElement(group);
        inventoryGui.addElement(new GuiPageElement('a', XMaterial.REDSTONE.parseItem(), GuiPageElement.PageAction.PREVIOUS, Colors.colorParser(plugin.getConfig().getString("inventories.go-prev-page"))));
        inventoryGui.addElement(new GuiPageElement('b', XMaterial.ARROW.parseItem(), GuiPageElement.PageAction.NEXT, Colors.colorParser(plugin.getConfig().getString("inventories.go-next-page"))));
        inventoryGui.addElement(new StaticGuiElement('c', XMaterial.BARRIER.parseItem(), click -> {
            inventoryGui.close();
            return true;
        }, Colors.colorParser(plugin.getConfig().getString("inventories.close-item-name")), Colors.colorParser(plugin.getConfig().getString("inventories.close-item-lore"))));
        return inventoryGui;
    }



}
