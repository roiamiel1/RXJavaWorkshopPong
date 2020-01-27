package engine;

import io.reactivex.Observable;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

class GretaImpl implements Greta {
    private Random random = new Random();

    private final int width;
    private final int height;

    private Point lastLocation;

    private long lastChangeYDirection = -1;
    private int yDirection = 0;

    private Point ballLocation = new Point(0, 0);

    public GretaImpl(int width, int height) {
        this.width = width;
        this.height = height;

        lastLocation = new Point(width, 0);
    }

    public void setBallLocation(Point ballLocation) {
        this.ballLocation = ballLocation;
    }

    public Observable<Point> getLocation() {
        return Observable.interval(0, 6, TimeUnit.MILLISECONDS).map(i -> {
            if (lastChangeYDirection == -1 || (System.currentTimeMillis() - lastChangeYDirection) > 250) {
                lastChangeYDirection = System.currentTimeMillis();
                final int nextY = (ballLocation.y + random.nextInt(10) - 5) % height;
                yDirection = Integer.compare(nextY, lastLocation.y);
            }
            lastLocation.y += yDirection;
            return lastLocation;
        }).distinctUntilChanged();
    }
}
