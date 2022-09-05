package net.straxidus.dtm.game.listeners;

import net.md_5.bungee.api.ChatColor;
import net.straxidus.dtm.events.MonumentBreakEvent;
import net.straxidus.dtm.game.Team;
import net.straxidus.dtm.map.TeamMapData;
import net.straxidus.dtm.Messages;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MonumentBreakListener implements Listener {
    @EventHandler
    public void onMonBreak(MonumentBreakEvent e) {
        Team pTeam = e.getGame().getPlayerTeam(e.getPlayer());
        TeamMapData pTeamData = e.getGame().getMap().getTeamData(pTeam);
        ChatColor pTeamColor = pTeamData.getTeamChatColor();
        if (e.getMonument().getTeam() == pTeam) {
            e.getPlayer().sendMessage(Messages.breakOwnMon);
            e.setCancelled(true);
        } else {
            e.getMonument().setBroken(true);
            e.getGame().updateScoreboard();
            e.getGame().sendMessage(String.format(Messages.monDestroyed, pTeamColor + e.getPlayer().getName(), e.getGame().getMap().getTeamData(pTeam.getOtherTeam()).getTeamChatColor() + e.getMonument().getName()));
            if (e.getGame().allTeamMonumentsBroken(pTeam.getOtherTeam())) {
                e.getGame().sendMessage(String.format(Messages.teamWon, pTeamColor + pTeamData.getName()));
                e.getGame().endMatch();
                e.getPlayer().setGameMode(GameMode.SURVIVAL); // apparently setting to adventure mode on block break cancels it w/o isCancelled being true
            }
        }
    }

}
