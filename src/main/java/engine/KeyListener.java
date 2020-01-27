package engine;

import io.reactivex.subjects.PublishSubject;

import java.awt.event.KeyEvent;

class KeyListener implements java.awt.event.KeyListener {

    private PublishSubject<Keys> keysEvents = PublishSubject.create();

    public PublishSubject<Keys> getKeysEvents() {
        return keysEvents;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        final int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_UP) {
            keysEvents.onNext(Keys.UP);
        } else if (keyCode == KeyEvent.VK_DOWN) {
            keysEvents.onNext(Keys.DOWN);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
