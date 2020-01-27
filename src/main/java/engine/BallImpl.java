package engine;

import io.reactivex.Observable;

import java.awt.*;
import java.util.concurrent.TimeUnit;

class BallImpl implements Ball {
    private Point lastLocation;

    private int xDirection = 1;
    private int yDirection = -1;

    private final int width;
    private final int height;

    private Point playerALocation = new Point(0, 0);
    private Point playerBLocation = new Point(0, 0);

    public BallImpl(int width, int height) {
        this.width = width;
        this.height = height;
        this.lastLocation = new Point(width / 2, height / 2);
    }

    @Override
    public void setPlayersLocation(Point playerALocation, Point playerBLocation) {
        this.playerALocation = playerALocation;
        this.playerBLocation = playerBLocation;
    }

    public Observable<Point> getLocation() {
        return Observable.interval(0, 6, TimeUnit.MILLISECONDS).map(i -> {
            move();
            return lastLocation;
        }).distinctUntilChanged();
    }

    private void move() {
        Rectangle ballRect = new Rectangle(lastLocation.x, lastLocation.y,
                ballWidth, ballHeight);
        Rectangle playerARect = new Rectangle(playerALocation.x, playerALocation.y,
                GretaImpl.playerWidth, GretaImpl.playerHeight);
        Rectangle playerBRect = new Rectangle(playerBLocation.x, playerBLocation.y,
                GretaImpl.playerWidth, GretaImpl.playerHeight);

        if (ballRect.intersects(playerARect)) {
            xDirection = 1;
        } if(ballRect.intersects(playerBRect)) {
            xDirection = -1;
        }

        lastLocation.x += xDirection;
        lastLocation.y += yDirection;

        if (lastLocation.x <= 0) {
            xDirection = 1;
        }

        if (lastLocation.x >= width - ballWidth) {
            xDirection = -1;
        }

        if (lastLocation.y <= 0) {
            yDirection = 1;
        }

        if (lastLocation.y >= height - ballHeight) {
            yDirection = -1;
        }
    }
}
