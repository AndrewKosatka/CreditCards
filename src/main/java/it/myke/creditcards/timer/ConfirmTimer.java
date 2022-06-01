package it.myke.creditcards.timer;

import it.myke.creditcards.Plugin;
import it.myke.creditcards.disk.Lang;
import it.myke.creditcards.object.CardConfigStep;
import org.bukkit.scheduler.BukkitRunnable;

public class ConfirmTimer extends BukkitRunnable {
    private final String randomString;
    private final Plugin plugin;

    public ConfirmTimer(String randomString, Plugin plugin) {
        this.randomString = randomString;
        this.plugin = plugin;
    }


    /**
     * If the player has not finished the card creation process within the time limit, the card creation process will be
     * cancelled
     */
    @Override
    public void run() {
        if(plugin.getCardConfigStep().containsKey(randomString)) {
            CardConfigStep cardConfigStep = plugin.getCardConfigStep().get(randomString);
            plugin.getCardConfigStep().remove(randomString);
            plugin.getCardConfigPlayers().remove(cardConfigStep.getSender().getName());
            cardConfigStep.getSender().sendMessage(Lang.time_over_cardcreation_process.replace("%cardholder%", cardConfigStep.getSender().getName()));
            cardConfigStep.getBanker().sendMessage(Lang.time_over_cardcreation_process.replace("%cardholder%", cardConfigStep.getSender().getName()));
        }
    }
}
