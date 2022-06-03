package it.myke.CreditCards.command;

import it.myke.CreditCards.Perms;
import it.myke.CreditCards.Plugin;
import it.myke.CreditCards.command.subcommand.SubCardCreator;
import it.myke.CreditCards.command.subcommand.SubCardInfo;
import it.myke.CreditCards.command.subcommand.SubShowCardTypes;
import it.myke.CreditCards.disk.Lang;
import it.myke.CreditCards.object.CardConfigStep;
import it.myke.CreditCards.task.CardConfigStepTask;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class CreditCardCommand implements @Nullable CommandExecutor {
    private final Plugin plugin;

    public CreditCardCommand(Plugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("creditcard").setExecutor(this);
        plugin.getCommand("creditcard").setAliases(Arrays.asList("creditcards", "cdc", "cards"));
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {

            if (args.length >= 1) {
                switch (args[0]) {
                    case "create":
                        if (args.length == 2) {
                            if (sender.hasPermission(Perms.CREDITCARD_CREATE)) {
                                new SubCardCreator(plugin, (Player) sender, args[1]);
                            } else sender.sendMessage(Lang.no_perms);
                        } else sender.sendMessage(Lang.creditcard_create_command_usage);
                        break;
                    case "confirm":
                        if (args.length == 2) {
                            CardConfigStep configStep = plugin.getCardConfigStep().getOrDefault(args[1], null);
                            new CardConfigStepTask(configStep, args[1], sender, plugin);
                        }
                        break;
                    case "types":
                        new SubShowCardTypes(plugin, (Player) sender);
                        break;
                    case "info":

                        String playerArg = "";
                        if(args.length == 2) {
                            playerArg = args[1];
                        } else playerArg = sender.getName();

                        new SubCardInfo(plugin, (Player) sender, playerArg);
                        break;
                    case "help":
                        sender.sendMessage(Lang.creditcard_help_message.replace("%subcommands%", "(create, types, info)"));
                        break;
                    default: sender.sendMessage(Lang.creditcard_unknown_subcommand);
                }
            } else sender.sendMessage(Lang.creditcard_unknown_subcommand);

        } else sender.sendMessage(Lang.only_players);
        return false;
    }

}
