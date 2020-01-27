package engine;

import io.reactivex.Completable;
import io.reactivex.Observable;

import java.awt.*;

public interface Ball {

    public static final int ballWidth = 10;
    public static final int ballHeight = 10;

    public void setPlayersLocation(Point yourLocation, Point gretaLocation);

    public Observable<Point> getLocation();

    public static Ball create(int width, int height) {
        return new BallImpl(width, height);
    }
}
