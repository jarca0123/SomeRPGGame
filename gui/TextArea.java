package gui;

import game.Game;
import game.GamePanel;
import graphics.JGraphics2D;

import java.awt.*;
import java.awt.event.KeyEvent;


public class TextArea extends GUIComponent {
    private String placeholder = "";
    public String text = "";
    private Font font;

    public TextArea(int id, String placeholder) {
        super(id);
    }

    public TextArea(int id, int x, int y, int width, int height, String placeholder) {
        super(id, x, y, width, height);
        this.placeholder = placeholder;
    }

    public TextArea(int id, int x, int y, int width, int height, String text, Font font) {
        super(id, x, y, width, height);
        this.text = text;
        if(font == null){
            this.font = Game.gamePanel.getFont();
        } else {
            this.font = font;
        }
    }


    public TextArea(int id, int x, int y, int width, int height, Object alignLeft, Object alignRight, Object alignUp, Object alignDown, int leftMargin, int rightMargin, int upMargin, int downMargin) {
        super(id, x, y, width, height, alignLeft, alignRight, alignUp, alignDown, leftMargin, rightMargin, upMargin, downMargin);
    }

    public TextArea(int id, int x, int y, int width, int height, Object alignLeft, Object alignRight, Object alignUp, Object alignDown) {
        super(id, x, y, width, height, alignLeft, alignRight, alignUp, alignDown);
    }

    private void drawString(Graphics g, String text, int x, int y) {
        for (String line : text.split("\n"))
            g.drawString(line, x, contentOffsetY + (y += g.getFontMetrics().getHeight()));
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);
        int tempContentHeight = 0;
        int tempContentWidth = 0;
        tempContentHeight = text.split("\n").length * g2d.getFontMetrics().getHeight();
        for (String line : text.split("\n")) {
            if (g2d.getFontMetrics().stringWidth(text) > tempContentWidth) {
                tempContentWidth = g2d.getFontMetrics().stringWidth(text);
            }
        }
        contentHeight = tempContentHeight;
        contentWidth = tempContentWidth;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2d.setColor(Color.PINK);
        g2d.setClip(x, y, width, height);
        g2d.drawRect((int)bounds.getX(), (int)bounds.getY(), (int)bounds.getWidth(), (int)bounds.getHeight());
        Font tempFont = g2d.getFont();
        g2d.setFont(font);
        drawString(g2d, text, x, y);
        g2d.setFont(tempFont);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    @Override
    public void onKey(KeyEvent e) {
        super.onKey(e);
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (text.length() > 0)
                text = text.substring(0, text.length() - 1);
        } else {
            text += e.getKeyChar();
        }
    }
}
