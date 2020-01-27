package engine;

import io.reactivex.Observable;
import io.reactivex.Single;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

class RemoteBallImpl implements RemoteBall {
    private Point lastLocation;

    private int xDirection = 1;
    private int yDirection = 0;

    private final int width;
    private final int height;

    private Point playerALocation = new Point(0, 0);
    private Point playerBLocation = new Point(0, 0);

    public RemoteBallImpl(int width, int height) {
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
        });
    }

    private void move() {
        Rectangle ballRect = new Rectangle(lastLocation.x, lastLocation.y,
                ballWidth, ballHeight);
        Rectangle playerARect = new Rectangle(playerALocation.x, playerALocation.y,
                RemotePlayerImpl.playerWidth, RemotePlayerImpl.playerHeight);
        Rectangle playerBRect = new Rectangle(playerBLocation.x, playerBLocation.y,
                RemotePlayerImpl.playerWidth, RemotePlayerImpl.playerHeight);

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
