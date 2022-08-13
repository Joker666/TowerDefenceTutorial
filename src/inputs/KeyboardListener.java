package inputs;

import main.Game;
import main.GameStates;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static main.GameStates.EDIT;
import static main.GameStates.PLAYING;

public class KeyboardListener implements KeyListener {
    private final Game game;

    public KeyboardListener(Game game) {
        this.game = game;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (GameStates.gameState == EDIT)
            game.getEditor().keyPressed(e);
        else if (GameStates.gameState == PLAYING)
            game.getPlaying().keyPressed(e);

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
    }
}
