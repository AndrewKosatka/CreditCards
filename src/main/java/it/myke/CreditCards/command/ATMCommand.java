package it.myke.CreditCards.command;

import it.myke.CreditCards.Plugin;
import it.myke.CreditCards.command.subcommand.ATMCreator;
import it.myke.CreditCards.disk.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ATMCommand implements CommandExecutor {

    public ATMCommand(Plugin plugin) {
       plugin.getCommand("atm").setExecutor(this);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player) {
            if (args.length >= 1) {
                if (args.length == 1) {
                    if ("get".equals(args[0])) {
                        new ATMCreator(sender);
                    } else {
                        sender.sendMessage(Lang.atm_help_message);
                    }
                }
            } else sender.sendMessage(Lang.atm_help_message);
        } else sender.sendMessage(Lang.only_players);

        return false;
    }
}
