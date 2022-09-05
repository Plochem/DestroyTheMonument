package net.straxidus.dtm.game.countdowns;

import net.straxidus.dtm.game.Game;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Countdown extends BukkitRunnable {
    int time;
    Game game;

    public Countdown(int time, Game game) {
        this.time = time;
        this.game = game;
    }
}
