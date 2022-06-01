package it.myke.creditcards.utils;

import net.md_5.bungee.api.ChatColor;

public class Colors {

    /**
     * It takes a string, and replaces all instances of '&' with the Minecraft color code character
     *
     * @param msg The message you want to parse.
     * @return The message with the color codes translated.
     */
    public static String colorParser(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }


}
