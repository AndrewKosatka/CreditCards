package it.myke.CreditCards.command;

import it.myke.CreditCards.Perms;
import it.myke.CreditCards.Plugin;
import it.myke.CreditCards.command.subcommand.SubBanknotes;
import it.myke.CreditCards.disk.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class BanknoteCommand implements CommandExecutor {
    private final it.myke.CreditCards.Plugin plugin;

    public BanknoteCommand(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginCommand("banknotes").setExecutor(this);
        plugin.getServer().getPluginCommand("banknotes").setAliases(Arrays.asList("banknote", "withdraw"));
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (args.length >= 1) {
                switch (args[0]) {
                    case "get":
                        if(sender.hasPermission(Perms.BANKNOTE_GET)) {
                            new SubBanknotes(((Player) sender).getPlayer(), plugin);
                        } else sender.sendMessage(Lang.no_perms);
                        break;
                    case "help":
                        sender.sendMessage(Lang.banknotes_help_message);
                        break;
                    default: sender.sendMessage(Lang.banknotes_unknown_subcommand);
                }
            } else
                sender.sendMessage(Lang.banknotes_unknown_subcommand);

        } else sender.sendMessage(Lang.only_players);
        return false;
    }


}
