package net.straxidus.dtm.commands;

import net.straxidus.dtm.DTM;
import net.straxidus.dtm.database.PlayerData;
import net.straxidus.dtm.shop.Currency;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class TokenCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandsender, Command command, String s, String[] args) { // token add|subtract amt [name]
        if (command.getName().equalsIgnoreCase("token")) {
            if (args.length == 3) {
                int amt = validAmount(args[1]);
                if (amt == -1) {
                    commandsender.sendMessage("invalid token amount");
                    return true;
                }
                PlayerData pdata = null;
                OfflinePlayer targetPlayer = null;
                try {
                    UUID id = UUID.fromString(args[2]);
                    pdata = DTM.dtm.getPlayerData(id);
                    targetPlayer = Bukkit.getOfflinePlayer(id);
                } catch (IllegalArgumentException e) {
                    targetPlayer = Bukkit.getOfflinePlayer(args[2]);
                    pdata = DTM.dtm.getPlayerData(targetPlayer.getUniqueId());
                }
                if (pdata == null ) {
                    commandsender.sendMessage("This player has never joined the server before.");
                    return true;
                }
                if (args[0].equalsIgnoreCase("add")) {
                    pdata.addCurrencyAmount(Currency.TOKENS, amt);
                } else if (args[0].equalsIgnoreCase("subtract")) {
                    pdata.subtractCurrencyAmount(Currency.TOKENS, amt);
                } else {
                    commandsender.sendMessage("bruh");
                    return true;
                }
                if (targetPlayer.isOnline()) DTM.dtm.getLobby().showScoreboard(targetPlayer.getPlayer());
            } else {
                commandsender.sendMessage("bruh");
            }

        }
        return false;
    }

    private int validAmount(String amtStr) {
        try {
            int amt = Integer.parseInt(amtStr);
            if (amt > 0)
                return amt;
            return -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
