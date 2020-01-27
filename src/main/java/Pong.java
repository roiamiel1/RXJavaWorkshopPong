import engine.Ball;
import engine.GameEngine;
import engine.Greta;
import engine.Keys;
import io.reactivex.Observable;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Pong  {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 400;

    private final Ball ball = Ball.create(WIDTH, HEIGHT);
    private final Greta greta = Greta.create(WIDTH, HEIGHT);
    private final GameEngine gameEngine = GameEngine.create(WIDTH, HEIGHT);

    private Point playerLocation = new Point(0, 0);

    public void init() {
        gameEngine.getClicks()
                .sample(6, TimeUnit.MILLISECONDS)
                .map(event -> {
                    int yDirection = 0;
                    if (event == Keys.UP) {
                        yDirection = -1;
                    } else if (event == Keys.DOWN) {
                        yDirection = 1;
                    }

                    playerLocation.y += yDirection;

                    if (playerLocation.y < 0) {
                        playerLocation.y = 0;
                    } else if (playerLocation.y > HEIGHT - Greta.playerHeight) {
                        playerLocation.y = HEIGHT - Greta.playerHeight;
                    }

                    return playerLocation;
                })
                .throttleFirst(35, TimeUnit.MILLISECONDS)
                .observeOn(gameEngine.getRenderThread())
                .subscribe(gameEngine::drawSinglePlayer);
    }

    public static void main(String[] args) {
        new Pong().init();
    }
}