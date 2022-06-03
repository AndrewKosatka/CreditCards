package it.myke.CreditCards.object;

import it.myke.CreditCards.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;


public class ATMLocation {



    /**
     * It saves the location of an ATM to the config file
     *
     * @param atmLocation The location of the ATM
     * @param key The key of the ATM in the config.
     * @param plugin The plugin instance.
     */
    public static void serializeATMLocation(Location atmLocation, String key, Plugin plugin) {
        plugin.getAtmConfig().set("atms." + key + ".position", atmLocation.getWorld().getName() + "/" + atmLocation.getX() + "/" + atmLocation.getY() + "/" + atmLocation.getZ());
        try {
            plugin.getAtmConfig().save(new File(plugin.getDataFolder(), "atms.yml"));
        } catch (IOException e) {
            plugin.fatalError("Could not save the 'ATM' config!", e);
        }
    }

    /**
     * It takes a key and a plugin, and returns a location
     *
     * @param key The key of the ATM in the config.
     * @param plugin The plugin instance
     * @return A location object
     */
    public static Location deserializeATMLocation(String key, Plugin plugin) {
        String[] positionString = plugin.getAtmConfig().getString("atms." + key + ".position").split("/");
        return new Location(Bukkit.getWorld(positionString[0]),  Double.parseDouble(positionString[1]), Double.parseDouble(positionString[2]), Double.parseDouble(positionString[3]));

    }








}
