package engine;

import io.reactivex.Completable;
import io.reactivex.Observable;

import java.awt.*;

public interface Greta {

    public static final int playerWidth = 10;
    public static final int playerHeight = 30;

    public void setBallLocation(Point ballLocation);

    public Observable<Point> getLocation();

    public static Greta create(int width, int height) {
        return new GretaImpl(width, height);
    }
}
