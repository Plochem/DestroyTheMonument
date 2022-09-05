package net.straxidus.dtm.commands;

import net.straxidus.dtm.DTM;
import net.straxidus.dtm.game.Game;
import net.straxidus.dtm.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("leave")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Messages.playerToRunCmd);
                return false;
            }
            Player p = (Player) sender;
            Game game = DTM.dtm.getGameManager().getGamePlayerIn(p.getUniqueId());
            if (game == null) {
                p.sendMessage(Messages.alreadyNotInGame);
                return false;
            }
            if (game.getSpectators().contains(p.getUniqueId())) {
                DTM.dtm.getGameManager().removePlayerFromGame(p);
                p.sendMessage(Messages.leavingGame);
            } else { // in team
                game.moveFromTeamToSpectator(p);
                p.sendMessage(Messages.nowSpectator);
            }
        }
        return false;
    }
}
