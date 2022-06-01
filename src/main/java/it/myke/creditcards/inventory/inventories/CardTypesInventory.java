package it.myke.creditcards.inventory.inventories;

import com.cryptomorin.xseries.XMaterial;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import it.myke.creditcards.Plugin;
import it.myke.creditcards.disk.Lang;
import it.myke.creditcards.object.CardTypeParams;
import it.myke.creditcards.utils.Colors;

import static it.myke.creditcards.utils.CardUtils.firstCharUpper;
import static it.myke.creditcards.utils.CardUtils.fixLimit;

public class CardTypesInventory {





    /**
     * It creates an inventory GUI with a title, a background, and a list of items that represent the card types
     *
     * @param plugin The plugin instance
     * @return An InventoryGui object.
     */
    public InventoryGui getCardTypesInventoryGui(Plugin plugin) {
        String[] guiSetup = {
                "         ",
                " ggggggg ",
                " ggggggg ",
                " ggggggg ",
                "   fbn   "
        };
        InventoryGui inventoryGui = new InventoryGui(plugin, null, Colors.colorParser(plugin.getConfig().getString("inventories.cardtype-show.title")), guiSetup);
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
        inventoryGui.addElement(new GuiPageElement('f', XMaterial.REDSTONE.parseItem(), GuiPageElement.PageAction.PREVIOUS, Colors.colorParser(plugin.getConfig().getString("inventories.go-prev-page"))));
        inventoryGui.addElement(new GuiPageElement('n', XMaterial.ARROW.parseItem(), GuiPageElement.PageAction.NEXT, Colors.colorParser(plugin.getConfig().getString("inventories.go-next-page"))));
        inventoryGui.addElement(new StaticGuiElement('b', XMaterial.BARRIER.parseItem(), click -> {
            inventoryGui.close();
            return true;
        }, Colors.colorParser(plugin.getConfig().getString("inventories.close-item-name")), Colors.colorParser(plugin.getConfig().getString("inventories.close-item-lore"))));
        return inventoryGui;
    }




}
