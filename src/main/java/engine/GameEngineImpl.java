package engine;

import io.reactivex.*;
import io.reactivex.schedulers.Schedulers;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

class GameEngineImpl extends JFrame implements GameEngine {

    private static final String RENDER_THREAD_NAME = "RenderThread";
    private static final int MAX_EVENTS_PER_SEC = 20;

    private final KeyListener keyListener = new KeyListener();

    private final int width;
    private final int height;

    private Point padA = new Point(0, 0);
    private Point padB = new Point(0, 0);
    private Point ball = new Point(0, 0);

    private Scheduler renderScheduler = null;

    private AtomicInteger eventsCounter = new AtomicInteger(0);
    private AtomicLong counterLastResetTime = new AtomicLong(-1);

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
    public void drawFrame(Point padA, Point padB, Point ball) {
        assertOnRenderThread();
        runEventsCounter();

        this.padA = clampToFrame(padA.x, padA.y + 30, RemotePlayerImpl.playerWidth, RemotePlayerImpl.playerHeight);
        this.padB = clampToFrame(padB.x, padB.y + 30, RemotePlayerImpl.playerWidth, RemotePlayerImpl.playerHeight);
        this.ball = clampToFrame(ball.x, ball.y + 30, RemoteBallImpl.ballWidth, RemoteBallImpl.ballHeight);
        repaint();
        try { Thread.sleep(120); } catch (InterruptedException ignored) { }
    }

    @Override
    public Scheduler getRenderScheduler() {
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
    public Observable<Keys> getKeys() {
        return keyListener.getKeysEvents().toFlowable(BackpressureStrategy.BUFFER).toObservable();
    }

    @Override
    public void paint(Graphics g) {
        Image gameImage = createImage(getWidth(), getHeight());
        Graphics graphics = gameImage.getGraphics();

        graphics.setColor(Color.CYAN);
        graphics.fillRect(padA.x, padA.y, RemotePlayerImpl.playerWidth, RemotePlayerImpl.playerHeight);
        graphics.setColor(Color.MAGENTA);
        graphics.fillRect(padB.x, padB.y, RemotePlayerImpl.playerWidth, RemotePlayerImpl.playerHeight);
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(ball.x, ball.y, RemoteBallImpl.ballWidth, RemoteBallImpl.ballHeight);

        g.drawImage(gameImage, 0, 0, this);
    }

    private void runEventsCounter() {
        if (eventsCounter.incrementAndGet() > MAX_EVENTS_PER_SEC) {
            final long lastReset = counterLastResetTime.get();
            if (lastReset != -1 && System.currentTimeMillis() - lastReset >= 1000) {
                throw new IllegalStateException("backend.GameEngine: too many frames to draw");
            } else {
                counterLastResetTime.set(System.currentTimeMillis());
                eventsCounter.set(0);
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
