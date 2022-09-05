package net.straxidus.dtm.game.countdowns;

import net.straxidus.dtm.game.Game;
import net.straxidus.dtm.Messages;
import org.bukkit.Sound;

public class EndCountdown extends Countdown {

    public EndCountdown(int time, Game game) {
        super(time, game);
    }

    @Override
    public void run() {
        if (time != 1){
            if(time == 0){
                game.removeCountdown();
                game.resetMap();
            } else{
                game.sendMessage(String.format(Messages.playerToLobbyIn, time));
                game.playSound(Sound.BLOCK_NOTE_BLOCK_HAT);
            }
        } else{
            game.sendMessage(Messages.playerToLobbyInOne);
            game.playSound(Sound.BLOCK_NOTE_BLOCK_HAT);
        }
        time--;
    }
}
