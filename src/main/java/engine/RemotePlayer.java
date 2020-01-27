package engine;

import io.reactivex.Observable;
import io.reactivex.Single;

import java.awt.*;

public interface RemotePlayer {

    public static final int playerWidth = 10;
    public static final int playerHeight = 30;

    public void setBallLocation(Point ballLocation);

    public Observable<Point> getLocation();

    public static RemotePlayer create(int width, int height) {
        return new RemotePlayerImpl(width, height);
    }
}
