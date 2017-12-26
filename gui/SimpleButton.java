package gui;

import graphics.JGraphics2D;

import java.awt.*;


public class SimpleButton extends GUIComponent {
    private String text;
    private boolean isSelected = false;
    public SimpleButton(int id, String text) {
        super(id);
        this.text = text;
    }

    public SimpleButton(int id, int x, int y, int width, int height, Object alignLeft, Object alignRight, Object alignUp, Object alignDown, int leftMargin, int rightMargin, int upMargin, int downMargin) {
        super(id, x, y, width, height, alignLeft, alignRight, alignUp, alignDown, leftMargin, rightMargin, upMargin, downMargin);
    }

    public SimpleButton(int id, double x, double y, double width, double height, Object alignLeft, Object alignRight, Object alignUp, Object alignDown, int leftMargin, int rightMargin, int upMargin, int downMargin, String text) {
        super(id, x, y, width, height, alignLeft, alignRight, alignUp, alignDown, leftMargin, rightMargin, upMargin, downMargin);
        this.text = text;
    }

    public SimpleButton(int id, int x, int y, int width, int height, String text) {
        super(id, x, y, width, height);
        this.text = text;
    }

    @Override
    public void draw(Graphics2D g2d) {
        super.draw(g2d);

        if(isSelected) g2d.setColor(Color.red);
        if(windowParent != null && id != -1)g2d.setClip(windowParent.windowBounds);
        g2d.setColor(Color.white);
        g2d.drawRect(x, y, width, height);
        drawCenteredString(g2d, text, new Rectangle(x, y, width, height), g2d.getFont());
    }
}
