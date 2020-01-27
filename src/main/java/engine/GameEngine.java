package engine;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

import java.awt.*;

public interface GameEngine {

    /**
     * Draw a frame to the screen, don't call more then 20 times in a sec,
     * and call only on the render scheduler.
     */
    public void drawFrame(Point padA, Point padB, Point ball);

    public Scheduler getRenderScheduler();

    public Observable<Keys> getKeys();

    public static GameEngine create(int width, int height) {
        return new GameEngineImpl(width, height);
    }
}
