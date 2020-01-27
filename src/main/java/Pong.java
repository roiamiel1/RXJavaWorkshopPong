import engine.GameEngine;
import engine.RemoteBall;
import engine.RemotePlayer;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public class Pong  {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 400;

    private final RemoteBall remoteBall = RemoteBall.create(WIDTH, HEIGHT);
    private final RemotePlayer remotePlayer = RemotePlayer.create(WIDTH, HEIGHT);
    private final GameEngine gameEngine = GameEngine.create(WIDTH, HEIGHT);

    public void init() {
        Observable.combineLatest(remoteBall.getLocation(),
                remotePlayer.getLocation(), (ball, player) -> {
                      
                    return null;
                }).subscribe();
    }

    public static void main(String[] args) {
        new Pong().init();
    }
}