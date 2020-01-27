package engine;

import io.reactivex.*;
import io.reactivex.schedulers.Schedulers;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;

class GameEngineImpl extends JFrame implements GameEngine {

    private static final String RENDER_THREAD_NAME = "RenderThread";
    private static final int MAX_EVENTS_PER_SEC = 30;

    private final KeyListener keyListener = new KeyListener();

    private final int width;
    private final int height;

    private Point padA = new Point(0, 0);
    private Point padB = new Point(0, 0);
    private Point ball = new Point(0, 0);

    private Scheduler renderScheduler = null;

    private int eventsCounter = 0;
    private long counterLastResetTime = -1;

    public GameEngineImpl(int width, int height) {
        this.width = width;
        this.height = height;

        this.setTitle("Pong!");
        this.setSize(new Dimension(width, height));
        this.setResizable(false);
        this.setVisible(true);
        this.setBackground(Color.DARK_GRAY);
        this.addKeyListener(keyListener);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void drawSinglePlayer(Point playerLocation) {
        assertOnRenderThread();
        runEventsCounter();

        this.padA = clampToFrame(playerLocation.x, playerLocation.y, GretaImpl.playerWidth, GretaImpl.playerHeight);
        this.padB = clampToFrame(-1000, -1000, GretaImpl.playerWidth, GretaImpl.playerHeight);
        this.ball = clampToFrame(-1000, -1000, BallImpl.ballWidth, BallImpl.ballHeight);
        repaint();
    }

    @Override
    public void drawFrame(Point yourLocation, Point gretaLocation, Point ballLocation) {
        assertOnRenderThread();
        runEventsCounter();

        this.padA = clampToFrame(yourLocation.x, yourLocation.y, GretaImpl.playerWidth, GretaImpl.playerHeight);
        this.padB = clampToFrame(gretaLocation.x, gretaLocation.y, GretaImpl.playerWidth, GretaImpl.playerHeight);
        this.ball = clampToFrame(ballLocation.x, ballLocation.y, BallImpl.ballWidth, BallImpl.ballHeight);
        repaint();
    }

    @Override
    public Scheduler getRenderThread() {
        if (renderScheduler == null) {
            renderScheduler = Schedulers.from(Executors.newSingleThreadScheduledExecutor(r -> {
                final Thread thread = new Thread(r);
                thread.setName(RENDER_THREAD_NAME);
                return thread;
            }));
        }
        return renderScheduler;
    }

    @Override
    public Observable<Keys> getClicks() {
        return keyListener.getKeysEvents()
                .toFlowable(BackpressureStrategy.BUFFER)
                .flatMap(event -> Flowable.range(0, 100).map(i -> event))
                .toObservable();
    }

    @Override
    public void paint(Graphics g) {
        Image gameImage = createImage(getWidth(), getHeight());
        Graphics graphics = gameImage.getGraphics();

        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(ball.x, ball.y, BallImpl.ballWidth, BallImpl.ballHeight);
        graphics.setColor(Color.CYAN);
        graphics.fillRect(padA.x, padA.y, GretaImpl.playerWidth, GretaImpl.playerHeight);
        graphics.setColor(Color.MAGENTA);
        graphics.fillRect(padB.x, padB.y, GretaImpl.playerWidth, GretaImpl.playerHeight);

        g.drawImage(gameImage, 0, 0, this);
    }

    private synchronized void runEventsCounter() {
        eventsCounter++;
        if (eventsCounter > MAX_EVENTS_PER_SEC) {
            if (counterLastResetTime != -1 && (System.currentTimeMillis() - counterLastResetTime) <= 1000) {
                throw new IllegalStateException("backend.GameEngine: too many frames to draw");
            } else {
                counterLastResetTime = System.currentTimeMillis();
                eventsCounter = 0;
            }
        }
    }

    private void assertOnRenderThread() {
        if (!RENDER_THREAD_NAME.equals(Thread.currentThread().getName())) {
            throw new IllegalStateException("backend.GameEngine: you must call drawFrame on the render thread");
        }
    }

    private Point clampToFrame(int x, int y, int elementWidth, int elementHeight) {
        if (x + elementWidth >= width) {
            x = width - elementWidth;
        }

        if (x < 0) {
            x = 0;
        }

        if (y + elementHeight >= height) {
            y = height - elementHeight;
        }

        if (y < 0) {
            y = 0;
        }

        return new Point(x, y);
    }
}
