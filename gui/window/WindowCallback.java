package gui.window;

import gui.GUI;
import gui.GUIComponent;


public interface WindowCallback {
    void onEvent(GUIComponent component, String... args);
}
