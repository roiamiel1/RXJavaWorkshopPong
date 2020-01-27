package engine;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

class KeyListener implements java.awt.event.KeyListener {

    private Keys lastKey = Keys.NONE;

    public Observable<Keys> getKeysEvents() {
        return Observable.interval(1, TimeUnit.MICROSECONDS).map(i -> lastKey);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        final int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_UP) {
            lastKey = Keys.UP;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            lastKey = Keys.DOWN;
        } else {
            lastKey = Keys.NONE;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysEvents.onNext(Keys.NONE);
    }
}
