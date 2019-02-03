package gui;

import game.Game;

import java.awt.*;


public class InteractionText extends GUIComponent{
    public String text;
    private String renderedText;

    public int fontSize = 28;
    private boolean fightSelecting;

    public InteractionText(int id, String text) {
        super(id);
        this.text = text;
    }

    public InteractionText(int id, int x, int y, int width, int height, String text) {
        super(id, x, y, width, height);
        this.text = text;
    }

    public InteractionText(int id, double x, double y, double width, double height, Object alignLeft, Object alignRight, Object alignUp, Object alignDown, int leftMargin, int rightMargin, int upMargin, int downMargin, String text, boolean fightSelecting) {
        super(id, x, y, width, height, alignLeft, alignRight, alignUp, alignDown, leftMargin, rightMargin, upMargin, downMargin);
        this.text = text;
        this.fightSelecting = fightSelecting;
    }

    public InteractionText(int id, double x, double y, double width, double height, Object alignLeft, Object alignRight, Object alignUp, Object alignDown, int leftMargin, int rightMargin, int upMargin, int downMargin, String text, int fontSize, boolean fightSelecting) {
        super(id, x, y, width, height, alignLeft, alignRight, alignUp, alignDown, leftMargin, rightMargin, upMargin, downMargin);
        this.text = text;
        this.fontSize = fontSize;
        this.fightSelecting = fightSelecting;
    }



    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);

        g2d.setColor(Color.white);
        if(windowParent != null) g2d.setClip(this.windowParent.entireWindowBounds);


        Font tempFont = g2d.getFont();
        g2d.setFont(new Font(tempFont.getName(), tempFont.getStyle(), fontSize));
        if(fightSelecting) {
            g2d.drawString("* " + text, x, y + g2d.getFontMetrics().getHeight());
        } else {
            g2d.drawString(text, x, y + g2d.getFontMetrics().getHeight());
        }

        g2d.setFont(tempFont);

    }
}
