package net.straxidus.dtm.commands;

import net.straxidus.dtm.DTM;
import net.straxidus.dtm.game.Game;
import net.straxidus.dtm.game.GameState;
import net.straxidus.dtm.game.Team;
import net.straxidus.dtm.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandsender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("join")) {
            if (!(commandsender instanceof Player)) {
                commandsender.sendMessage(Messages.playerToRunCmd);
                return false;
            }
            Game game = DTM.dtm.getGameManager().getGamePlayerIn(((Player) commandsender).getUniqueId());
            if (game == null) return false;
            if (game.getState() == GameState.ENDING || game.getState() == GameState.RESETTING) {
                ((Player) commandsender).sendMessage(Messages.cantJoinTeam);
            } else if (game.getNumTeamPlayers() + 1 >= game.getMap().getMaxPlayers()) {
                ((Player) commandsender).sendMessage(Messages.teamsAreFull);
            } else {
                Team joinedTeam = game.moveSpectatorToTeam(((Player) commandsender));
                if (joinedTeam != null) {
                    commandsender.sendMessage(String.format(Messages.joinedTeam, game.getMap().getTeamData(joinedTeam).getTeamChatColor() + game.getMap().getTeamData(joinedTeam).getName()));
                } else {
                    commandsender.sendMessage(Messages.alreadyOnTeam);
                }
            }
        }
        return false;
    }
}
