package self.input;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Preset {
    private int key;
    private KeyListener action;

    public static final ArrayList<Preset> todosPresets =  new ArrayList<>();

    public Preset(int key, KeyListener action) {
        this.key = key;
        this.action = action;

        todosPresets.add(this);
    }

    public void handle(KeyEvent e) {
        if (e.getKeyCode() == this.key && e.isControlDown()) {
            this.action.acao(e);
        }
    }
}
