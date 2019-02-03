package gui.window;

import game.Game;
import gui.GUIComponent;
import gui.JustText;
import gui.SimpleButton;
import gui.TextBox;


public class Dialog extends Window {
    public String label;
    public int stringWidth;
    private WindowCallback callback;
    public String text;
    public Dialog(int id, int x, int y, int width, int height, String label, boolean actionBarEnabled, Object callback) {
        super(id, x, y, width, height, actionBarEnabled);
        this.label = label;
        this.stringWidth = Game.game.getGraphics().getFontMetrics().stringWidth(label);
        this.callback = (WindowCallback)callback;
        addComponent(new JustText(0, 0, 0, stringWidth, Game.game.getGraphics().getFontMetrics().getHeight(), this, null, this, null, 0, 0, 0, 0, label, true, 10));
        addComponent(new TextBox(1, getComponentById(0).width, 20, 0, Game.game.getGraphics().getFontMetrics().getHeight(), this, this, this, null, 10, 0, 0, 0, ""));
        addComponent(new SimpleButton(2, (width / 2) - 100, 0.5, 100, 25, this, this, null, this, 0, 0, 0, 0, "OK"));
    }

    @Override
    public void actionPerformed(GUIComponent guiComponent) {
        super.actionPerformed(guiComponent);
        if(guiComponent.id == 2){

            text = ((TextBox)getComponentById(1)).text;
            callback.onEvent(text);
        }
    }
}
