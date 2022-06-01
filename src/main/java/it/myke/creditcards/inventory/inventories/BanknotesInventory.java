package it.myke.creditcards.inventory.inventories;

import com.cryptomorin.xseries.XMaterial;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import it.myke.creditcards.Plugin;
import it.myke.creditcards.disk.Lang;
import it.myke.creditcards.object.Banknote;
import it.myke.creditcards.utils.Colors;

import static it.myke.creditcards.utils.CardUtils.firstCharUpper;
import static it.myke.creditcards.utils.CardUtils.fixLimit;

public class BanknotesInventory {

    /**
     * It creates a new InventoryGui object, adds a bunch of elements to it, and returns it
     *
     * @param plugin The plugin instance
     * @return An InventoryGui object.
     */
    public InventoryGui getBanknotesInventory(Plugin plugin) {
        String[] guiSetup = {
                "         ",
                " ggggggg ",
                " ggggggg ",
                " ggggggg ",
                "   fbn   "
        };
        InventoryGui inventoryGui = new InventoryGui(plugin, null, Colors.colorParser(plugin.getConfig().getString("inventories.banknotes-show.title")), guiSetup);
        inventoryGui.setFiller(XMaterial.GRAY_STAINED_GLASS_PANE.parseItem());

        GuiElementGroup group = new GuiElementGroup('g');

        for (Banknote banknote : plugin.getBanknoteUtils().getBanknotes()) {
            group.addElement((new StaticGuiElement('g', banknote.getItemstack(), Colors.colorParser(
                    Lang.banknote_gui_name_color + firstCharUpper(banknote.getConfigName())),
                         Lang.banknote_gui_lore.replace("%lore%", banknote.getLore()),
                         Lang.banknote_gui_value.replace("%value%", fixLimit(banknote.getValue())))));
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
