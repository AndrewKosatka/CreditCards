package it.myke.creditcards.timer;

import it.myke.creditcards.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class PaymentLimitTimer extends BukkitRunnable {
    private final Plugin plugin;


    public PaymentLimitTimer(Plugin plugin) {
        this.plugin = plugin;
    }



    /**
     * It clears the withdrawLimits, depositLimits, and paymentLimits maps
     * after the ticks specified in the config.
     */

    @Override
    public void run() {
        plugin.withdrawLimits = new HashMap<>();
        plugin.depositLimits = new HashMap<>();
        plugin.paymentLimits = new HashMap<>();
    }
}
