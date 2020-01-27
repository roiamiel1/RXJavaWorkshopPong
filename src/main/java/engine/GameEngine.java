package engine;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

import java.awt.*;

public interface GameEngine {

    /**
     * Note! Use in part A of the exercise (not in B).
     * call only on the render thread and not more then 30 times per second!
     */
    public void drawSinglePlayer(Point playerLocation);

    /**
     * Note! Use in part B of the exercise (not in A).
     * Note! call only on the render thread and not more then 30 times per second!
     */
    public void drawFrame(Point yourLocation, Point gretaLocation, Point ballLocation);

    public Scheduler getRenderThread();

    public Observable<Keys> getClicks();

    public static GameEngine create(int width, int height) {
        return new GameEngineImpl(width, height);
    }
}
