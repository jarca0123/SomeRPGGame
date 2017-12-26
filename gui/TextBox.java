package gui;

import game.Game;
import graphics.JGraphics2D;

import java.awt.*;
import java.awt.event.KeyEvent;


public class TextBox extends GUIComponent{
    public String placeholder = "";
    public String text = "";

    public TextBox(int id, String placeholder) {
        super(id);
    }

    public TextBox(int id, int x, int y, int width, int height, String placeholder) {
        super(id, x, y, width, height);
        this.text = placeholder;
    }

    public TextBox(int id, double x, double y, double width, double height, Object alignLeft, Object alignRight, Object alignUp, Object alignDown, int leftMargin, int rightMargin, int upMargin, int downMargin, String placeholder) {
        super(id, x, y, width, height, alignLeft, alignRight, alignUp, alignDown, leftMargin, rightMargin, upMargin, downMargin);
        this.text = placeholder;
    }



    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2d.setColor(Color.blue);



        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.white);
        g2d.setClip(windowParent.windowBounds);
        g2d.drawRect(x, y, width, height);
        drawCenteredString(g2d, text, bounds, g2d.getFont());
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    @Override
    public void onKey(KeyEvent e) {
        super.onKey(e);
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (text.length() > 0)
                text = text.substring(0, text.length() - 1);
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            onEnter(e);
        } else {
            text += e.getKeyChar();
        }
    }
}
