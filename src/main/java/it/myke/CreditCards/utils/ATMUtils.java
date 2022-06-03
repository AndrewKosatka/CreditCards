package it.myke.CreditCards.utils;

import it.myke.CreditCards.Plugin;
import it.myke.CreditCards.disk.Lang;
import it.myke.CreditCards.object.ATMLocation;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class ATMUtils {
    public HashMap<String, Location> atms;

    public ATMUtils() {
        this.atms = new HashMap<>();
    }

    /**
     * It loads all the ATMs from the config file into a HashMap.
     *
     * @param plugin The plugin instance
     */
    public void loadATMsLocation(Plugin plugin) {
        atms = new HashMap<>();
        if(plugin.getAtmConfig().getConfigurationSection("atms") != null) {
            for (String key : plugin.getAtmConfig().getConfigurationSection("atms").getKeys(false)) {
                atms.put(key, ATMLocation.deserializeATMLocation(key, plugin));
            }
        }
    }

    /**
     * This function returns the number of ATMs in the bank.
     *
     * @return The size of the atms arraylist.
     */
    public int getATMSize() {
        return atms.size();
    }

    /**
     * If the block is an ATM, return true.
     *
     * @param block The block you want to check if it's an ATM.
     * @return A boolean value.
     */
    public boolean isAtm(Block block) {
        return atms.containsValue(block.getLocation());
    }

    /**
     * It removes an ATM from the config file
     *
     * @param block The block that you want to remove from the ATM list.
     * @param plugin The plugin instance
     */
    public void removeATM(Block block, Plugin plugin) {
        for (String key : atms.keySet()) {
            atms.get(key);
            if (block.getWorld().getName().equals(atms.get(key).getWorld().getName()))
                if (block.getLocation().getX() == atms.get(key).getX())
                    if (block.getLocation().getY() == atms.get(key).getY())
                        if (block.getLocation().getZ() == atms.get(key).getZ()) {


                            plugin.getAtmConfig().set("atms." + key, null);
                            try {
                                plugin.getAtmConfig().save(new File(plugin.getDataFolder(), "atms.yml"));
                            } catch (IOException e) {
                                plugin.fatalError("Could not save the ATM config file!", e);
                            }
                        }
        }
    }



    /**
     * It creates an AnvilGUI with the text "PIN" and the title "Insert PIN" and when the player clicks the "Done" button,
     * it checks if the input is equal to the PIN and if it is, it opens the ATM inventory.
     *
     * @param plugin The plugin instance
     * @param player The player who is using the ATM
     * @param pin The pin of the card holder
     * @param cardHolderUUID The UUID of the player who owns the card.
     */
    public void pinCheck(Plugin plugin, Player player, String pin, UUID cardHolderUUID) {
        AnvilGUI.Builder builder = new AnvilGUI.Builder();
        builder.plugin(plugin);
        builder.text("PIN");
        builder.title(Lang.insert_pin);
        builder.onComplete(((p, s) -> {
            if (s.equals(pin)) {
                openATMInventory(plugin, p, cardHolderUUID);
            } else p.sendMessage(Lang.pin_not_correct);
            return AnvilGUI.Response.close();
        }));
        builder.open(player);
    }

    /**
     * It opens the ATM inventory for the player who requested it, and refreshes the balance of the ATM holder
     *
     * @param plugin The plugin instance
     * @param requesting The player who is opening the inventory.
     * @param holder The UUID of the player who's inventory you want to open.
     */
    public void openATMInventory(Plugin plugin, Player requesting, UUID holder) {
        requesting.openInventory(plugin.getInventoryManager().atmInventory);
        refreshBalance(plugin, requesting.getOpenInventory().getTopInventory(), requesting, Bukkit.getPlayer(holder));
    }



    /**
     * It sets the balance item in the inventory to the current balance of the cardholder
     *
     * @param plugin The plugin instance
     * @param inventory The inventory you want to set the item in.
     * @param player The player who is viewing the inventory.
     * @param cardholder The player who's balance is being displayed.
     */
    public void refreshBalance(Plugin plugin, Inventory inventory, Player player, Player cardholder) {
        inventory.setItem(22, plugin.getItemStore().getBalanceItem(player.getName(), cardholder.getName(), plugin.getEconomy().getBalance(cardholder)));
    }








}
