package engine;

import io.reactivex.Observable;
import io.reactivex.Single;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

class RemotePlayerImpl implements RemotePlayer {
    private Random random = new Random();

    private final int width;
    private final int height;

    private Point lastLocation;

    private Point ballLocation = new Point(0, 0);

    public RemotePlayerImpl(int width, int height) {
        this.width = width;
        this.height = height;

        lastLocation = new Point(width, 0);
    }

    public void setBallLocation(Point ballLocation) {
        this.ballLocation = ballLocation;
    }

    public Observable<Point> getLocation() {
        return Observable.interval(0, 6, TimeUnit.MILLISECONDS).map(i -> {
            final int nextY = Math.min(0, (ballLocation.y + random.nextInt(200) - 100) % height);
            lastLocation.y += Integer.compare(nextY, ballLocation.y);
            return lastLocation;
        });
    }
}
