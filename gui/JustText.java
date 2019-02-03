package gui;

import game.Game;
import graphics.JGraphics2D;

import java.awt.*;


public class JustText extends GUIComponent{
    public int fontSize;
    public String text;
    public boolean center;
    public Font font;
    public JustText(int id, String text, boolean center, int fontSize) {
        super(id);
        this.text = text;
        this.center = center;
        this.fontSize = fontSize;
    }



    public JustText(int id, double x, double y, double width, double height, Object alignLeft, Object alignRight, Object alignUp, Object alignDown, int leftMargin, int rightMargin, int upMargin, int downMargin, String text, boolean center, int fontSize) {
        super(id, x, y, width, height, alignLeft, alignRight, alignUp, alignDown, leftMargin, rightMargin, upMargin, downMargin);
        this.text = text;

        this.center = center;
        this.fontSize = fontSize;
        this.font = new Font("Determination Mono", Font.BOLD, fontSize);

    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);

        g2d.setColor(Color.white);



        g2d.setFont(new Font("Determination Mono", Font.BOLD, fontSize));
        if(center) {
            drawCenteredString(g2d, text, bounds, g2d.getFont());
        } else {
            g2d.drawString(text, x, y + g2d.getFontMetrics().getHeight());
        }
    }
}
