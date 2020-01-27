package engine;

import io.reactivex.Observable;
import io.reactivex.Single;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public interface RemoteBall {

    public static final int ballWidth = 10;
    public static final int ballHeight = 10;

    public void setPlayersLocation(Point playerALocation, Point playerBLocation);

    public Observable<Point> getLocation();

    public static RemoteBall create(int width, int height) {
        return new RemoteBallImpl(width, height);
    }
}
