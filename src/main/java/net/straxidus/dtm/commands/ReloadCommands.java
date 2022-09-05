package net.straxidus.dtm.commands;

import net.straxidus.dtm.DTM;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("dtmreload")) {
            //TODO perms
            DTM.dtm.getMessages().reload();
            DTM.dtm.getMapManager().reload();
            DTM.dtm.getLobby().reload();
            System.out.println("reload everything");
        }
        return false;
    }
}
