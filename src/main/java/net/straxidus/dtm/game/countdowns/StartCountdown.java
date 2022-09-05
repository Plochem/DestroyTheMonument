package net.straxidus.dtm.game.countdowns;

import net.straxidus.dtm.game.Game;
import net.straxidus.dtm.Messages;
import org.bukkit.Sound;

public class StartCountdown extends Countdown {

    public StartCountdown(int time, Game game) {
        super(time, game);
    }

    @Override
    public void run() {
        if (time != 1){
            if(time == 0){
                game.removeCountdown();
                game.beginMatch();
            } else{
                game.sendMessage(String.format(Messages.matchStartsIn, time));
                game.playSound(Sound.BLOCK_NOTE_BLOCK_HAT);
            }
        } else{
            game.sendMessage(Messages.matchStartsInOne);
            game.playSound(Sound.BLOCK_NOTE_BLOCK_HAT);
        }
        time--;
    }
}
