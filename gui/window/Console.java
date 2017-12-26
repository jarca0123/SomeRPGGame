package gui.window;

import game.GamePanel;
import graphics.JGraphics2D;
import gui.GUIComponent;
import gui.TextArea;
import gui.TextBox;

import java.awt.*;
import java.awt.event.KeyEvent;


public class Console extends Window {
    private int width = 100;
    private int height = 100;
    int minimalHeight = 100;
    int minimalWidth = 100;
    public Console(int id) {
        super(id, true);
        addComponent(new TextArea(-2, 0, 0, width, height, this, this, this, this, 0, 0, 0, 15));
        addComponent(new TextBox(-3, 0, 0, width, 15, this, this, false, this, 0, 0, 0, 25, ""));
    }

    public Console(int id, int x, int y, int width, int height) {
        super(id, x, y, width, height, true);
    }

    @Override
    public void actionPerformed(GUIComponent guiComponent) {
        super.actionPerformed(guiComponent);

    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        ((TextArea)getComponentById(-2)).text = GamePanel.consoleLog;
    }

    @Override
    public void enterPerformed(GUIComponent guiComponent) {
        super.enterPerformed(guiComponent);
        if(guiComponent.id == -3){
            String command = ((gui.TextBox)guiComponent).text.toString();
            ((gui.TextBox)guiComponent).text = "";
            GamePanel.processCommands(command);
        }
    }

    @Override
    public void keyPerformed(GUIComponent guiComponent, KeyEvent e) {
        super.keyPerformed(guiComponent, e);
        if(e.getKeyCode() == 59){
            this.visible = false;
        }
    }
}
