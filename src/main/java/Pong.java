import engine.Ball;
import engine.GameEngine;
import engine.Greta;

public class Pong  {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 400;

    private final Ball ball = Ball.create(WIDTH, HEIGHT);
    private final Greta greta = Greta.create(WIDTH, HEIGHT);
    private final GameEngine gameEngine = GameEngine.create(WIDTH, HEIGHT);

    public void init() {
        // todo you code goes here
    }

    public static void main(String[] args) {
        new Pong().init();
    }
}